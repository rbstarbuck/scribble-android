package com.rbstarbuck.scribble.game.draw

import android.graphics.Matrix
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.rbstarbuck.scribble.game.brush.BrushType
import com.rbstarbuck.scribble.game.brush.FillType
import com.rbstarbuck.scribble.game.color.HSVColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

private const val MIN_SIZE = 0.05f

class Strokes(
    private val selectedColor: StateFlow<HSVColor>,
    private val selectedWidth: StateFlow<Float>,
    val selectedBrushType: StateFlow<BrushType>,
    val selectedFillType: StateFlow<FillType>
) {
    private var _currentStroke: MutableStroke? = null
    val currentStroke: Stroke?
        get() = _currentStroke

    private val _committedStrokes = mutableListOf<Stroke>()
    val committedStrokes: List<Stroke>
        get() = _committedStrokes

    private val _recomposeCurrentStrokesStateFlow = MutableStateFlow(false)
    val recomposeCurrentStrokesStateFlow = _recomposeCurrentStrokesStateFlow.asStateFlow()

    private val _recomposeCommittedStrokesStateFlow = MutableStateFlow(false)
    val recomposeCommittedStrokesStateFlow = _recomposeCommittedStrokesStateFlow.asStateFlow()

    private val _translationStateFlow = MutableStateFlow(Offset.Zero)
    val translationStateFlow = _translationStateFlow.asStateFlow()

    private val _scaleStateFlow = MutableStateFlow(Offset(1f, 1f))
    val scaleStateFlow = _scaleStateFlow.asStateFlow()

    private val _rotationZStateFlow = MutableStateFlow(0f)
    val rotationZStateFlow = _rotationZStateFlow.asStateFlow()

    fun beginStroke(x: Float, y: Float) {
        _currentStroke = MutableStroke(
            color = selectedColor.value,
            width = selectedWidth.value,
            unscaledWidth = selectedWidth.value,
            brushType = selectedBrushType.value,
            fillType = selectedFillType.value,
            initialPoint = Point(x, y)
        )
        _recomposeCurrentStrokesStateFlow.value = !recomposeCurrentStrokesStateFlow.value
    }

    fun appendStroke(x: Float, y: Float) {
        _currentStroke!!.addPoint(Point(x, y))
        _recomposeCurrentStrokesStateFlow.value = !recomposeCurrentStrokesStateFlow.value
    }

    fun moveCurrentStrokePoint(x: Float, y: Float) {
        val currentPoint = _currentStroke!!.points.last()
        currentPoint.x = x
        currentPoint.y = y
        _recomposeCurrentStrokesStateFlow.value = !recomposeCurrentStrokesStateFlow.value
    }

    fun endStroke() {
        _committedStrokes.add(_currentStroke!!)
        _currentStroke = null
        CoroutineScope(Dispatchers.Default).launch {
            delay(100L)
            _recomposeCommittedStrokesStateFlow.value = !recomposeCommittedStrokesStateFlow.value
        }
    }

    fun mergeInto(other: Strokes) {
        other._committedStrokes.addAll(committedStrokes)
    }

    fun undo() {
        if (_currentStroke != null) {
            _currentStroke = null
        } else {
            _committedStrokes.removeLastOrNull()
        }

        _recomposeCommittedStrokesStateFlow.value = !recomposeCommittedStrokesStateFlow.value
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

        _recomposeCommittedStrokesStateFlow.value = !recomposeCommittedStrokesStateFlow.value
        _translationStateFlow.value += Offset(x, y)
    }

    fun scale(
        dX: Float,
        dY: Float
    ) {
        val box = boundingBox()
        val scaleX = box.width * dX >= MIN_SIZE
        val scaleY = box.height * dY >= MIN_SIZE

        val matrix = Matrix()
        if (scaleX) matrix.postScale(dX, 1f, box.center.x, box.center.y)
        if (scaleY) matrix.postScale(1f, dY, box.center.x, box.center.y)

        if (scaleX || scaleY) {
            mapPoints { dstArray, srcArray ->
                matrix.mapPoints(dstArray, srcArray)
            }

            _recomposeCommittedStrokesStateFlow.value = !recomposeCommittedStrokesStateFlow.value
            _scaleStateFlow.value += Offset(
                x = if (scaleX) dX - 1 else 0f,
                y = if (scaleY) dY - 1 else 0f
            )
        }
    }

    fun rotateZ(degrees: Float) {
        val box = boundingBox()

        val matrix = Matrix()
        matrix.postRotate(degrees, box.center.x, box.center.y)

        mapPoints { dstArray, srcArray ->
            matrix.mapPoints(dstArray, srcArray)
        }

        var totalRotation = _rotationZStateFlow.value + degrees
        while (totalRotation > 180f) {
            totalRotation -= 360f
        }
        while (totalRotation < -180f) {
            totalRotation += 360f
        }

        _recomposeCommittedStrokesStateFlow.value = !recomposeCommittedStrokesStateFlow.value
        _rotationZStateFlow.value = totalRotation
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

    fun mapPoints(mapping: (srcArray: FloatArray, dstArray: FloatArray) -> Unit) {
        for (stroke in committedStrokes) {
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
    }

    fun firstPoint() = currentStroke!!.points.first()

    fun lastPoint() = currentStroke!!.points.last()
}

interface Stroke {
    val color: HSVColor
    var width: Float
    val unscaledWidth: Float
    val brushType: BrushType
    val fillType: FillType
    val points: List<Point>
}

class MutableStroke(
    override val color: HSVColor,
    override var width: Float,
    override val unscaledWidth: Float,
    override val brushType: BrushType,
    override val fillType: FillType,
    initialPoint: Point
): Stroke {
    private val _points = mutableListOf(initialPoint)
    override val points: List<Point>
        get() = _points

    fun addPoint(point: Point) {
        _points.add(point)
    }
}

data class Point(
    var x: Float,
    var y: Float
)