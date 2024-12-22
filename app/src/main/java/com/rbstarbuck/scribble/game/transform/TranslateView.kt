package com.rbstarbuck.scribble.game.transform

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import com.rbstarbuck.scribble.game.draw.CanvasView
import com.rbstarbuck.scribble.util.pxToDp
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun TranslateView(
    viewModel: TranslateViewModel,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        val context = LocalContext.current

        val selectedLayer by viewModel.selectedLayerStateFlow.collectAsState()

        val dragOffsetStateFlow = remember { MutableStateFlow(Offset(x = 0f, y = 0f)) }
        val dragOffset by dragOffsetStateFlow.collectAsState()

        val layerWasVisible = remember { selectedLayer.visible }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .offset(
                    x = dragOffset.x.pxToDp(context),
                    y = dragOffset.y.pxToDp(context)
                ).pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            selectedLayer.visible = false
                        },
                        onDrag = { change, offset ->
                            dragOffsetStateFlow.value += offset
                        },
                        onDragEnd = {
                            selectedLayer.strokes.translate(
                                x = dragOffset.x / size.width,
                                y = dragOffset.y / size.height
                            )
                            dragOffsetStateFlow.value = Offset(x = 0f, y = 0f)
                            selectedLayer.visible = layerWasVisible
                        }
                    )
                }
        ) {
            drawTransformBox(selectedLayer.strokes, context)
        }

        CanvasView(
            strokes = selectedLayer.strokes,
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .offset(
                    x = dragOffset.x.pxToDp(context),
                    y = dragOffset.y.pxToDp(context)
                )
        )
    }
}