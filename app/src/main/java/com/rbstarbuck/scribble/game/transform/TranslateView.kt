package com.rbstarbuck.scribble.game.transform

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.pointer.pointerInput
import com.rbstarbuck.scribble.game.draw.CanvasView

@Composable
fun TranslateView(
    viewModel: TranslateViewModel,
    modifier: Modifier = Modifier
) {
    val selectedLayer by viewModel.selectedLayer.stateFlow.collectAsState()

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, offset ->
                        selectedLayer.strokes.translate(
                            x = offset.x / size.width,
                            y = offset.y / size.height
                        )
                    },
                )
            }
    )

    Canvas(modifier) {
        drawTransformBox(selectedLayer.strokes)
    }

    CanvasView(
        strokes = selectedLayer.strokes,
        modifier = modifier.clipToBounds()
    )
}