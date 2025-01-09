package com.rbstarbuck.scribble.game.draw

import android.graphics.Matrix
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.rbstarbuck.scribble.game.brush.BrushType
import com.rbstarbuck.scribble.game.brush.FillType
import com.rbstarbuck.scribble.game.color.HSVColor
import com.rbstarbuck.scribble.koin.state.SelectedBrushType
import com.rbstarbuck.scribble.koin.state.SelectedColor
import com.rbstarbuck.scribble.koin.state.SelectedFillType
import com.rbstarbuck.scribble.koin.state.SelectedStrokeWidth
import com.rbstarbuck.scribble.util.generateKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import kotlin.getValue

private const val MIN_SIZE = 0.05f

class Strokes {
    private val selectedColor: SelectedColor by inject(SelectedColor::class.java)
    private val selectedStrokeWidth: SelectedStrokeWidth by inject(SelectedStrokeWidth::class.java)
    private val selectedBrushType: SelectedBrushType by inject(SelectedBrushType::class.java)
    private val selectedFillType: SelectedFillType by inject(SelectedFillType::class.java)

    private var _currentStroke: MutableStroke? = null
    val currentStroke: Stroke?
        get() = _currentStroke

    private val _committedStrokes = mutableListOf<MutableStroke>()
    val committedStrokes: List<Stroke>
        get() = _committedStrokes

    private val _recomposeCurrentStrokesStateFlow = MutableStateFlow(0)
    val recomposeCurrentStrokesStateFlow = _recomposeCurrentStrokesStateFlow.asStateFlow()

    private val _recomposeCommittedStrokesStateFlow = MutableStateFlow(0)
    val recomposeCommittedStrokesStateFlow = _recomposeCommittedStrokesStateFlow.asStateFlow()

    private val _translationStateFlow = MutableStateFlow(Offset.Zero)
    val translationStateFlow = _translationStateFlow.asStateFlow()

    private val _scaleStateFlow = MutableStateFlow(Offset(1f, 1f))
    val scaleStateFlow = _scaleStateFlow.asStateFlow()

    private val _rotationStateFlow = MutableStateFlow(0f)
    val rotationStateFlow = _rotationStateFlow.asStateFlow()

    fun beginStroke(x: Float, y: Float) {
        _currentStroke = MutableStroke(
            color = selectedColor.color,
            width = selectedStrokeWidth.width,
            unscaledWidth = selectedStrokeWidth.width,
            brushType = selectedBrushType.brushType,
            fillType = selectedFillType.fillType,
            initialPoint = Point(x, y)
        )

        _recomposeCurrentStrokesStateFlow.value += 1
    }

    fun appendStroke(x: Float, y: Float) {
        _currentStroke!!.addPoint(Point(x, y))

        _recomposeCurrentStrokesStateFlow.value += 1
    }

    fun moveCurrentStrokePoint(x: Float, y: Float) {
        val currentPoint = _currentStroke!!.points.last()
        currentPoint.x = x
        currentPoint.y = y

        _recomposeCurrentStrokesStateFlow.value += 1
    }

    fun moveRectangleStrokePoints(x: Float, y: Float) {
        val points = currentStroke!!.points

        points[1].x = x
        points[2].x = x
        points[2].y = y
        points[3].y = y

        _recomposeCurrentStrokesStateFlow.value += 1
    }

    fun moveCircleStrokePoints(x: Float, y: Float, center: Offset) {
        val dstArray = Array(60) { FloatArray(2) }
        val srcArray = Array(60) { FloatArray(2) }

        var i = 0
        val matrix = Matrix()
        for (point in currentStroke!!.points) {
            srcArray[i][0] = x
            srcArray[i][1] = y

            matrix.setRotate(i * 6f, center.x, center.y)
            matrix.mapPoints(dstArray[i], srcArray[i])

            point.x = dstArray[i][0]
            point.y = dstArray[i][1]

            ++i
        }

        _recomposeCurrentStrokesStateFlow.value += 1
    }

    fun endStroke() {
        _committedStrokes.add(_currentStroke!!)
        _currentStroke = null
        CoroutineScope(Dispatchers.Default).launch {
            delay(100L)
            _recomposeCommittedStrokesStateFlow.value += 1
        }
    }

    fun copy(): Strokes {
        val other = Strokes()

        for (stroke in _committedStrokes) {
            other._committedStrokes.add(stroke.copy())
        }

        return other
    }

    fun mergeInto(other: Strokes) {
        other._committedStrokes.addAll(_committedStrokes)
    }

