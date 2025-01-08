package com.rbstarbuck.scribble.game.draw

import com.google.firebase.Timestamp

data class Drawing(
    val day: Timestamp,
    val uid: String,
    val layers: Array<String>
)