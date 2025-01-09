package com.rbstarbuck.scribble.koin.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface SelectedStrokeWidth {
    val stateFlow: StateFlow<Float>
    var width: Float
}

class SelectedStrokeWidthImpl: SelectedStrokeWidth {
    private val _stateFlow = MutableStateFlow(0.015f)
    override val stateFlow = _stateFlow.asStateFlow()

    override var width: Float
        get() = stateFlow.value
        set(value) {
            _stateFlow.update { value }
        }
}