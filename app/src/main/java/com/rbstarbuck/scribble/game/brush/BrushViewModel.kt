package com.rbstarbuck.scribble.game.brush

import androidx.lifecycle.ViewModel
import com.rbstarbuck.scribble.game.color.HSVColor
import com.rbstarbuck.scribble.game.layer.Layers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BrushViewModel(
    val colorStateFlow: StateFlow<HSVColor>,
    val selectedLayerStateFlow: StateFlow<Layers.Layer>,
    val strokeWidthStateFlow: MutableStateFlow<Float>,
    val brushTypeStateFlow: MutableStateFlow<BrushType>,
    val fillTypeStateFlow: MutableStateFlow<FillType>
): ViewModel()