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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.pointerInput
import com.rbstarbuck.scribble.game.draw.CanvasView
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.java.KoinJavaComponent.inject

@Composable
fun ScaleView(modifier: Modifier = Modifier) {
    val viewModel: ScaleViewModel by inject(ScaleViewModel::class.java)

    val selectedLayer by viewModel.selectedLayer.stateFlow.collectAsState()

    val boundsStateFlow = remember { MutableStateFlow(Rect.Zero) }
    val bounds by boundsStateFlow.collectAsState()

    Box(modifier = modifier
        .pointerInput(Unit) {
            detectDragGestures(
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