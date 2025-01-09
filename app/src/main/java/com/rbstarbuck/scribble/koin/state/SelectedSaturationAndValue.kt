package com.rbstarbuck.scribble.koin.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface SelectedSaturationAndValue {
    val stateFlow: StateFlow<Pair<Float, Float>>
    var saturationAndValue: Pair<Float, Float>
}

class SelectedSaturationAndValueImpl: SelectedSaturationAndValue {
    private val _stateFlow = MutableStateFlow(Pair(0f, 0f))
    override val stateFlow = _stateFlow.asStateFlow()

    override var saturationAndValue: Pair<Float, Float>
        get() = stateFlow.value
        set(value) {
            _stateFlow.value = value
        }
}