package com.rbstarbuck.scribble.operation

import com.rbstarbuck.scribble.game.layer.Layers.Layer

fun uploadLayer(layer: Layer) {
    LayerOperation(layer).upload()
}

private class LayerOperation(private val layer: Layer): Operation() {

    override fun execute() {
        val map = mapOf(
            "visible" to layer.visible,
            "translation" to packFloats(
                layer.strokes.translationStateFlow.value.x,
                layer.strokes.translationStateFlow.value.y,
            ),
            "scale" to packFloats(
                layer.strokes.scaleStateFlow.value.x,
                layer.strokes.scaleStateFlow.value.y
            ),
            "rotation" to layer.strokes.rotationStateFlow.value,
            "strokes" to Array(layer.strokes.committedStrokes.size) {
                layer.strokes.committedStrokes[it].key
            }
        )

        val task = firestore
            .collection(LAYER_COLLECTION)
            .document(layer.key)
            .set(map)

        finalize(task)
    }

}