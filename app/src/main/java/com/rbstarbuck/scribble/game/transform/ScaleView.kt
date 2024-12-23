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
import com.rbstarbuck.scribble.util.Bitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.absoluteValue

@Composable
fun ScaleView(
    viewModel: ScaleViewModel,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        val context = LocalContext.current

        val selectedLayer by viewModel.selectedLayerStateFlow.collectAsState()

        val scaleStateFlow = remember { MutableStateFlow(Offset(x = 0.5f, y = 0.5f)) }
        val scale by scaleStateFlow.collectAsState()

        val pivotFractionStateFlow = remember { MutableStateFlow(Offset(0f, 0f)) }
        val pivotFraction by pivotFractionStateFlow.collectAsState()

        val layerWasVisible = remember { selectedLayer.visible }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            selectedLayer.visible = false
                        },
                        onDrag = { change, offset ->
                            scaleStateFlow.value += Offset(
                                x = (offset.x / size.width),
                                y = (offset.y / size.height)
                            )
                        },
                        onDragEnd = {
                            selectedLayer.strokes.scale(
                                dX = scale.x.absoluteValue * 2f,
                                dY = scale.y.absoluteValue * 2f
                            )
                            scaleStateFlow.value = Offset(x = 0.5f, y = 0.5f)
                            selectedLayer.visible = layerWasVisible
                        }
                    )
                }
        ) {
            val bounds = drawTransformBox(selectedLayer.strokes, context)

            pivotFractionStateFlow.value = Offset(
                x = bounds.center.x.absoluteValue / size.width,
                y = bounds.center.y.absoluteValue / size.height
            )
        }
    }
}