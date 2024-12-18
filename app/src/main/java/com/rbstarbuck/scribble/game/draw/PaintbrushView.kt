package com.rbstarbuck.scribble.game.draw

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.rbstarbuck.scribble.game.layer.Layers.Layer
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PaintbrushView(
    selectedLayerStateFlow: StateFlow<Layer>,
    modifier: Modifier = Modifier
) {
    val selectedLayerState = selectedLayerStateFlow.collectAsState()
    val strokes = selectedLayerState.value.strokes

    val visibleState = selectedLayerState.value.visibleStateFlow.collectAsState()
    val visible = visibleState.value

    if (visible) {
        Box(
            modifier = modifier
                .pointerInput(strokes) {
                    detectTapGestures(
                        onTap = { offset ->
                            strokes.beginStroke(
                                x = offset.x / size.width,
                                y = offset.y / size.height
                            )
                            strokes.endStroke()
                        }
                    )
                }.pointerInput(strokes) {
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
                        },
                        onDragCancel = {
                            strokes.endStroke()
                        }
                    )
                }
        ) {}
    }
}