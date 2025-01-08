package com.rbstarbuck.scribble.game.draw

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.rbstarbuck.scribble.game.brush.BrushType
import com.rbstarbuck.scribble.game.layer.Layers
import com.rbstarbuck.scribble.game.transform.RotateViewModel
import com.rbstarbuck.scribble.game.transform.ScaleViewModel
import com.rbstarbuck.scribble.game.transform.TransformType
import com.rbstarbuck.scribble.game.transform.TranslateViewModel
import com.rbstarbuck.scribble.util.generateKey
import kotlinx.coroutines.flow.StateFlow

class DrawingViewModel(
    val layers: Layers,
    val backgroundStateFlow: StateFlow<Color>,
    val selectedBrushTypeStateFlow: StateFlow<BrushType>,
    val selectedTransformTypeStateFlow: StateFlow<TransformType>
): ViewModel() {
    val key: String = generateKey()

    val translateViewModel = TranslateViewModel(layers.selectedLayerStateFlow)
    val scaleViewModel = ScaleViewModel(layers.selectedLayerStateFlow)
    val rotateViewModel = RotateViewModel(selectedLayerStateFlow = layers.selectedLayerStateFlow)
}