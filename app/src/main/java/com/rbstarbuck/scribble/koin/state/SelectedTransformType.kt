package com.rbstarbuck.scribble.koin.state

import com.rbstarbuck.scribble.game.transform.TransformType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface SelectedTransformType {
    val stateFlow: StateFlow<TransformType>
    var transformType: TransformType
}

class SelectedTransformTypeImpl: SelectedTransformType {
    private val _stateFlow = MutableStateFlow(TransformType.TRANSLATE)
    override val stateFlow = _stateFlow.asStateFlow()

    override var transformType: TransformType
        get() = stateFlow.value
        set(value) {
            _stateFlow.update { value }
        }
}