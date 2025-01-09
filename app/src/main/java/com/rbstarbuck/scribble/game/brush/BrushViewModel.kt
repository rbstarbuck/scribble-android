package com.rbstarbuck.scribble.game.brush

import androidx.lifecycle.ViewModel
import com.rbstarbuck.scribble.koin.state.SelectedBrushType
import com.rbstarbuck.scribble.koin.state.SelectedColor
import com.rbstarbuck.scribble.koin.state.SelectedFillType
import com.rbstarbuck.scribble.koin.state.SelectedLayer
import org.koin.java.KoinJavaComponent.inject
import kotlin.getValue

class BrushViewModel: ViewModel() {
    val selectedLayer: SelectedLayer by inject(SelectedLayer::class.java)
    val selectedColor: SelectedColor by inject(SelectedColor::class.java)
    val selectedBrushType: SelectedBrushType by inject(SelectedBrushType::class.java)
    val selectedFillType: SelectedFillType by inject(SelectedFillType::class.java)
}