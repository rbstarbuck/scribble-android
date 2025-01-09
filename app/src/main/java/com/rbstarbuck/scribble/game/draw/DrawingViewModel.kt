package com.rbstarbuck.scribble.game.draw

import androidx.lifecycle.ViewModel
import com.rbstarbuck.scribble.game.layer.Layers
import com.rbstarbuck.scribble.koin.state.SelectedBrushType
import com.rbstarbuck.scribble.koin.state.SelectedTransformType
import com.rbstarbuck.scribble.util.generateKey
import org.koin.java.KoinJavaComponent.inject

class DrawingViewModel: ViewModel() {
    val key: String = generateKey()

    val layers: Layers by inject(Layers::class.java)
    val selectedBrushType: SelectedBrushType by inject(SelectedBrushType::class.java)
    val selectedTransformType: SelectedTransformType by inject(SelectedTransformType::class.java)
}