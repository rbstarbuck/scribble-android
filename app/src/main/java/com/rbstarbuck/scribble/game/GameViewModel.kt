package com.rbstarbuck.scribble.game

import androidx.lifecycle.ViewModel
import com.rbstarbuck.scribble.game.brush.BrushType
import com.rbstarbuck.scribble.game.color.ColorPickerViewModel
import com.rbstarbuck.scribble.game.draw.DrawingViewModel
import com.rbstarbuck.scribble.game.layer.Layers
import com.rbstarbuck.scribble.game.layer.LayersViewModel
import com.rbstarbuck.scribble.game.brush.BrushViewModel
import com.rbstarbuck.scribble.game.brush.FillType
import com.rbstarbuck.scribble.game.prompt.GamePromptViewModel
import com.rbstarbuck.scribble.game.transform.TransformViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class GameViewModel: ViewModel() {
    val layers: Layers

    val colorPickerViewModel: ColorPickerViewModel
    val layersViewModel: LayersViewModel
    val brushViewModel: BrushViewModel
    val drawingViewModel: DrawingViewModel
    val transformViewModel: TransformViewModel
    val gamePromptViewModel: GamePromptViewModel

    init {
        val strokeWidthStateFlow = MutableStateFlow(0.015f)
        val brushTypeStateFlow = MutableStateFlow(BrushType.Pencil)
        val fillTypeStateFlow = MutableStateFlow(FillType.Stroke)

        gamePromptViewModel = GamePromptViewModel()

        colorPickerViewModel = ColorPickerViewModel()

        layers = Layers(
            selectedColorStateFlow = colorPickerViewModel.colorStateFlow,
            selectedStrokeWidthStateFlow = strokeWidthStateFlow,
            selectedBrushTypeStateFlow = brushTypeStateFlow,
            selectedFillTypeStateFlow = fillTypeStateFlow
        )

        layersViewModel = LayersViewModel(
            layers = layers,
            backgroundStateFlow = colorPickerViewModel.backgroundStateFlow
        )

        brushViewModel = BrushViewModel(
            colorStateFlow = colorPickerViewModel.colorStateFlow,
            selectedLayerStateFlow = layers.selectedLayerStateFlow,
            strokeWidthStateFlow = strokeWidthStateFlow,
            brushTypeStateFlow = brushTypeStateFlow,
            fillTypeStateFlow = fillTypeStateFlow,
        )

        transformViewModel = TransformViewModel(
            selectedLayerStateFlow = layers.selectedLayerStateFlow
        )

        drawingViewModel = DrawingViewModel(
            layers = layers,
            backgroundStateFlow = colorPickerViewModel.backgroundStateFlow,
            selectedBrushTypeStateFlow = brushTypeStateFlow,
            selectedTransformTypeStateFlow = transformViewModel.selectedTransformTypeStateFlow,
        )
    }
}