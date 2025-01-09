package com.rbstarbuck.scribble.operation

import androidx.compose.ui.graphics.toArgb
import com.rbstarbuck.scribble.game.brush.FillType
import com.rbstarbuck.scribble.game.color.toColor
import com.rbstarbuck.scribble.game.draw.Stroke

fun uploadStroke(stroke: Stroke) {
    StrokeOperation(stroke).upload()
}

private class StrokeOperation(private val stroke: Stroke): Operation() {

    override fun execute() {
        val document = StrokeDocument(
            width = packFloats(stroke.width, stroke.unscaledWidth),
            brushTypeAndColor = packInts(stroke.brushType.ordinal, stroke.color.toColor().toArgb()),
            filled = stroke.fillType == FillType.Filled,
            points = Array(stroke.points.size) {
                packFloats(stroke.points[it].x, stroke.points[it].y)
            }
        )

        val task = firestore
            .collection(STROKE_COLLECTION)
            .document(stroke.key)
            .set(document)

        finalize(task)
    }
}

data class StrokeDocument(
    val width: Long,
    val brushTypeAndColor: Long,
    val filled: Boolean,
    val points: Array<Long>
)