package com.rbstarbuck.scribble.game.transform

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
fun RotateView(
    viewModel: RotateViewModel,
    modifier: Modifier = Modifier
) {
    val selectedLayer by viewModel.selectedLayer.stateFlow.collectAsState()

    val localRotationStateFlow = remember { MutableStateFlow(0f) }
    val localRotation by localRotationStateFlow.collectAsState()

    val boundsStateFlow = remember { MutableStateFlow(Rect.Zero) }
    val bounds by boundsStateFlow.collectAsState()

    Box(
        modifier = modifier
            .graphicsLayer {
                transformOrigin = TransformOrigin(
                    pivotFractionX = bounds.center.x / size.width,
                    pivotFractionY = bounds.center.y / size.height
                )
                rotationZ = localRotation
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, offset ->
                        val degrees = (offset.x / size.width + offset.y / size.height) * 180f

                        localRotationStateFlow.value += degrees
                        selectedLayer.strokes.rotateZ(
                            degrees = degrees,
                            strokesCenter = Offset(
                                x = bounds.center.x / size.width,
                                y = bounds.center.y / size.height
                            )
                        )
                    },
                    onDragEnd = {
                        localRotationStateFlow.value = 0f
                    }
                )
            }
    )

    Canvas(modifier) {
        boundsStateFlow.value = drawTransformBox(selectedLayer.strokes)
    }

    CanvasView(
        strokes = selectedLayer.strokes,
        modifier = modifier.clipToBounds()
    )
}