package com.rbstarbuck.scribble.game.transform

import androidx.lifecycle.ViewModel
import com.rbstarbuck.scribble.game.layer.Layers
import kotlinx.coroutines.flow.StateFlow

class ScaleViewModel(
    val selectedLayerStateFlow: StateFlow<Layers.Layer>
): ViewModel()