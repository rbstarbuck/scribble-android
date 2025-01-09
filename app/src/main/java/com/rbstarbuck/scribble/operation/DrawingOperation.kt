package com.rbstarbuck.scribble.operation

import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.rbstarbuck.scribble.game.layer.Layers
import com.rbstarbuck.scribble.koin.state.SelectedBackgroundColor
import org.koin.java.KoinJavaComponent.inject

fun uploadDrawing(key: String) {
    DrawingOperation(key).upload()
}

private class DrawingOperation(private val key: String): Operation() {
    val auth = Firebase.auth

    override fun execute() {
        val uid = auth.currentUser?.uid

        if (uid != null) {
            val selectedBackgroundColor:
                    SelectedBackgroundColor by inject(SelectedBackgroundColor::class.java)
            val layers: Layers by inject(Layers::class.java)
            val layerList = layers.layersStateFlow.value

            val map = mapOf(
                "day" to Timestamp.now(),
                "uid" to uid,
                "background" to selectedBackgroundColor.color.value,
                "layers" to Array(layerList.size) { layerList[it].key }
            )

            val task = firestore
                .collection(DRAWING_COLLECTION)
                .document(key)
                .set(map)

            finalize(task)
        }
    }
}