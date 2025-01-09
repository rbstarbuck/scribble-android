package com.rbstarbuck.scribble.koin.state

import com.rbstarbuck.scribble.game.color.HSVColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface SelectedColor {
    val stateFlow: StateFlow<HSVColor>
    var color: HSVColor
}

class SelectedColorImpl: SelectedColor {
    private val _stateFlow = MutableStateFlow(HSVColor(0f, 0f, 0f))
    override val stateFlow = _stateFlow.asStateFlow()

    override var color: HSVColor
        get() = stateFlow.value
        set(value) {
            _stateFlow.value = value
        }
}