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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun RotateView(
    viewModel: RotateViewModel,
    modifier: Modifier = Modifier
) {
    val selectedLayer by viewModel.selectedLayerStateFlow.collectAsState()
    val strokesRotation by selectedLayer.strokes.rotationStateFlow.collectAsState()

    val localRotationStateFlow = remember { MutableStateFlow(0f) }
    val localRotation by localRotationStateFlow.collectAsState()

    val boundsStateFlow = remember { MutableStateFlow(Rect.Zero) }
    val bounds by boundsStateFlow.collectAsState()

    val recomposeBoundingBoxStateFlow = remember { MutableStateFlow(0) }
    val recomposeBoundingBox by recomposeBoundingBoxStateFlow.collectAsState()

    key(recomposeBoundingBox) {
        Box(
            modifier = modifier
                .graphicsLayer {
                    transformOrigin = TransformOrigin(bounds.center.x, bounds.center.y)
                    rotationZ = localRotation
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            boundsStateFlow.value = selectedLayer.strokes.boundingBox()
                        },
                        onDrag = { change, offset ->
                            val degrees = (offset.x / size.width + offset.y / size.height) * 180f

                            localRotationStateFlow.value += degrees
                            selectedLayer.strokes.rotateZ(
                                degrees = degrees,
                                strokesCenter = bounds.center
                            )
                        },
                        onDragEnd = {
                            localRotationStateFlow.value = 0f
                            recomposeBoundingBoxStateFlow.value += 1
                        }
                    )
                }
        )
    }

    key(strokesRotation) {
        Canvas(
            modifier = modifier
                .fillMaxSize()
                .graphicsLayer {
                    boundsStateFlow.value = selectedLayer.strokes.boundingBox()

                    transformOrigin = TransformOrigin(bounds.center.x, bounds.center.y)
                    rotationZ = strokesRotation
                }
        ) {
            drawTransformBox(selectedLayer.strokes)
        }
    }
}