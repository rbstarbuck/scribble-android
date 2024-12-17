package com.rbstarbuck.scribble.game.draw

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CanvasViewModel(
    val backgroundColor: StateFlow<Color>,
    val strokes: Strokes,
    val recomposeStateFlow: StateFlow<Boolean> = MutableStateFlow(false)
): ViewModel() {

}