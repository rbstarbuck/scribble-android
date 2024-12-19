package com.rbstarbuck.scribble.game.color

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class ColorPickerViewModel: ViewModel() {
    val hueStateFlow = MutableStateFlow(180f)
    val saturationValueStateFlow = MutableStateFlow(0f to 0f)

    val savedColorsViewModel = SavedColorsViewModel(hueStateFlow, saturationValueStateFlow)
}