package com.rbstarbuck.scribble.koin.state

import com.rbstarbuck.scribble.game.brush.BrushType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface SelectedBrushType {
    val stateFlow: StateFlow<BrushType>
    var brushType: BrushType
}

class SelectedBrushTypeImpl: SelectedBrushType {
    val _stateFlow = MutableStateFlow(BrushType.Pencil)
    override val stateFlow = _stateFlow.asStateFlow()

    override var brushType: BrushType
        get() = stateFlow.value
        set(value) {
            _stateFlow.update { value }
        }
}