package com.rbstarbuck.scribble.operation

import androidx.compose.ui.graphics.Color
import com.rbstarbuck.scribble.game.brush.FillType
import com.rbstarbuck.scribble.game.draw.Stroke

class AddStrokeOperation(private val stroke: Stroke): Operation() {

    override fun execute() {
        val map = mapOf(
            "color" to Color.hsv(
                stroke.color.hue,
                stroke.color.saturation,
                stroke.color.value
            ).value,
            "width" to packFloats(stroke.width, stroke.unscaledWidth),
            "brushType" to stroke.brushType.ordinal,
            "isFilled" to (stroke.fillType == FillType.Filled),
            "points" to Array(stroke.points.size) {
                packFloats(stroke.points[it].x, stroke.points[it].y)
            }
        )

        val task = firestore
            .collection(STROKE_COLLECTION)
            .document(stroke.key)
            .set(map)

        finalize(task)
    }
}