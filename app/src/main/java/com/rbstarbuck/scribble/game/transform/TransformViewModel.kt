package com.rbstarbuck.scribble.game.transform

import androidx.lifecycle.ViewModel
import com.rbstarbuck.scribble.game.layer.Layers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TransformViewModel(
    val selectedLayerStateFlow: StateFlow<Layers.Layer>,
    val boundingBoxRotationStateFlow: MutableStateFlow<Float>,
    val recomposeBoundingBoxStateFlow: MutableStateFlow<Int>
): ViewModel() {
    val selectedTransformTypeStateFlow = MutableStateFlow(TransformType.TRANSLATE)
}