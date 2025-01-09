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
import org.koin.java.KoinJavaComponent.inject

@Composable
fun DrawingView(modifier: Modifier = Modifier) {
    val viewModel: DrawingViewModel by inject(DrawingViewModel::class.java)

    Box(modifier) {
        CanvasBackgroundView(Modifier.fillMaxSize())

        val layers by viewModel.layers.layersStateFlow.collectAsState()
        val selectedBrushType by viewModel.selectedBrushType.stateFlow.collectAsState()
        val selectedTransformType by viewModel.selectedTransformType.stateFlow.collectAsState()
        val tabItemRecompose by TabItem.recomposeFlag.collectAsState()

        for (layer in layers.reversed()) {
            val visible by layer.visibleStateFlow.collectAsState()
            val selected by layer.selectedStateFlow.collectAsState()

            if (visible) {
                if (selected && TabItem.selectedItem == TabItem.TransformTabItem) {
                    when (selectedTransformType) {
                        TransformType.TRANSLATE -> TranslateView(Modifier.fillMaxSize())
                        TransformType.SCALE -> ScaleView(Modifier.fillMaxSize())
                        TransformType.ROTATE -> RotateView(Modifier.fillMaxSize())
                    }
                } else {
                    CanvasView(
                        strokes = layer.strokes,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        key(tabItemRecompose) {
            if (TabItem.selectedItem == TabItem.BrushTabItem) {
                when (selectedBrushType) {
                    BrushType.Pencil, BrushType.Eraser -> PencilPaintbrushView(
                        modifier = Modifier.fillMaxSize()
                    )

                    BrushType.Line -> LineAndPolygonPaintbrushView(
                        isPolygon = false,
                        modifier = Modifier.fillMaxSize()
                    )

                    BrushType.Polygon -> LineAndPolygonPaintbrushView(
                        isPolygon = true,
                        modifier = Modifier.fillMaxSize()
                    )

                    BrushType.Circle -> CirclePaintbrushView(
                        modifier = Modifier.fillMaxSize()
                    )

                    BrushType.Rectangle -> RectanglePaintbrushView(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}