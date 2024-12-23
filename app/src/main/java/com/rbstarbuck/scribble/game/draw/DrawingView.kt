package com.rbstarbuck.scribble.game.draw

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.rbstarbuck.scribble.game.TabItem
import com.rbstarbuck.scribble.game.brush.BrushType
import com.rbstarbuck.scribble.game.transform.RotateView
import com.rbstarbuck.scribble.game.transform.ScaleView
import com.rbstarbuck.scribble.game.transform.TransformType
import com.rbstarbuck.scribble.game.transform.TranslateView

@Composable
fun DrawingView(
    viewModel: DrawingViewModel,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        CanvasBackgroundView(
            backgroundStateFlow = viewModel.backgroundStateFlow,
            modifier = Modifier.fillMaxSize()
        )

        val layers by viewModel.layers.layersStateFlow.collectAsState()
        val selectedBrush by viewModel.selectedBrushTypeStateFlow.collectAsState()
        val selectedTransformType by viewModel.selectedTransformTypeStateFlow.collectAsState()
        val tabItemRecompose by TabItem.recomposeFlag.collectAsState()

        for (layer in layers.reversed()) {
            val visible by layer.visibleStateFlow.collectAsState()

            if (visible) {
                CanvasView(
                    strokes = layer.strokes,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        key(tabItemRecompose) {
            if (TabItem.selectedItem == TabItem.BrushTabItem) {
                when (selectedBrush) {
                    BrushType.Pencil, BrushType.Eraser -> PencilPaintbrushView(
                        selectedLayerStateFlow = viewModel.layers.selectedLayerStateFlow,
                        modifier = Modifier.fillMaxSize()
                    )

                    BrushType.Line -> LineAndPolygonPaintbrushView(
                        selectedLayerStateFlow = viewModel.layers.selectedLayerStateFlow,
                        isPolygon = false,
                        modifier = Modifier.fillMaxSize()
                    )

                    BrushType.Polygon -> LineAndPolygonPaintbrushView(
                        selectedLayerStateFlow = viewModel.layers.selectedLayerStateFlow,
                        isPolygon = true,
                        modifier = Modifier.fillMaxSize()
                    )

                    BrushType.Rectangle, BrushType.Circle -> RectangleAndCirclePaintbrushView(
                        selectedLayerStateFlow = viewModel.layers.selectedLayerStateFlow,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else if (TabItem.selectedItem == TabItem.TransformTabItem) {
                when (selectedTransformType) {
                    TransformType.TRANSLATE -> TranslateView(
                        viewModel = viewModel.translateViewModel,
                        modifier = Modifier.fillMaxSize()
                    )

                    TransformType.SCALE -> ScaleView(
                        viewModel = viewModel.scaleViewModel,
                        modifier = Modifier.fillMaxSize()
                    )

                    TransformType.ROTATE -> RotateView(
                        viewModel = viewModel.rotateViewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}