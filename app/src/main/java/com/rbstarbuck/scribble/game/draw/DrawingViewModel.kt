package com.rbstarbuck.scribble.game.draw

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.rbstarbuck.scribble.game.Strokes
import com.rbstarbuck.scribble.game.layer.Layers
import kotlinx.coroutines.flow.StateFlow

class DrawingViewModel(
    val backgroundColor: StateFlow<Color>,
    val layers: Layers,
): ViewModel()