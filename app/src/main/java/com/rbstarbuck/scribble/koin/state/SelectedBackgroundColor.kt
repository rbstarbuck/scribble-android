package com.rbstarbuck.scribble.koin.state

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface SelectedBackgroundColor {
    val stateFlow: StateFlow<Color>
    var color: Color
}

class SelectedBackgroundColorImpl: SelectedBackgroundColor {
    private val _stateFlow = MutableStateFlow(Color.White)
    override val stateFlow = _stateFlow.asStateFlow()

    override var color: Color
        get() = stateFlow.value
        set(value) {
            _stateFlow.value = value
        }
}