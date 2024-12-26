package com.rbstarbuck.scribble.game.transform

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.pointerInput
import com.rbstarbuck.scribble.game.draw.CanvasView
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ScaleView(
    viewModel: ScaleViewModel,
    modifier: Modifier = Modifier
) {
    val selectedLayer by viewModel.selectedLayerStateFlow.collectAsState()
    val recompose by selectedLayer.strokes.recomposeCommittedStrokesStateFlow.collectAsState()

    val boundsStateFlow = remember { MutableStateFlow(Rect.Zero) }
    val bounds by boundsStateFlow.collectAsState()

    val layerWasVisible = remember { selectedLayer.visible }

    Box(modifier = modifier
        .pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { offset ->
                    selectedLayer.visible = false
                },
                onDrag = { change, offset ->
                    val width = bounds.width.coerceIn(0.0000001f, Float.MAX_VALUE)
                    val height = bounds.height.coerceIn(0.0000001f, Float.MAX_VALUE)

                    val dX = if (change.position.x < bounds.center.x) {
                        (width + 2f * -offset.x) / width
                    } else {
                        (width + 2f * offset.x) / width
                    }

                    val dY = if (change.position.y < bounds.center.y) {
                        (height + 2f * -offset.y) / height
                    } else {
                        (height + 2f * offset.y) / height
                    }

                    selectedLayer.strokes.scale(dX, dY)
                },
                onDragEnd = {
                    selectedLayer.visible = layerWasVisible
                }
            )
        }
    ) {
        key(recompose) {
            Canvas(Modifier.fillMaxSize()) {
                boundsStateFlow.value = drawTransformBox(selectedLayer.strokes)
            }
        }

        CanvasView(
            strokes = selectedLayer.strokes,
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
        )
    }
}