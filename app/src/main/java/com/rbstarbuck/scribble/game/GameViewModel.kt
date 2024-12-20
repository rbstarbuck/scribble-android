package com.rbstarbuck.scribble.game

import androidx.lifecycle.ViewModel
import com.rbstarbuck.scribble.game.color.ColorPickerViewModel
import com.rbstarbuck.scribble.game.draw.DrawingViewModel
import com.rbstarbuck.scribble.game.layer.Layers
import com.rbstarbuck.scribble.game.layer.LayersViewModel
import com.rbstarbuck.scribble.game.brush.BrushViewModel

class GameViewModel: ViewModel() {
    val colorPickerViewModel = ColorPickerViewModel()

    val brushViewModel = BrushViewModel(colorStateFlow = colorPickerViewModel.colorStateFlow)

    val layers = Layers(
        selectedStrokeColor = colorPickerViewModel.colorStateFlow,
        selectedStrokeWidth = brushViewModel.lineThicknessStateFlow,
        selectedBrushType = brushViewModel.brushTypeStateFlow,
        selectedFillType = brushViewModel.fillTypeStateFlow
    )

    val drawingViewModel = DrawingViewModel(
        layers = layers,
        backgroundColor = colorPickerViewModel.backgroundStateFlow,
        selectedBrushType = brushViewModel.brushTypeStateFlow
    )
    val layersViewModel = LayersViewModel(
        layers = layers,
        backgroundStateFlow = colorPickerViewModel.backgroundStateFlow
    )
}