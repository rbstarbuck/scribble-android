package com.rbstarbuck.scribble.game.transform

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
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
    val recomposeBoundingBox by viewModel.recomposeBoundingBoxStateFlow.collectAsState()
    val boundingBoxRotation by viewModel.boundingBoxRotationStateFlow.collectAsState()

    val boundsStateFlow = remember { MutableStateFlow(Rect.Zero) }
    val bounds by boundsStateFlow.collectAsState()

    key(recomposeBoundingBox) {
        Canvas(
            modifier = modifier
                .graphicsLayer {
                    transformOrigin = TransformOrigin(
                        pivotFractionX = bounds.center.x / size.width,
                        pivotFractionY = bounds.center.y / size.height
                    )
                    rotationZ = boundingBoxRotation
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, offset ->
                            val degrees = (offset.x / size.width + offset.y / size.height) * 180f

                            viewModel.boundingBoxRotationStateFlow.value += degrees
                            selectedLayer.strokes.rotateZ(
                                degrees = degrees,
                                strokesCenter = Offset(
                                    x = bounds.center.x / size.width,
                                    y = bounds.center.y / size.height
                                )
                            )
                        },
                        onDragEnd = {
                            viewModel.boundingBoxRotationStateFlow.value = 0f
                            viewModel.recomposeBoundingBoxStateFlow.value += 1
                        }
                    )
                }
        ) {
            boundsStateFlow.value = drawTransformBox(selectedLayer.strokes)
        }
    }
}