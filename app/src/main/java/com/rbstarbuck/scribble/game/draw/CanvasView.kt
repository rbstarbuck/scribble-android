package com.rbstarbuck.scribble.game.draw

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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.dp
import com.rbstarbuck.scribble.game.brush.BrushType
import com.rbstarbuck.scribble.game.brush.FillType
import com.rbstarbuck.scribble.game.color.toColor
import com.rbstarbuck.scribble.util.SoftwareLayer
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sqrt

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
                            drawStrokeCircle(stroke)
                        } else if (stroke.brushType == BrushType.Rectangle) {
                            drawStrokeRectangle(stroke)
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
                            drawStrokeCircle(currentStroke)
                        } else if (currentStroke.brushType == BrushType.Rectangle) {
                            drawStrokeRectangle(currentStroke)
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

            if (stroke.brushType == BrushType.Polygon) {
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

private fun DrawScope.drawStrokeRectangle(stroke: Stroke) {
    if (stroke.points.size == 2) {
        val first = stroke.points.first()
        val second = stroke.points.last()

        val xFirst = first.x * size.width
        val xSecond = second.x * size.width
        val yFirst = first.y * size.height
        val ySecond = second.y * size.height

        val topLeft = when {
            xFirst < xSecond && yFirst > ySecond -> Offset(xFirst, ySecond)
            xFirst < xSecond && yFirst < ySecond -> Offset(xFirst, yFirst)
            xFirst > xSecond && yFirst > ySecond -> Offset(xSecond, ySecond)
            else -> Offset(xSecond, yFirst)
        }

        val rectSize = Size(
            width = (xFirst - xSecond).absoluteValue,
            height = (yFirst - ySecond).absoluteValue
        )

        drawRect(
            color = stroke.color.toColor(),
            topLeft = topLeft,
            size = rectSize,
            style = when (stroke.fillType) {
                FillType.Stroke -> androidx.compose.ui.graphics.drawscope.Stroke(
                    width = stroke.width * size.width
                )
                FillType.Filled -> Fill
            }
        )
    }
}

private fun DrawScope.drawStrokeCircle(stroke: Stroke) {
    if (stroke.points.size == 2) {
        drawCircle(
            color = stroke.color.toColor(),
            radius = distanceInCanvas(
                p1 = stroke.points.first(),
                p2 = stroke.points.last()
            ),
            center = Offset(
                x = stroke.points.first().x * size.width,
                y = stroke.points.first().y * size.height
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

private fun DrawScope.distanceInCanvas(
    p1: Point,
    p2: Point
) = sqrt(
    (p1.x * size.width - p2.x * size.width).pow(2) +
            (p1.y * size.height - p2.y * size.height).pow(2))