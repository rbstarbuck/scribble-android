package com.rbstarbuck.scribble.game.color

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SavedColorsViewModel(
    val hueStateFlow: MutableStateFlow<Float>,
    val saturationValueStateFlow: MutableStateFlow<Pair<Float, Float>>
): ViewModel() {
    private val _savedColorsStateFlow = MutableStateFlow(emptyList<SavedColor>())
    val savedColorsStateFlow = _savedColorsStateFlow.asStateFlow()

    fun addColor() {
        val savedColor = SavedColor(
            hue = hueStateFlow.value,
            saturation = saturationValueStateFlow.value.first,
            value = saturationValueStateFlow.value.second
        )

        _savedColorsStateFlow.value =
            listOf(savedColor) + savedColorsStateFlow.value.filter { it != savedColor }
    }

    init {
        addColor()
    }
}

data class SavedColor(
    val hue: Float,
    val saturation: Float,
    val value: Float
)