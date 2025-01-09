package com.rbstarbuck.scribble.game.draw

import android.content.Context
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.rbstarbuck.scribble.util.dpToPx
import com.rbstarbuck.scribble.koin.state.SelectedLayer
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.java.KoinJavaComponent.inject
import kotlin.math.pow
import kotlin.math.sqrt

private val selectedLayer: SelectedLayer by inject(SelectedLayer::class.java)

@Composable
fun PencilPaintbrushView(
    modifier: Modifier = Modifier
) {
    val selectedLayer by selectedLayer.stateFlow.collectAsState()
    val visible by selectedLayer.visibleStateFlow.collectAsState()

    if (visible) {
        Box(
            modifier = modifier
                .pointerInput(selectedLayer.strokes) {
                    detectTapGestures(
                        onTap = { offset ->
                            selectedLayer.strokes.beginStroke(
                                x = offset.x / size.width,
                                y = offset.y / size.height
                            )
                            selectedLayer.strokes.endStroke()
                        }
                    )
                }.pointerInput(selectedLayer.strokes) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            selectedLayer.strokes.beginStroke(
                                x = offset.x / size.width,
                                y = offset.y / size.height
                            )
                        },
                        onDrag = { change, _ ->
                            selectedLayer.strokes.appendStroke(
                                x = change.position.x / size.width,
                                y = change.position.y / size.height
                            )
                        },
                        onDragEnd = {
                            selectedLayer.strokes.endStroke()
                        },
                        onDragCancel = {
                            selectedLayer.strokes.endStroke()
                        }
                    )
                }
        ) {}
    }
}

@Composable
fun LineAndPolygonPaintbrushView(
    isPolygon: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val selectedLayer by selectedLayer.stateFlow.collectAsState()
    val visible by selectedLayer.visibleStateFlow.collectAsState()

    var lastDragPoint = Offset.Zero

    if (visible) {
        Box(
            modifier = modifier
                .pointerInput(selectedLayer.strokes) {
                    detectTapGestures(
                        onTap = { offset ->
                            if (selectedLayer.strokes.currentStroke == null) {
                                selectedLayer.strokes.beginStroke(
                                    x = offset.x / size.width,
                                    y = offset.y / size.height
                                )
                            } else {
                                val point = if (isPolygon) {
                                    selectedLayer.strokes.firstPoint()
                                } else {
                                    selectedLayer.strokes.lastPoint()
                                }

                                if (
                                    pointsAreCloseToEachOther(
                                        offset.x,
                                        offset.y,
                                        point.x * size.width,
                                        point.y * size.height,
                                        context
                                    )
                                ) {
                                    selectedLayer.strokes.endStroke()
                                } else {
                                    selectedLayer.strokes.appendStroke(
                                        x = offset.x / size.width,
                                        y = offset.y / size.height
                                    )
                                }
                            }
                        }
                    )
                }.pointerInput(selectedLayer.strokes) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            if (selectedLayer.strokes.currentStroke == null) {
                                selectedLayer.strokes.beginStroke(
                                    x = offset.x / size.width,
                                    y = offset.y / size.height
                                )
                            } else {
                                selectedLayer.strokes.appendStroke(
                                    x = offset.x / size.width,
                                    y = offset.y / size.height
                                )
                            }
                        },
                        onDrag = { change, _ ->
                            lastDragPoint = change.position
                            selectedLayer.strokes.moveCurrentStrokePoint(
                                x = change.position.x / size.width,
                                y = change.position.y / size.height
                            )
                        }
                    )
                }
        ) {}
    }
}

@Composable
fun RectanglePaintbrushView(
    modifier: Modifier = Modifier
) {
    val selectedLayer by selectedLayer.stateFlow.collectAsState()
    val visible by selectedLayer.visibleStateFlow.collectAsState()

    if (visible) {
        Box(
            modifier = modifier
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            val x = offset.x / size.width
                            val y = offset.y / size.width

                            selectedLayer.strokes.beginStroke(x, y)
                            selectedLayer.strokes.appendStroke(x, y)
                            selectedLayer.strokes.appendStroke(x, y)
                            selectedLayer.strokes.appendStroke(x, y)
                        },
                        onDrag = { change, dragAmount ->
                            val x = change.position.x / size.width
                            val y = change.position.y / size.height

                            selectedLayer.strokes.moveRectangleStrokePoints(x, y)
                        },
                        onDragEnd = {
                            selectedLayer.strokes.endStroke()
                        }
                    )
                }
        )
    }
}

@Composable
fun CirclePaintbrushView(
    modifier: Modifier = Modifier
) {
    val selectedLayer by selectedLayer.stateFlow.collectAsState()
    val visible by selectedLayer.visibleStateFlow.collectAsState()

    val centerStateFlow = remember { MutableStateFlow(Offset.Zero) }

    if (visible) {
        Box(
            modifier = modifier
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            val x = offset.x / size.width
                            val y = offset.y / size.height

                            selectedLayer.strokes.beginStroke(x, y)
                            for (i in 1..<60) {
                                selectedLayer.strokes.appendStroke(x, y)
                            }

                            centerStateFlow.value = Offset(x, y)
                        },
                        onDrag = { change, dragAmount ->
                            val x = (change.position.x - dragAmount.x) / size.width
                            val y = (change.position.y - dragAmount.y) / size.height

                            selectedLayer.strokes.moveCircleStrokePoints(
                                x,
                                y,
                                centerStateFlow.value
                            )
                        },
                        onDragEnd = {
                            selectedLayer.strokes.endStroke()
                        }
                    )
                }
        )
    }
}

private fun pointsAreCloseToEachOther(
    x1: Float,
    y1: Float,
    x2: Float,
    y2: Float,
    context: Context
) = sqrt((x1 - x2).pow(2) + (y1 - y2).pow(2)) <= 24.dp.dpToPx(context)