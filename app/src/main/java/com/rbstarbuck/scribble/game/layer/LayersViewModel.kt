package com.rbstarbuck.scribble.game.layer

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

class LayersViewModel(
    val layers: Layers,
    val backgroundStateFlow: StateFlow<Color>
): ViewModel()