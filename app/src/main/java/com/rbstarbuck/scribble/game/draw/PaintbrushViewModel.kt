package com.rbstarbuck.scribble.game.draw

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class PaintbrushViewModel(
    val strokes: Strokes,
    private val recomposeStateFlow: MutableStateFlow<Boolean>
): ViewModel() {
    fun recompose() {
        recomposeStateFlow.value = !recomposeStateFlow.value
    }
}