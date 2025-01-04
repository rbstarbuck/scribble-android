package com.rbstarbuck.scribble.game.transform

import androidx.lifecycle.ViewModel
import com.rbstarbuck.scribble.game.layer.Layers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TransformControlsViewModel(
    val selectedLayerStateFlow: StateFlow<Layers.Layer>
): ViewModel() {
    val selectedTransformTypeStateFlow = MutableStateFlow(TransformType.TRANSLATE)
    val recomposeBoundingBoxStateFlow = MutableStateFlow(0)
}