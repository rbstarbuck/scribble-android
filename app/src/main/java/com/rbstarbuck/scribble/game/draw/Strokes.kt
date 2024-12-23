package com.rbstarbuck.scribble.game.draw

import android.graphics.Matrix
import androidx.compose.ui.geometry.Offset
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

    fun beginStroke(x: Float, y: Float) {
        _currentStroke = MutableStroke(
            color = selectedColor.value,
            width = selectedWidth.value,
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

    fun translate(x: Float, y: Float) {
        for (stroke in committedStrokes) {
            for (point in stroke.points) {
                point.x += x
                point.y += y
            }
        }

        _recomposeCommittedStrokesStateFlow.value = !recomposeCommittedStrokesStateFlow.value
    }

    fun scale(dX: Float, dY: Float) {
        val centroid = centroid()

        val matrix = Matrix()
        matrix.postScale(dX, dY, centroid.x, centroid.y)

        mapPoints { dstArray, srcArray ->
            matrix.mapPoints(dstArray, srcArray)
        }
    }

    fun rotate(degrees: Float) {
        val centroid = centroid()

        val matrix = Matrix()
        matrix.postRotate(degrees, centroid.x, centroid.y)

        mapPoints { dstArray, srcArray ->
            matrix.mapPoints(dstArray, srcArray)
        }
    }

    fun centroid(): Offset {
        var minX = Float.MAX_VALUE
        var maxX = Float.MIN_VALUE
        var minY = Float.MAX_VALUE
        var maxY = Float.MIN_VALUE

        for (stroke in committedStrokes) {
            for (point in stroke.points) {
                if (point.x < minX) minX = point.x
                if (point.x > maxX) maxX = point.x
                if (point.y < minY) minY = point.y
                if (point.y > maxY) maxY = point.y
            }
        }

        return Offset(
            x = (minX + maxX) / 2f,
            y = (minY + maxY) / 2f
        )
    }

    fun mapPoints(mapping: (FloatArray, FloatArray) -> Unit) {
        for (stroke in committedStrokes) {
            val points = mutableListOf<Float>()

            for (point in stroke.points) {
                points.add(point.x)
                points.add(point.y)
            }

            val srcPoints = points.toFloatArray()
            val dstPoints = FloatArray(points.size)

            mapping(dstPoints, srcPoints)

            for (i in 0..<dstPoints.size) {
                if (i % 2 == 0) {
                    stroke.points[i / 2].x = dstPoints[i]
                    stroke.points[i / 2].y = dstPoints[i + 1]
                }
            }
        }
    }

    fun firstPoint() = currentStroke!!.points.first()

    fun lastPoint() = currentStroke!!.points.last()
}

interface Stroke {
    val color: HSVColor
    val width: Float
    val brushType: BrushType
    val fillType: FillType
    val points: List<Point>
}

class MutableStroke(
    override val color: HSVColor,
    override val width: Float,
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