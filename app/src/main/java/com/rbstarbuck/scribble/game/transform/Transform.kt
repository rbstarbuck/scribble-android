package com.rbstarbuck.scribble.game.transform

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.rbstarbuck.scribble.game.brush.BrushType
import com.rbstarbuck.scribble.game.draw.Strokes

enum class TransformType {
    TRANSLATE,
    SCALE,
    ROTATE
}

fun DrawScope.drawTransformBox(
    strokes: Strokes,
    scale: Offset = Offset.Zero
): Rect {
    val bounds = strokesBoundingBox(strokes, scale)
    val boundsPadding = 16.dp.toPx()

    val topLeft = Offset(
        x = bounds.left - boundsPadding,
        y = bounds.top - boundsPadding,
    )

    val size = Size(
        width = (bounds.width + 2f * boundsPadding),
        height = (bounds.height + 2f * boundsPadding),
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

private fun DrawScope.strokesBoundingBox(
    strokes: Strokes,
    scale: Offset = Offset.Zero
): Rect {
    var left = Float.MAX_VALUE
    var right = Float.MIN_VALUE
    var top = Float.MAX_VALUE
    var bottom = Float.MIN_VALUE

    for (stroke in strokes.committedStrokes) {
        if (stroke.brushType != BrushType.Eraser) {
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
        left = left * size.width - scale.x,
        top = top * size.height - scale.y,
        right = right * size.width + scale.x,
        bottom = bottom * size.height + scale.y
    )
}
