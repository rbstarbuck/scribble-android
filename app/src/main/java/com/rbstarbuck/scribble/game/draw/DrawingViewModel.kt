package com.rbstarbuck.scribble.game.draw

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.rbstarbuck.scribble.game.brush.BrushType
import com.rbstarbuck.scribble.game.layer.Layers
import com.rbstarbuck.scribble.game.transform.RotateViewModel
import com.rbstarbuck.scribble.game.transform.ScaleViewModel
import com.rbstarbuck.scribble.game.transform.TransformType
import com.rbstarbuck.scribble.game.transform.TranslateViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DrawingViewModel(
    val layers: Layers,
    val backgroundStateFlow: StateFlow<Color>,
    val selectedBrushTypeStateFlow: StateFlow<BrushType>,
    val selectedTransformTypeStateFlow: StateFlow<TransformType>,
    val boundingBoxRotationStateFlow: MutableStateFlow<Float>,
    val recomposeBoundingBoxStateFlow: MutableStateFlow<Int>
): ViewModel() {
    val translateViewModel = TranslateViewModel(layers.selectedLayerStateFlow)
    val scaleViewModel = ScaleViewModel(layers.selectedLayerStateFlow)
    val rotateViewModel = RotateViewModel(
        selectedLayerStateFlow = layers.selectedLayerStateFlow,
        boundingBoxRotationStateFlow = boundingBoxRotationStateFlow,
        recomposeBoundingBoxStateFlow = recomposeBoundingBoxStateFlow
    )
}