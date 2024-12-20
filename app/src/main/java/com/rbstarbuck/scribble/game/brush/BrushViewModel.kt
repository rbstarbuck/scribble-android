package com.rbstarbuck.scribble.game.brush

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BrushViewModel(val colorStateFlow: StateFlow<Color>): ViewModel() {
    val lineThicknessStateFlow = MutableStateFlow(0.015f)
}