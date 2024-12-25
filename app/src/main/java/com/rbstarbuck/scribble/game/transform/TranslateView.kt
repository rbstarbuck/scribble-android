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
import androidx.compose.ui.input.pointer.pointerInput
import com.rbstarbuck.scribble.game.draw.CanvasView
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun TranslateView(
    viewModel: TranslateViewModel,
    modifier: Modifier = Modifier
) {
    val selectedLayer by viewModel.selectedLayerStateFlow.collectAsState()

    val recomposeStateFlow = remember { MutableStateFlow(false) }
    val recompose by recomposeStateFlow.collectAsState()

    val layerWasVisible = remember { selectedLayer.visible }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        selectedLayer.visible = false
                    },
                    onDrag = { change, offset ->
                        selectedLayer.strokes.translate(
                            x = offset.x / size.width,
                            y = offset.y / size.height
                        )

                        recomposeStateFlow.value = !recomposeStateFlow.value
                    },
                    onDragEnd = {
                        selectedLayer.visible = layerWasVisible
                    }
                )
            }
    ) {
        key(recompose) {
            Canvas(Modifier.fillMaxSize()) {
                drawTransformBox(selectedLayer.strokes)
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