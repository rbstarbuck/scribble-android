package com.rbstarbuck.scribble.game

import androidx.compose.ui.graphics.Color
import com.rbstarbuck.scribble.game.draw.DrawingViewModel
import com.rbstarbuck.scribble.game.layer.Layers
import kotlinx.coroutines.flow.MutableStateFlow

class GameViewModel {
    val backgroundColor = MutableStateFlow(Color.White)
    val selectedStrokeColor = MutableStateFlow(Color.Black)
    val selectedStrokeWidth = MutableStateFlow(5f)

    val layers = Layers(selectedStrokeColor, selectedStrokeWidth)
    val drawing = DrawingViewModel(backgroundColor, layers)
}