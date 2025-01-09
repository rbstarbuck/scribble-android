package com.rbstarbuck.scribble.game.transform

import androidx.lifecycle.ViewModel
import com.rbstarbuck.scribble.game.layer.Layers
import com.rbstarbuck.scribble.koin.state.SelectedLayer
import com.rbstarbuck.scribble.koin.state.SelectedTransformType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.java.KoinJavaComponent.inject

class TransformControlsViewModel: ViewModel() {
    val selectedLayer: SelectedLayer by inject(SelectedLayer::class.java)
    val selectedTransformType: SelectedTransformType by inject(SelectedTransformType::class.java)
    val recomposeBoundingBoxStateFlow = MutableStateFlow(0)
}