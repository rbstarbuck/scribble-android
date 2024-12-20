package com.rbstarbuck.scribble.game.color

import android.graphics.Color.HSVToColor
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ColorPickerViewModel: ViewModel() {
    val hueStateFlow = MutableStateFlow(180f)
    val saturationValueStateFlow = MutableStateFlow(0f to 0f)
    val colorStateFlow = MutableStateFlow(HSVColor(0f, 0f, 0f))
    val backgroundStateFlow = MutableStateFlow(Color.White)

    val savedColorsViewModel = SavedColorsViewModel(hueStateFlow, saturationValueStateFlow)

    init {
        viewModelScope.launch {
            hueStateFlow.collect { hue ->
                val color = colorStateFlow.value
                colorStateFlow.value = color.copy(hue = hue)
            }
        }

        viewModelScope.launch {
            saturationValueStateFlow.collect { saturationAndValue ->
                val color = colorStateFlow.value
                colorStateFlow.value = color.copy(
                    saturation = saturationAndValue.first,
                    value = saturationAndValue.second
                )
            }
        }
    }
}

data class HSVColor(
    val hue: Float,
    val saturation: Float,
    val value: Float
)

fun HSVColor.toColor() = Color(HSVToColor(floatArrayOf(hue, saturation, value)))