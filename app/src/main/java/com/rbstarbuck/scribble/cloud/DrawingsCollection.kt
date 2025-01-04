package com.rbstarbuck.scribble.cloud

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class DrawingCollection {
    private val db = Firebase.firestore

    fun write(day: String, drawing: DrawingDocument, onSuccess: () -> Unit = {}) {
        db.collection("Drawings")
            .document(day)
            .set(drawing)
            .addOnSuccessListener {
                onSuccess()
            }
    }

    fun read(day: String, onSuccess: (DrawingDocument) -> Unit) {
        db.collection("Drawings")
            .document(day)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val document = documentSnapshot.toObject(DrawingDocument::class.java)!!
                onSuccess(document)
            }
    }
}

data class DrawingDocument(
    val url: String,
    val user: String,
    val rank: Double = 1500.00
)