package com.rbstarbuck.scribble.game

import androidx.lifecycle.ViewModel
import com.rbstarbuck.scribble.game.color.ColorPickerViewModel
import com.rbstarbuck.scribble.game.draw.DrawingViewModel
import com.rbstarbuck.scribble.game.layer.Layers
import com.rbstarbuck.scribble.game.layer.LayersViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class GameViewModel: ViewModel() {
    val selectedStrokeWidth = MutableStateFlow(0.005f)

    val colorPickerViewModel = ColorPickerViewModel()

    val layers = Layers(colorPickerViewModel.colorStateFlow, selectedStrokeWidth)

    val drawingViewModel = DrawingViewModel(layers, colorPickerViewModel.backgroundStateFlow)
    val layersViewModel = LayersViewModel(layers, colorPickerViewModel.backgroundStateFlow)
}