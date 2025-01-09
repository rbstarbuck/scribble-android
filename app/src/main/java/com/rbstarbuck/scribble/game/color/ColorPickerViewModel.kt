package com.rbstarbuck.scribble.game.color

import android.graphics.Color.HSVToColor
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rbstarbuck.scribble.koin.state.SelectedBackgroundColor
import com.rbstarbuck.scribble.koin.state.SelectedColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class ColorPickerViewModel: ViewModel() {
    val selectedColor: SelectedColor by inject(SelectedColor::class.java)
    val selectedBackgroundColor:
            SelectedBackgroundColor by inject(SelectedBackgroundColor::class.java)

    val hueStateFlow = MutableStateFlow(180f)
    val saturationValueStateFlow = MutableStateFlow(0f to 0f)

    val savedColorsViewModel = SavedColorsViewModel(hueStateFlow, saturationValueStateFlow)

    init {
        viewModelScope.launch {
            hueStateFlow.collect { hue ->
                selectedColor.color = selectedColor.color.copy(hue = hue)
            }
        }

        viewModelScope.launch {
            saturationValueStateFlow.collect { saturationAndValue ->
                selectedColor.color = selectedColor.color.copy(
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