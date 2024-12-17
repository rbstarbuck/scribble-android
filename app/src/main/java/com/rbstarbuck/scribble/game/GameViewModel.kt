package com.rbstarbuck.scribble.game

import androidx.compose.ui.graphics.Color
import com.rbstarbuck.scribble.game.draw.DrawingViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class GameViewModel {
    val drawing = DrawingViewModel(
        backgroundColor = MutableStateFlow(Color.White),
        selectedColor = MutableStateFlow(Color.Black),
        selectedStrokeWidth = MutableStateFlow(10f)
    )
}