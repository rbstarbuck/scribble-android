package com.rbstarbuck.scribble.game.draw

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.rbstarbuck.scribble.game.brush.BrushType

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
        val brush by viewModel.selectedBrushType.collectAsState()

        for (layer in layers.reversed()) {
            val visible by layer.visibleStateFlow.collectAsState()

            if (visible) {
                CanvasView(
                    strokes = layer.strokes,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        when (brush) {
            BrushType.Pencil, BrushType.Eraser -> PencilPaintbrushView(
                selectedLayerStateFlow = viewModel.layers.selectedLayerStateFlow,
                modifier = Modifier.fillMaxSize()
            )
            BrushType.Polygon -> PolygonPaintbrushView(
                selectedLayerStateFlow = viewModel.layers.selectedLayerStateFlow,
                modifier = Modifier.fillMaxSize()
            )
            BrushType.Circle -> CirclePaintbrushView(
                selectedLayerStateFlow = viewModel.layers.selectedLayerStateFlow,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}