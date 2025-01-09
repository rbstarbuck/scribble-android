package com.rbstarbuck.scribble.game.color

import android.graphics.Color.HSVToColor
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rbstarbuck.scribble.koin.state.SelectedBackgroundColor
import com.rbstarbuck.scribble.koin.state.SelectedColor
import com.rbstarbuck.scribble.koin.state.SelectedHue
import com.rbstarbuck.scribble.koin.state.SelectedSaturationAndValue
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class ColorPickerViewModel: ViewModel() {
    val selectedColor: SelectedColor by inject(SelectedColor::class.java)
    val selectedBackgroundColor:
            SelectedBackgroundColor by inject(SelectedBackgroundColor::class.java)
    val selectedHue: SelectedHue by inject(SelectedHue::class.java)
    val selectedSaturationAndValue:
            SelectedSaturationAndValue by inject(SelectedSaturationAndValue::class.java)

    init {
        viewModelScope.launch {
            selectedHue.stateFlow.collect { hue ->
                selectedColor.color = selectedColor.color.copy(hue = hue)
            }
        }

        viewModelScope.launch {
            selectedSaturationAndValue.stateFlow.collect { saturationAndValue ->
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