package com.rbstarbuck.scribble.koin.state

import com.rbstarbuck.scribble.game.layer.Layers
import com.rbstarbuck.scribble.game.layer.Layers.Layer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.java.KoinJavaComponent.inject

interface SelectedLayer {
    val stateFlow: StateFlow<Layer>
    var layer: Layer
}

class SelectedLayerImpl: SelectedLayer {
    private val layers: Layers by inject(Layers::class.java)

    private val _stateFlow = MutableStateFlow(layers.layersStateFlow.value.first())
    override val stateFlow = _stateFlow.asStateFlow()

    override var layer: Layer
        get() = stateFlow.value
        set(value) {
            _stateFlow.update { value }
        }
}