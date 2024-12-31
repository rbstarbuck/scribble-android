package com.rbstarbuck.scribble.game.transform

import android.graphics.Matrix
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
    scale: Offset = Offset.Zero,
    rotation: Float = 0f
): Rect {
    val bounds = strokesBoundingBox(strokes, scale, rotation)
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
    scale: Offset,
    rotation: Float
): Rect {
    var left = Float.MAX_VALUE
    var right = Float.MIN_VALUE
    var top = Float.MAX_VALUE
    var bottom = Float.MIN_VALUE

    var totalPoints = 0

    for (stroke in strokes.committedStrokes) {
        if (stroke.brushType != BrushType.Eraser) {
            for (point in stroke.points) {
                val horizontal = point.x
                val vertical = point.y

                if (horizontal < left) left = horizontal
                if (horizontal > right) right = horizontal
                if (vertical < top) top = vertical
                if (vertical > bottom) bottom = vertical

                totalPoints += 2
            }
        }
    }

    val center = Rect(left, top, right, bottom).center

    val dstArray = FloatArray(totalPoints)
    val srcArray = FloatArray(totalPoints)

    var i = 0
    for (stroke in strokes.committedStrokes) {
        if (stroke.brushType != BrushType.Eraser) {
            for (point in stroke.points) {
                srcArray[i++] = point.x
                srcArray[i++] = point.y
            }
        }
    }

    val matrix = Matrix()
    matrix.postRotate(-rotation, center.x, center.y)
    matrix.mapPoints(dstArray, srcArray)

    left = Float.MAX_VALUE
    right = Float.MIN_VALUE
    top = Float.MAX_VALUE
    bottom = Float.MIN_VALUE

    for (i in 0..<totalPoints / 2) {
        val xIndex = i * 2
        val yIndex = xIndex + 1

        if (dstArray[xIndex] < left) left = dstArray[xIndex]
        if (dstArray[xIndex] > right) right = dstArray[xIndex]
        if (dstArray[yIndex] < top) top = dstArray[yIndex]
        if (dstArray[yIndex] > bottom) bottom = dstArray[yIndex]
    }

    return Rect(
        topLeft = Offset(
            x = left * size.width - scale.x,
            y = top * size.height - scale.y
        ),
        bottomRight = Offset(
            x = right * size.width + scale.x,
            y = bottom * size.height + scale.y
        )
    )
}
