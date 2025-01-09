package com.rbstarbuck.scribble.koin.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface SelectedColorHue {
    val stateFlow: StateFlow<Float>
    var hue: Float
}

class SelectedColorHueImpl: SelectedColorHue {
    private val _stateFlow = MutableStateFlow(180f)
    override val stateFlow = _stateFlow.asStateFlow()

    override var hue: Float
        get() = stateFlow.value
        set(value) {
            _stateFlow.value = value
        }
}