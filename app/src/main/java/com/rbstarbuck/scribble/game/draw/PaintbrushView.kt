package com.rbstarbuck.scribble.game.draw

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun PaintbrushView(
    viewModel: PaintbrushViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        viewModel.strokes.beginStroke(
                            x = offset.x / size.width,
                            y = offset.y / size.height
                        )
                        viewModel.strokes.endStroke()
                        viewModel.recompose()
                    }
                )
            }.pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        viewModel.strokes.beginStroke(
                            x = offset.x / size.width,
                            y = offset.y / size.height
                        )
                        viewModel.recompose()
                    },
                    onDrag = { change, _ ->
                        viewModel.strokes.appendStroke(
                            x = change.position.x / size.width,
                            y = change.position.y / size.height
                        )
                        viewModel.recompose()
                    },
                    onDragEnd = {
                        viewModel.strokes.endStroke()
                    }
                )
            }
    ) {}
}