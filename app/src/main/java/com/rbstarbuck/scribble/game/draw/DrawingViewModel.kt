package com.rbstarbuck.scribble.game.draw

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

class DrawingViewModel(
    val backgroundColor: StateFlow<Color>,
    selectedColor: StateFlow<Color>,
    selectedStrokeWidth: StateFlow<Float>
): ViewModel() {
    val strokes = Strokes(selectedColor, selectedStrokeWidth)
    val canvas = CanvasViewModel(strokes)
    val paintbrush = PaintbrushViewModel(strokes)
}