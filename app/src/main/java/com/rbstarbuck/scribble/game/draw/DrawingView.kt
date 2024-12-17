package com.rbstarbuck.scribble.game.draw

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DrawingView(
    viewModel: DrawingViewModel,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        CanvasBackgroundView(
            backgroundStateFlow = viewModel.backgroundColor,
            modifier = Modifier.fillMaxSize()
        )

        CommittedStrokesCanvasView(
            viewModel = viewModel.canvas,
            modifier = Modifier.fillMaxSize()
        )

        CurrentStrokeCanvasView(
            viewModel = viewModel.canvas,
            modifier = Modifier.fillMaxSize()
        )

        PaintbrushView(
            viewModel = viewModel.paintbrush,
            modifier = Modifier.fillMaxSize()
        )
    }
}