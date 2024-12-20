package com.rbstarbuck.scribble.game.draw

import android.view.View
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.rbstarbuck.scribble.game.brush.BrushType
import com.rbstarbuck.scribble.game.brush.FillType
import com.rbstarbuck.scribble.game.color.toColor
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CanvasBackgroundView(
    backgroundStateFlow: StateFlow<Color>,
    modifier: Modifier = Modifier
) {
    val background by backgroundStateFlow.collectAsState()

    Box(modifier.background(background))
}

@Composable
fun CanvasView(
    strokes: Strokes,
    modifier: Modifier = Modifier
) {
    val recomposeCommittedStrokes by strokes.recomposeCurrentStrokeStateFlow.collectAsState()

    key(recomposeCommittedStrokes) {
        SoftwareLayerComposable {
            Canvas(modifier.clipToBounds()) {
                for (stroke in strokes.committedStrokes) {
                    drawStroke(
                        stroke = stroke,
                        brushType = stroke.brushType,
                        fillType = stroke.fillType
                    )
                }

                val currentStroke = strokes.currentStroke
                if (currentStroke != null) {
                    drawStroke(
                        stroke = currentStroke,
                        brushType = currentStroke.brushType,
                        fillType = currentStroke.fillType
                    )

                    if (currentStroke.brushType == BrushType.Polygon) {
                        drawCircle(
                            color = Color.DarkGray,
                            radius = 24.dp.toPx(),
                            center = Offset(
                                currentStroke.points.first().x * size.width,
                                currentStroke.points.first().y * size.height
                            ),
                            style = androidx.compose.ui.graphics.drawscope.Stroke (
                                width = 1.5.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(
                                    intervals = floatArrayOf(5.dp.toPx(), 5.dp.toPx())
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawStroke(
    stroke: Stroke,
    brushType: BrushType,
    fillType: FillType
) {
    if (stroke.points.size == 1) {
        val point = stroke.points.first()

        drawCircle(
            color = stroke.color.toColor(),
            radius = stroke.width * size.width / 2f,
            center = Offset(
                x = point.x * size.width,
                y = point.y * size.height
            )
        )
    } else {
        if (stroke.points.isNotEmpty()) {
            val path = Path()
            val initialPoint = stroke.points.first()

            path.moveTo(
                x = initialPoint.x * size.width,
                y = initialPoint.y * size.height
            )

            for (point in stroke.points) {
                path.lineTo(
                    x = point.x * size.width,
                    y = point.y * size.height
                )
            }

            if (brushType == BrushType.Polygon) {
                path.close()
            }


            drawPath(
                path = path,
                color = if (stroke.brushType == BrushType.Eraser) {
                    Color.Transparent
                } else {
                    stroke.color.toColor()
                },
                style = if (fillType == FillType.Stroke || stroke.points.size < 3) {
                    androidx.compose.ui.graphics.drawscope.Stroke(
                        width = stroke.width * size.width,
                        cap = StrokeCap.Round
                    )
                } else {
                    Fill
                },
                blendMode = if (stroke.brushType == BrushType.Eraser) {
                    BlendMode.Clear
                } else {
                    BlendMode.SrcOver
                }
            )
        }
    }
}

@Composable
fun SoftwareLayerComposable(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    AndroidView(
        factory = { context ->
            ComposeView(context).apply {
                setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            }
        },
        update = { composeView ->
            composeView.setContent(content)
        },
        modifier = modifier,
    )
}