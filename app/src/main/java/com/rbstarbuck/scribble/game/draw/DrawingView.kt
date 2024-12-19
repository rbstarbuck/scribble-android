package com.rbstarbuck.scribble.game.draw

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

        val layers by viewModel.layers.layersStateFlow.collectAsState()

        for (layer in layers.reversed()) {
            val visible by layer.visibleStateFlow.collectAsState()

            if (visible) {
                val selected by layer.selectedStateFlow.collectAsState()

                CommittedStrokesCanvasView(
                    strokes = layer.strokes,
                    modifier = Modifier.fillMaxSize()
                )

                if (selected) {
                    CurrentStrokeCanvasView(
                        strokes = layer.strokes,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        PaintbrushView(
            selectedLayerStateFlow = viewModel.layers.selectedLayerStateFlow,
            modifier = Modifier.fillMaxSize()
        )
    }
}