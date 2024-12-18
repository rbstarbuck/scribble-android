package com.rbstarbuck.scribble.game.draw

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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

        val layersState = viewModel.layers.layersStateFlow.collectAsState()
        for (layer in layersState.value) {
            val visibleState = layer.visibleStateFlow.collectAsState()

            if (visibleState.value) {
                CommittedStrokesCanvasView(
                    strokes = layer.strokes,
                    modifier = Modifier.fillMaxSize()
                )

                val selectedState = layer.selectedStateFlow.collectAsState()

                if (selectedState.value) {
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