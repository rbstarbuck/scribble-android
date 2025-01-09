package com.rbstarbuck.scribble.operation

import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.auth
import com.rbstarbuck.scribble.game.draw.DrawingViewModel
import com.rbstarbuck.scribble.koin.state.SelectedBackgroundColor
import org.koin.java.KoinJavaComponent.inject

class AddDrawingOperation(private val drawing: DrawingViewModel): Operation() {
    val auth = Firebase.auth

    override fun execute() {
        val uid = auth.currentUser?.uid

        if (uid != null) {
            val selectedBackgroundColor:
                    SelectedBackgroundColor by inject(SelectedBackgroundColor::class.java)
            
            val map = mapOf(
                "day" to Timestamp.now(),
                "uid" to uid,
                "background" to selectedBackgroundColor.color.value,
                "layers" to Array(drawing.layers.layersStateFlow.value.size) {
                    drawing.layers.layersStateFlow.value[it].key
                }
            )

            val task = firestore
                .collection(DRAWING_COLLECTION)
                .document(drawing.key)
                .set(map)

            finalize(task)
        }
    }
}