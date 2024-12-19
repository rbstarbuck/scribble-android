package com.rbstarbuck.scribble.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.rbstarbuck.scribble.game.draw.DrawingView
import com.rbstarbuck.scribble.game.layer.LayersView
import com.rbstarbuck.scribble.R
import com.rbstarbuck.scribble.game.color.ColorPickerView

@Composable
fun GameView(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        DrawingView(
            viewModel = viewModel.drawingViewModel,
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .aspectRatio(1f)
                .border(
                    width = 2.dp,
                    color = colorResource(R.color.canvas_border)
                )
        )

//        LayersView(
//            viewModel = viewModel.layersViewModel,
//            modifier = Modifier
//                .padding(horizontal = 20.dp)
//                .fillMaxWidth()
//        )

        ColorPickerView(
            viewModel = viewModel.colorPickerViewModel,
            modifier = Modifier
                .height(200.dp)
                .padding(horizontal = 20.dp)
        )
    }
}