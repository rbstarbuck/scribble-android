package com.rbstarbuck.scribble.game

import androidx.lifecycle.ViewModel
import com.rbstarbuck.scribble.game.color.ColorPickerViewModel
import com.rbstarbuck.scribble.game.draw.DrawingViewModel
import com.rbstarbuck.scribble.game.layer.Layers
import com.rbstarbuck.scribble.game.layer.LayersViewModel
import com.rbstarbuck.scribble.game.brush.BrushViewModel

class GameViewModel: ViewModel() {
    val colorPickerViewModel = ColorPickerViewModel()
    val brushViewModel = BrushViewModel(colorPickerViewModel.colorStateFlow)

    val layers = Layers(colorPickerViewModel.colorStateFlow, brushViewModel.lineThicknessStateFlow)

    val drawingViewModel = DrawingViewModel(layers, colorPickerViewModel.backgroundStateFlow)
    val layersViewModel = LayersViewModel(layers, colorPickerViewModel.backgroundStateFlow)
}