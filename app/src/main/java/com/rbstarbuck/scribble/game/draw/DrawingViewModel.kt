package com.rbstarbuck.scribble.game.draw

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.rbstarbuck.scribble.game.brush.BrushType
import com.rbstarbuck.scribble.game.layer.Layers
import kotlinx.coroutines.flow.StateFlow

class DrawingViewModel(
    val layers: Layers,
    val backgroundStateFlow: StateFlow<Color>,
    val selectedBrushTypeStateFlow: StateFlow<BrushType>
): ViewModel()