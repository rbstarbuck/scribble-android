package com.rbstarbuck.scribble.game.transform

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import com.rbstarbuck.scribble.game.draw.CanvasView
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ScaleView(
    viewModel: ScaleViewModel,
    modifier: Modifier = Modifier
) {
    Box(modifier, contentAlignment = Alignment.Center) {
        val selectedLayer by viewModel.selectedLayerStateFlow.collectAsState()

        val boundsStateFlow = remember { MutableStateFlow(Rect.Zero) }
        val bounds by boundsStateFlow.collectAsState()

        val scaleStateFlow = remember { MutableStateFlow(Offset(x = 0f, y =01f)) }
        val scale by scaleStateFlow.collectAsState()

        val pivotFractionStateFlow = remember { MutableStateFlow(Offset(0f, 0f)) }
        val pivotFraction by pivotFractionStateFlow.collectAsState()

        val layerWasVisible = remember { selectedLayer.visible }

        var originalBounds = Rect.Zero

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            selectedLayer.visible = false
                        },
                        onDrag = { change, offset ->
                            val dX = if (change.position.x >= originalBounds.center.x) {
                                offset.x
                            } else {
                                offset.x * -1
                            }

                            val dY = if (change.position.y >= originalBounds.center.y) {
                                offset.y
                            } else {
                                offset.y * -1
                            }

                            scaleStateFlow.value += Offset(dX, dY)
                        },
                        onDragEnd = {
                            selectedLayer.strokes.scale(
                                dX = ((originalBounds.width + 2 * scale.x) / originalBounds.width),
                                dY = ((originalBounds.height + 2 * scale.y) / originalBounds.height)
                            )

                            originalBounds = bounds
                            scaleStateFlow.value = Offset.Zero
                            selectedLayer.visible = layerWasVisible
                        }
                    )
                }
        ) {
            boundsStateFlow.value = drawTransformBox(
                strokes = selectedLayer.strokes,
                scale = scale
            )

            if (originalBounds == Rect.Zero) originalBounds = bounds

            pivotFractionStateFlow.value = Offset(
                x = bounds.center.x / size.width,
                y = bounds.center.y / size.height
            )
        }

        CanvasView(
            strokes = selectedLayer.strokes,
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .graphicsLayer {
                    transformOrigin = TransformOrigin(pivotFraction.x, pivotFraction.y)
                    scaleX = (originalBounds.width + 2 * scale.x) / originalBounds.width
                    scaleY = (originalBounds.height + 2 * scale.y) / originalBounds.height
                }
        )
    }
}