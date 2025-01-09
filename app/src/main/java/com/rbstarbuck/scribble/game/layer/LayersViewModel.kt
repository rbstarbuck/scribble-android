package com.rbstarbuck.scribble.game.layer

import androidx.lifecycle.ViewModel
import com.rbstarbuck.scribble.koin.state.SelectedBackgroundColor
import org.koin.java.KoinJavaComponent.inject

class LayersViewModel: ViewModel() {
    val layers: Layers by inject(Layers::class.java)
    val selectedBackgroundColor:
            SelectedBackgroundColor by inject(SelectedBackgroundColor::class.java)
}