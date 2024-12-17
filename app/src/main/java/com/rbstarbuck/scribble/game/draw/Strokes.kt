package com.rbstarbuck.scribble.game.draw

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class Strokes(
    private val selectedColor: StateFlow<Color>,
    private val selectedWidth: StateFlow<Float>
): Iterable<Stroke> {
    private var _currentStroke: MutableStroke? = null
    val currentStroke: Stroke?
        get() = _currentStroke

    private val _committedStrokes = mutableListOf<Stroke>()
    val committedStrokes: List<Stroke>
        get() = _committedStrokes

    private val _recomposeCurrentStrokeStateFlow = MutableStateFlow(false)
    val recomposeCurrentStrokeStateFlow = _recomposeCurrentStrokeStateFlow.asStateFlow()

    private val _recomposeCommittedStrokesStateFlow = MutableStateFlow(false)
    val recomposeCommittedStrokesStateFlow = _recomposeCommittedStrokesStateFlow.asStateFlow()

    fun beginStroke(x: Float, y: Float) {
        _currentStroke = MutableStroke(
            color = selectedColor.value,
            width = selectedWidth.value,
            initialPoint = Point(x, y)
        )
    }

    fun appendStroke(x: Float, y: Float) {
        _currentStroke!!.addPoint(Point(x, y))
        _recomposeCurrentStrokeStateFlow.value = !_recomposeCurrentStrokeStateFlow.value
    }

    fun endStroke() {
        _committedStrokes.add(_currentStroke!!)
        _currentStroke = null
        _recomposeCommittedStrokesStateFlow.value = !_recomposeCommittedStrokesStateFlow.value
    }

    override fun iterator(): Iterator<Stroke> = StrokesIterator()

    private inner class StrokesIterator: Iterator<Stroke> {
        private val strokesIterator: Iterator<Stroke> = _committedStrokes.iterator()
        private val lastStroke: MutableStroke? = _currentStroke

        private var hasLastStroke = lastStroke != null

        override fun hasNext(): Boolean = strokesIterator.hasNext() || hasLastStroke

        override fun next(): Stroke = if (strokesIterator.hasNext()) {
            strokesIterator.next()
        } else {
            hasLastStroke = false
            lastStroke!!
        }
    }
}

interface Stroke {
    val color: Color
    val width: Float
    val points: List<Point>
}

class MutableStroke(
    override val color: Color,
    override val width: Float,
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