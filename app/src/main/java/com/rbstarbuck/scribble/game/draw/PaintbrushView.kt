package com.rbstarbuck.scribble.game.draw

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun PaintbrushView(
    strokes: Strokes,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { offset ->
                        strokes.beginStroke(
                            x = offset.x / size.width,
                            y = offset.y / size.height
                        )
                        strokes.endStroke()
                    }
                )
            }.pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        strokes.beginStroke(
                            x = offset.x / size.width,
                            y = offset.y / size.height
                        )
                    },
                    onDrag = { change, _ ->
                        strokes.appendStroke(
                            x = change.position.x / size.width,
                            y = change.position.y / size.height
                        )
                    },
                    onDragEnd = {
                        strokes.endStroke()
                    }
                )
            }
    ) {}
}