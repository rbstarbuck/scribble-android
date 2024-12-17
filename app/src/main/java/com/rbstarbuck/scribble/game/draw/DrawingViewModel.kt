package com.rbstarbuck.scribble.game.draw

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DrawingViewModel(
    backgroundColor: StateFlow<Color>,
    selectedColor: StateFlow<Color>,
    selectedStrokeWidth: StateFlow<Float>
): ViewModel() {
    private val recomposeStateFlow = MutableStateFlow(false)

    val strokes = Strokes(selectedColor, selectedStrokeWidth)
    val canvas = CanvasViewModel(backgroundColor, strokes, recomposeStateFlow)
    val paintbrush = PaintbrushViewModel(strokes, recomposeStateFlow)
}