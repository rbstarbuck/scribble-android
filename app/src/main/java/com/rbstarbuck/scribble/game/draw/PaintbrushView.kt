package com.rbstarbuck.scribble.game.draw

import android.content.Context
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.rbstarbuck.scribble.dpToPx
import com.rbstarbuck.scribble.game.layer.Layers.Layer
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun PencilPaintbrushView(
    selectedLayerStateFlow: StateFlow<Layer>,
    modifier: Modifier = Modifier
) {
    val selectedLayer by selectedLayerStateFlow.collectAsState()
    val strokes = selectedLayer.strokes

    val visible by selectedLayer.visibleStateFlow.collectAsState()

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

@Composable
fun PolygonPaintbrushView(
    selectedLayerStateFlow: StateFlow<Layer>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val selectedLayer by selectedLayerStateFlow.collectAsState()
    val strokes = selectedLayer.strokes

    val visible by selectedLayer.visibleStateFlow.collectAsState()

    var lastDragPoint = Offset(0f, 0f)

    if (visible) {
        Box(
            modifier = modifier
                .pointerInput(strokes) {
                    detectTapGestures(
                        onTap = { offset ->
                            if (strokes.currentStroke == null) {
                                strokes.beginStroke(
                                    x = offset.x / size.width,
                                    y = offset.y / size.height
                                )
                            } else {
                                if (
                                    pointsAreCloseToEachOther(
                                        offset.x,
                                        offset.y,
                                        strokes.firstPoint().x * size.width,
                                        strokes.firstPoint().y * size.height,
                                        context
                                    )
                                ) {
                                    strokes.endStroke()
                                } else {
                                    strokes.appendStroke(
                                        x = offset.x / size.width,
                                        y = offset.y / size.height
                                    )
                                }
                            }
                        }
                    )
                }.pointerInput(strokes) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            if (strokes.currentStroke == null) {
                                strokes.beginStroke(
                                    x = offset.x / size.width,
                                    y = offset.y / size.height
                                )
                            } else {
                                strokes.appendStroke(
                                    x = offset.x / size.width,
                                    y = offset.y / size.height
                                )
                            }
                        },
                        onDrag = { change, _ ->
                            lastDragPoint = change.position
                            strokes.moveCurrentStrokePoint(
                                x = change.position.x / size.width,
                                y = change.position.y / size.height
                            )
                        }
                    )
                }
        ) {}
    }
}

private fun pointsAreCloseToEachOther(
    x1: Float,
    y1: Float,
    x2: Float,
    y2: Float,
    context: Context
) = sqrt((x1 - x2).pow(2) + (y1 - y2).pow(2)) <= 24.dp.dpToPx(context)