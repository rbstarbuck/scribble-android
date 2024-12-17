package com.rbstarbuck.scribble.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.rbstarbuck.scribble.game.draw.DrawingView

@Composable
fun GameView(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.background(Color.LightGray),
        verticalArrangement = Arrangement.Center
    ) {
        DrawingView(
            viewModel = viewModel.drawing,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
    }
}