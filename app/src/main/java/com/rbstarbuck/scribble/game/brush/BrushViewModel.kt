package com.rbstarbuck.scribble.game.brush

import androidx.lifecycle.ViewModel
import com.rbstarbuck.scribble.game.color.HSVColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BrushViewModel(val colorStateFlow: StateFlow<HSVColor>): ViewModel() {
    val lineThicknessStateFlow = MutableStateFlow(0.015f)
}