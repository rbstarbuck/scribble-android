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
            strokes = viewModel.strokes,
            modifier = Modifier.fillMaxSize()
        )

        CurrentStrokeCanvasView(
            strokes = viewModel.strokes,
            modifier = Modifier.fillMaxSize()
        )

        PaintbrushView(
            strokes = viewModel.strokes,
            modifier = Modifier.fillMaxSize()
        )
    }
}