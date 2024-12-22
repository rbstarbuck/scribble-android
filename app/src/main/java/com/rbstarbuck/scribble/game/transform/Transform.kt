package com.rbstarbuck.scribble.game.transform

import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.rbstarbuck.scribble.game.brush.BrushType
import com.rbstarbuck.scribble.game.draw.Point
import com.rbstarbuck.scribble.game.draw.Strokes
import com.rbstarbuck.scribble.util.dpToPx
import kotlin.math.pow
import kotlin.math.sqrt

enum class TransformType {
    TRANSLATE,
    SCALE,
    ROTATE
}

fun DrawScope.drawTransformBox(strokes: Strokes, context: Context): Rect {
    val bounds = strokesBoundingBox(strokes)
    val boundsPadding = 16.dp.dpToPx(context)

    val topLeft = Offset(
        x = bounds.left - boundsPadding,
        y = bounds.top - boundsPadding
    )

    val size = Size(
        width = bounds.width + 2f * boundsPadding,
        height = bounds.height + 2f * boundsPadding
    )

    drawRect(
        color = Color.DarkGray,
        topLeft = topLeft,
        size = size,
        style = androidx.compose.ui.graphics.drawscope.Stroke(
            width = 5f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f))
        )

    )

    return bounds
}

private fun DrawScope.distance(
    p1: Point,
    p2: Point
) = sqrt((p1.x - p2.x).pow(2) + (p1.y - p2.y).pow(2))

private fun DrawScope.strokesBoundingBox(strokes: Strokes): Rect {
    var left = Float.MAX_VALUE
    var right = Float.MIN_VALUE
    var top = Float.MAX_VALUE
    var bottom = Float.MIN_VALUE

    for (stroke in strokes.committedStrokes) {
        if (stroke.brushType == BrushType.Circle) {
            val first = stroke.points.first()
            val second = stroke.points.last()

            val distance = distance(first, second)

            val leftDistance = first.x - distance
            val rightDistance = first.x + distance
            val topDistance = first.y - distance
            val bottomDistance = first.y + distance

            if (leftDistance < left) left = leftDistance
            if (rightDistance > right) right = rightDistance
            if (topDistance < top) top = topDistance
            if (bottomDistance > bottom) bottom = bottomDistance
        } else {
            for (point in stroke.points) {
                val horizontal = point.x
                val vertical = point.y

                if (horizontal < left) left = horizontal
                if (horizontal > right) right = horizontal
                if (vertical < top) top = vertical
                if (vertical > bottom) bottom = vertical
            }
        }
    }

    return Rect(
        left = left * size.width,
        top = top * size.height,
        right = right * size.width,
        bottom = bottom * size.height
    )
}
