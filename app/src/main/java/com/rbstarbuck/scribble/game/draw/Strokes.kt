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
    private val selectedBrushType: StateFlow<BrushType>,
    private val selectedFillType: StateFlow<FillType>
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
    }

    fun appendStroke(x: Float, y: Float) {
        _currentStroke!!.addPoint(Point(x, y))
        _recomposeStateFlow.value = !_recomposeStateFlow.value
    }

    fun endStroke() {
        _committedStrokes.add(_currentStroke!!)
        _currentStroke = null
        _recomposeStateFlow.value = !_recomposeStateFlow.value
    }

    fun mergeInto(other: Strokes) {
        other._committedStrokes.addAll(committedStrokes)
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
}

data class Point(
    val x: Float,
    val y: Float
)