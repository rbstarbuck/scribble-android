package com.rbstarbuck.scribble.game.transform

import androidx.lifecycle.ViewModel
import com.rbstarbuck.scribble.game.layer.Layers
import com.rbstarbuck.scribble.koin.state.SelectedLayer
import kotlinx.coroutines.flow.StateFlow
import org.koin.java.KoinJavaComponent.inject

class RotateViewModel: ViewModel() {
    val selectedLayer: SelectedLayer by inject(SelectedLayer::class.java)
}