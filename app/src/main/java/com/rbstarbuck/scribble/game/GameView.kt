package com.rbstarbuck.scribble.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rbstarbuck.scribble.game.draw.DrawingView
import com.rbstarbuck.scribble.game.layer.LayersView

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
            viewModel = viewModel.drawingViewModel,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .aspectRatio(1f)
        )

        LayersView(
            viewModel = viewModel.layersViewModel,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
        )
    }
}