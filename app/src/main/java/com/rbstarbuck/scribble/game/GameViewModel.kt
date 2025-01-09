package com.rbstarbuck.scribble.game

import androidx.lifecycle.ViewModel
import com.rbstarbuck.scribble.game.color.ColorPickerViewModel
import com.rbstarbuck.scribble.game.draw.DrawingViewModel
import com.rbstarbuck.scribble.game.layer.LayersViewModel
import com.rbstarbuck.scribble.game.brush.BrushViewModel
import com.rbstarbuck.scribble.game.prompt.GamePromptViewModel
import com.rbstarbuck.scribble.game.transform.TransformControlsViewModel

class GameViewModel: ViewModel() {
    val colorPickerViewModel: ColorPickerViewModel = ColorPickerViewModel()
    val layersViewModel: LayersViewModel = LayersViewModel()
    val brushViewModel: BrushViewModel = BrushViewModel()
    val drawingViewModel: DrawingViewModel = DrawingViewModel()
    val transformControlsViewModel: TransformControlsViewModel = TransformControlsViewModel()
    val gamePromptViewModel: GamePromptViewModel = GamePromptViewModel()
}