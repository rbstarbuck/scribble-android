package com.rbstarbuck.scribble.game.transform

import com.rbstarbuck.scribble.game.layer.Layers
import kotlinx.coroutines.flow.StateFlow

class TranslateViewModel(val selectedLayer: StateFlow<Layers.Layer>)