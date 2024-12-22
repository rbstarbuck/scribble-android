package com.rbstarbuck.scribble.game.transform

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import com.rbstarbuck.scribble.game.draw.CanvasView
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun RotateView(
    viewModel: RotateViewModel,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        val context = LocalContext.current

        val selectedLayer by viewModel.selectedLayerStateFlow.collectAsState()

        val rotationStateFlow = remember { MutableStateFlow(0f) }
        val rotation by rotationStateFlow.collectAsState()

        val pivotFractionStateFlow = remember { MutableStateFlow(Offset(0f, 0f)) }
        val pivotFraction by pivotFractionStateFlow.collectAsState()

        val layerWasVisible = remember { selectedLayer.visible }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    transformOrigin = TransformOrigin(pivotFraction.x, pivotFraction.y)
                    rotationZ = rotation * 360f
                }
//                .rotate(rotation * 360f)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            selectedLayer.visible = false
                        },
                        onDrag = { change, offset ->
                            rotationStateFlow.value += offset.x / size.width
                            rotationStateFlow.value += offset.y / size.height
                        },
                        onDragEnd = {
                            selectedLayer.strokes.rotate(rotation * 360f)
                            rotationStateFlow.value = 0f
                            selectedLayer.visible = layerWasVisible
                        }
                    )
                }
        ) {
            val bounds = drawTransformBox(selectedLayer.strokes, context)
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
//                .rotate(rotation * 360f)
                .graphicsLayer {
                    transformOrigin = TransformOrigin(pivotFraction.x, pivotFraction.y)
                    rotationZ = rotation * 360f
                }
        )
    }
}
