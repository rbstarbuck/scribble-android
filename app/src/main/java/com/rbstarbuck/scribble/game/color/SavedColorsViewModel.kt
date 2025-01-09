package com.rbstarbuck.scribble.game.color

import androidx.lifecycle.ViewModel
import com.rbstarbuck.scribble.koin.state.SelectedHue
import com.rbstarbuck.scribble.koin.state.SelectedSaturationAndValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.java.KoinJavaComponent.inject

class SavedColorsViewModel(): ViewModel() {
    val selectedHue: SelectedHue by inject(SelectedHue::class.java)
    val selectedSaturationAndValue:
            SelectedSaturationAndValue by inject(SelectedSaturationAndValue::class.java)

    private val _savedColorsStateFlow = MutableStateFlow(emptyList<SavedColor>())
    val savedColorsStateFlow = _savedColorsStateFlow.asStateFlow()

    fun addColor() {
        val savedColor = SavedColor(
            hue = selectedHue.hue,
            saturation = selectedSaturationAndValue.saturationAndValue.first,
            value = selectedSaturationAndValue.saturationAndValue.second
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