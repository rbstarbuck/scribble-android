package com.rbstarbuck.scribble.koin.state

import com.rbstarbuck.scribble.game.brush.FillType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface SelectedFillType {
    val stateFlow: StateFlow<FillType>
    var fillType: FillType
}

class SelectedFillTypeImpl: SelectedFillType {
    private val _stateFlow = MutableStateFlow(FillType.Stroke)
    override val stateFlow = _stateFlow.asStateFlow()

    override var fillType: FillType
        get() = stateFlow.value
        set(value) {
            _stateFlow.update { value }
        }
}