package com.rbstarbuck.scribble.game.draw

import com.rbstarbuck.scribble.game.brush.BrushType
import com.rbstarbuck.scribble.game.brush.FillType
import com.rbstarbuck.scribble.game.color.HSVColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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

    private val _recomposeStateFlow = MutableStateFlow(false)
    val recomposeCurrentStrokeStateFlow = _recomposeStateFlow.asStateFlow()

    fun beginStroke(x: Float, y: Float) {
        _currentStroke = MutableStroke(
            color = selectedColor.value,
            width = selectedWidth.value,
            brushType = selectedBrushType.value,
            fillType = selectedFillType.value,
            initialPoint = Point(x, y)
        )
        recompose()
    }

    fun appendStroke(x: Float, y: Float) {
        _currentStroke!!.addPoint(Point(x, y))
        recompose()
    }

    fun moveCurrentStrokePoint(x: Float, y: Float) {
        val currentPoint = _currentStroke!!.points.last()
        currentPoint.x = x
        currentPoint.y = y
        recompose()
    }

    fun endStroke() {
        _committedStrokes.add(_currentStroke!!)
        _currentStroke = null
        recompose()
    }

    fun mergeInto(other: Strokes) {
        other._committedStrokes.addAll(committedStrokes)
    }

    fun firstPoint() = currentStroke!!.points.first()

    fun lastPoint() = currentStroke!!.points.last()

    fun discardLastPoint() = _currentStroke!!.discardLastPoint()

    private fun recompose() {
        _recomposeStateFlow.value = !_recomposeStateFlow.value
    }
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
    private val _points = mutableListOf<Point>(initialPoint)
    override val points: List<Point>
        get() = _points

    fun addPoint(point: Point) {
        _points.add(point)
    }

    fun discardLastPoint() = _points.removeAt(points.size - 1)
}

data class Point(
    var x: Float,
    var y: Float
)