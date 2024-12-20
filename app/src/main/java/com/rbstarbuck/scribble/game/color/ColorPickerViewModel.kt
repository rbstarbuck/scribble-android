package com.rbstarbuck.scribble.game.color

import android.graphics.Color.HSVToColor
import android.graphics.Color.colorToHSV
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ColorPickerViewModel: ViewModel() {
    val hueStateFlow = MutableStateFlow(180f)
    val saturationValueStateFlow = MutableStateFlow(0f to 0f)
    val colorStateFlow = MutableStateFlow(Color.Black)
    val backgroundStateFlow = MutableStateFlow(Color.White)

    val savedColorsViewModel = SavedColorsViewModel(hueStateFlow, saturationValueStateFlow)

    init {
        viewModelScope.launch {
            hueStateFlow.collect { hue ->
                val hsv = FloatArray(3)
                colorToHSV(colorStateFlow.value.toArgb(), hsv)

                colorStateFlow.value = Color(HSVToColor(floatArrayOf(hue, hsv[1], hsv[2])))
            }
        }

        viewModelScope.launch {
            saturationValueStateFlow.collect { saturationAndValue ->
                val hsv = FloatArray(3)
                colorToHSV(colorStateFlow.value.toArgb(), hsv)

                colorStateFlow.value = Color(
                    HSVToColor(
                        floatArrayOf(hsv[0], saturationAndValue.first, saturationAndValue.second)
                    )
                )
            }
        }
    }
}