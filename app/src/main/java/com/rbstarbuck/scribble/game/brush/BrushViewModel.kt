package com.rbstarbuck.scribble.game.brush

import androidx.lifecycle.ViewModel
import com.rbstarbuck.scribble.game.color.HSVColor
import com.rbstarbuck.scribble.game.layer.Layers
import com.rbstarbuck.scribble.koin.state.SelectedBrushType
import com.rbstarbuck.scribble.koin.state.SelectedColor
import com.rbstarbuck.scribble.koin.state.SelectedFillType
import com.rbstarbuck.scribble.koin.state.SelectedLayer
import com.rbstarbuck.scribble.koin.state.SelectedStrokeWidth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.java.KoinJavaComponent.inject
import kotlin.getValue

class BrushViewModel: ViewModel() {
    val selectedLayer: SelectedLayer by inject(SelectedLayer::class.java)
    val selectedColor: SelectedColor by inject(SelectedColor::class.java)
    val selectedBrushType: SelectedBrushType by inject(SelectedBrushType::class.java)
    val selectedFillType: SelectedFillType by inject(SelectedFillType::class.java)
}