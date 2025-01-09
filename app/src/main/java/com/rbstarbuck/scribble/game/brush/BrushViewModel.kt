package com.rbstarbuck.scribble.game.brush

import androidx.lifecycle.ViewModel
import com.rbstarbuck.scribble.game.color.HSVColor
import com.rbstarbuck.scribble.game.layer.Layers
import com.rbstarbuck.scribble.koin.state.SelectedStrokeWidth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.java.KoinJavaComponent.inject
import kotlin.getValue

class BrushViewModel(
    val colorStateFlow: StateFlow<HSVColor>,
    val selectedLayerStateFlow: StateFlow<Layers.Layer>,
    val brushTypeStateFlow: MutableStateFlow<BrushType>,
    val fillTypeStateFlow: MutableStateFlow<FillType>
): ViewModel() {
    val selectedStrokeWidth: SelectedStrokeWidth by inject(SelectedStrokeWidth::class.java)
}