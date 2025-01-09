package com.rbstarbuck.scribble.game.color

import androidx.lifecycle.ViewModel
import com.rbstarbuck.scribble.koin.state.SelectedColorHue
import com.rbstarbuck.scribble.koin.state.SelectedColorSaturationAndValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.java.KoinJavaComponent.inject

class SavedColorsViewModel(): ViewModel() {
    val selectedColorHue: SelectedColorHue by inject(SelectedColorHue::class.java)
    val selectedColorSaturationAndValue:
            SelectedColorSaturationAndValue by inject(SelectedColorSaturationAndValue::class.java)

    private val _savedColorsStateFlow = MutableStateFlow(emptyList<SavedColor>())
    val savedColorsStateFlow = _savedColorsStateFlow.asStateFlow()

    fun addColor() {
        val savedColor = SavedColor(
            hue = selectedColorHue.hue,
            saturation = selectedColorSaturationAndValue.saturationAndValue.first,
            value = selectedColorSaturationAndValue.saturationAndValue.second
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