package com.rbstarbuck.scribble.game.transform

import androidx.lifecycle.ViewModel
import com.rbstarbuck.scribble.koin.state.SelectedLayer
import org.koin.java.KoinJavaComponent.inject

class TranslateViewModel: ViewModel() {
    val selectedLayer: SelectedLayer by inject(SelectedLayer::class.java)
}