    fun undo() {
        if (_currentStroke != null) {
            _currentStroke = null
        } else {
            _committedStrokes.removeLastOrNull()
        }

        _recomposeCommittedStrokesStateFlow.value += 1
    }

    fun translate(
        x: Float,
        y: Float
    ) {
        for (stroke in committedStrokes) {
            for (point in stroke.points) {
                point.x += x
                point.y += y
            }
        }

        _recomposeCommittedStrokesStateFlow.value += 1
        _translationStateFlow.value += Offset(x, y)
    }

    fun scale(
        dX: Float,
        dY: Float
    ) {
        val bounds = boundingBox()
        val scaleX = bounds.width * dX >= MIN_SIZE
        val scaleY = bounds.height * dY >= MIN_SIZE

        val matrix = Matrix()
        if (scaleX) matrix.postScale(dX, 1f, bounds.center.x, bounds.center.y)
        if (scaleY) matrix.postScale(1f, dY, bounds.center.x, bounds.center.y)

        if (scaleX || scaleY) {
            for (stroke in committedStrokes) {
                mapPoints(stroke) { dstArray, srcArray ->
                    matrix.mapPoints(dstArray, srcArray)
                }
            }

            _scaleStateFlow.value += Offset(
                x = if (scaleX) dX - 1 else 0f,
                y = if (scaleY) dY - 1 else 0f
            )
            _recomposeCommittedStrokesStateFlow.value += 1
        }
    }

    fun rotateZ(
        degrees: Float,
        strokesCenter: Offset
    ) {
        val matrix = Matrix()
        matrix.postRotate(degrees, strokesCenter.x, strokesCenter.y)

        for (stroke in committedStrokes) {
            mapPoints(stroke) { dstArray, srcArray ->
                matrix.mapPoints(dstArray, srcArray)
            }
        }

        var totalRotation = rotationStateFlow.value + degrees
        while (totalRotation > 180f) {
            totalRotation -= 360f
        }
        while (totalRotation < -180f) {
            totalRotation += 360f
        }

        _rotationStateFlow.value = totalRotation
        _recomposeCommittedStrokesStateFlow.value += 1
    }

    fun mapPoints(
        stroke: Stroke,
        mapping: (FloatArray, FloatArray) -> Unit
    ) {
        val srcPoints = FloatArray(stroke.points.size * 2)
        val dstPoints = FloatArray(srcPoints.size)

        stroke.points.forEachIndexed { i, p ->
            val i2 = i * 2
            srcPoints[i2] = p.x
            srcPoints[i2 + 1] = p.y
        }

        mapping(dstPoints, srcPoints)

        stroke.points.forEachIndexed { i, p ->
            val i2 = i * 2
            p.x = dstPoints[i2]
            p.y = dstPoints[i2 + 1]
        }
    }

    fun boundingBox(): Rect {
        var minX = Float.MAX_VALUE
        var maxX = Float.MIN_VALUE
        var minY = Float.MAX_VALUE
        var maxY = Float.MIN_VALUE

        for (stroke in committedStrokes) {
            if (stroke.brushType != BrushType.Eraser) {
                for (point in stroke.points) {
                    if (point.x < minX) minX = point.x
                    if (point.x > maxX) maxX = point.x
                    if (point.y < minY) minY = point.y
                    if (point.y > maxY) maxY = point.y
                }
            }
        }

        return Rect(
            topLeft = Offset(minX, minY),
            bottomRight = Offset(maxX, maxY)
        )
    }

    fun firstPoint() = currentStroke!!.points.first()

    fun lastPoint() = currentStroke!!.points.last()
}

interface Stroke {
    val key: String
    val color: HSVColor
    var width: Float
    val unscaledWidth: Float
    val brushType: BrushType
    val fillType: FillType
    val points: List<Point>
    val initialPoint: Point

    fun copy(): Stroke
}

data class MutableStroke(
    override val key: String = generateKey(),
    override val color: HSVColor,
    override var width: Float,
    override val unscaledWidth: Float,
    override val brushType: BrushType,
    override val fillType: FillType,
    override val initialPoint: Point
): Stroke {
    private val _points = mutableListOf(initialPoint)
    override val points: List<Point>
        get() = _points

    fun addPoint(point: Point) {
        _points.add(point)
    }

    override fun copy(): MutableStroke {
        val other = copy(
            initialPoint = initialPoint.copy(
                x = initialPoint.x,
                y = initialPoint.y
            )
        )

        for (point in points) {
            other.addPoint(point.copy())
        }

        return other
    }
}

data class Point(
    var x: Float,
    var y: Float
)