package com.rbstarbuck.scribble.game.draw

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.StateFlow

class Strokes(
    private val selectedColor: StateFlow<Color>,
    private val selectedWidth: StateFlow<Float>
): Iterable<Stroke> {
    private var currentStroke: MutableStroke? = null
    private val strokes = mutableListOf<Stroke>()

    fun beginStroke(x: Float, y: Float) {
        currentStroke = MutableStroke(
            color = selectedColor.value,
            width = selectedWidth.value,
            initialPoint = Point(x, y)
        )
    }

    fun appendStroke(x: Float, y: Float) {
        currentStroke!!.addPoint(Point(x, y))
    }

    fun endStroke() {
        strokes.add(currentStroke!!)
        currentStroke = null
    }

    override fun iterator(): Iterator<Stroke> = StrokesIterator()

    private inner class StrokesIterator: Iterator<Stroke> {
        private val strokesIterator: Iterator<Stroke> = strokes.iterator()
        private val lastStroke: MutableStroke? = currentStroke

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