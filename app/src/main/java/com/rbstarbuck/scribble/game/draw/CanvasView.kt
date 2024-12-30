package com.rbstarbuck.scribble.game.draw

import android.graphics.Matrix
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.rbstarbuck.scribble.game.brush.BrushType
import com.rbstarbuck.scribble.game.brush.FillType
import com.rbstarbuck.scribble.game.color.toColor
import com.rbstarbuck.scribble.util.SoftwareLayer
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
    val recomposeCurrentStrokes by strokes.recomposeCurrentStrokesStateFlow.collectAsState()
    val recomposeCommittedStrokes by strokes.recomposeCommittedStrokesStateFlow.collectAsState()

    Box(modifier) {
        SoftwareLayer {
            key(recomposeCommittedStrokes) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .clipToBounds()
                ) {
                    for (stroke in strokes.committedStrokes) {
                        if (stroke.brushType == BrushType.Circle) {
                            drawStrokeCircle(stroke, strokes.rotationStateFlow.value)
                        } else {
                            drawStroke(stroke)
                        }
                    }
                }
            }

            key(recomposeCurrentStrokes) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .clipToBounds()
                ) {
                    val currentStroke = strokes.currentStroke
                    if (currentStroke != null) {
                        if (currentStroke.brushType == BrushType.Circle) {
                            drawStrokeCircle(currentStroke, strokes.rotationStateFlow.value)
                        } else {
                            drawStroke(currentStroke)
                        }

                        if (currentStroke.brushType == BrushType.Polygon) {
                            drawCloseShapeCircle(currentStroke.points.first())
                        } else if (currentStroke.brushType == BrushType.Line) {
                            drawCloseShapeCircle(currentStroke.points.last())
                        }
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawStroke(stroke: Stroke) {
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

            if (stroke.brushType == BrushType.Polygon || stroke.brushType == BrushType.Rectangle) {
                path.close()
            }

            drawPath(
                path = path,
                color = if (stroke.brushType == BrushType.Eraser) {
                    Color.Transparent
                } else {
                    stroke.color.toColor()
                },
                style = if (stroke.fillType == FillType.Stroke || stroke.points.size < 3) {
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

private fun DrawScope.drawStrokeCircle(stroke: Stroke, rotation: Float = 0f) {
    val points = stroke.points
    val bounds = boundingBox(stroke)

    val srcArray = FloatArray(8)
    val dstArray = FloatArray(8)

    srcArray[0] = points[0].x
    srcArray[1] = points[0].y
    srcArray[2] = points[1].x
    srcArray[3] = points[1].y
    srcArray[4] = points[2].x
    srcArray[5] = points[2].y
    srcArray[6] = points[3].x
    srcArray[7] = points[3].y

    val matrix = Matrix()
    matrix.postRotate(-rotation, bounds.center.x, bounds.center.y)
    matrix.mapPoints(dstArray, srcArray)

    rotate(rotation, Offset(bounds.center.x * size.width, bounds.center.y * size.height)) {
        drawOval(
            color = stroke.color.toColor(),
            topLeft = Offset(
                x = dstArray[0] * size.width,
                y = dstArray[1] * size.height
            ),
            size = Size(
                width = (dstArray[2] - dstArray[0]) * size.width,
                height = (dstArray[7] - dstArray[1]) * size.height
            ),
            style = when (stroke.fillType) {
                FillType.Stroke -> androidx.compose.ui.graphics.drawscope.Stroke(
                    width = stroke.width * size.width
                )

                FillType.Filled -> Fill
            }
        )
    }
}

private fun DrawScope.drawCloseShapeCircle(point: Point) {
    drawCircle(
        color = Color.DarkGray,
        radius = 24.dp.toPx(),
        center = Offset(
            point.x * size.width,
            point.y * size.height
        ),
        style = androidx.compose.ui.graphics.drawscope.Stroke (
            width = 1.5.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(5.dp.toPx(), 5.dp.toPx())
            )
        )
    )
}