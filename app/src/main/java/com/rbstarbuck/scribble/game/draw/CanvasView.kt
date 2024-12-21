package com.rbstarbuck.scribble.game.draw

import android.view.View
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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
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
import com.rbstarbuck.scribble.game.TabItem
import com.rbstarbuck.scribble.game.brush.BrushType
import com.rbstarbuck.scribble.game.brush.FillType
import com.rbstarbuck.scribble.game.color.toColor
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.pow
import kotlin.math.sqrt

private const val TRANSFORM_BOUNDS_PADDING = 25f

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
        SoftwareLayerComposable {
            key(recomposeCommittedStrokes) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .clipToBounds()
                ) {
                    for (stroke in strokes.committedStrokes) {
                        if (stroke.brushType == BrushType.Circle) {
                            drawStrokeCircle(stroke)
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
                        } else {
                            drawStroke(currentStroke)
                        }

                        if (currentStroke.brushType == BrushType.Polygon) {
                            drawCloseShapeCircle(currentStroke.points.first())
                        } else if (currentStroke.brushType == BrushType.Line) {
                            drawCloseShapeCircle(currentStroke.points.last())
                        }
                    }

                    if (
                        TabItem.TransformTabItem.isSelected &&
                        strokes.committedStrokes.isNotEmpty()
                    ) {
                        drawTransformBox(strokes)
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


private fun DrawScope.drawTransformBox(strokes: Strokes): Rect {
    val bounds = strokesBoundingBox(strokes)

    val topLeft = Offset(
        x = bounds.left - TRANSFORM_BOUNDS_PADDING,
        y = bounds.top - TRANSFORM_BOUNDS_PADDING
    )

    val size = Size(
        width = bounds.width + 2f * TRANSFORM_BOUNDS_PADDING,
        height = bounds.height + 2f * TRANSFORM_BOUNDS_PADDING
    )

    drawRect(
        color = Color.DarkGray,
        topLeft = topLeft,
        size = size,
        style = androidx.compose.ui.graphics.drawscope.Stroke(
            width = 10f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(30f, 30f))
        )

    )

    return bounds
}

private fun DrawScope.distanceInCanvas(
    p1: Point,
    p2: Point
) = sqrt(
    (p1.x * size.width - p2.x * size.width).pow(2) +
            (p1.y * size.height - p2.y * size.height).pow(2))

private fun DrawScope.distance(
    p1: Point,
    p2: Point
) = sqrt((p1.x - p2.x).pow(2) + (p1.y - p2.y).pow(2))

private fun DrawScope.strokesBoundingBox(strokes: Strokes): Rect {
    var left = Float.MAX_VALUE
    var right = Float.MIN_VALUE
    var top = Float.MAX_VALUE
    var bottom = Float.MIN_VALUE

    for (stroke in strokes.committedStrokes) {
        if (stroke.brushType == BrushType.Circle) {
            val first = stroke.points.first()
            val second = stroke.points.last()

            val distance = distance(first, second)

            val leftDistance = first.x - distance
            val rightDistance = first.x + distance
            val topDistance = first.y - distance
            val bottomDistance = first.y + distance

            if (leftDistance < left) left = leftDistance
            if (rightDistance > right) right = rightDistance
            if (topDistance < top) top = topDistance
            if (bottomDistance > bottom) bottom = bottomDistance
        } else {
            for (point in stroke.points) {
                val horizontal = point.x
                val vertical = point.y

                if (horizontal < left) left = horizontal
                if (horizontal > right) right = horizontal
                if (vertical < top) top = vertical
                if (vertical > bottom) bottom = vertical
            }
        }
    }

    return Rect(
        left = left * size.width,
        top = top * size.height,
        right = right * size.width,
        bottom = bottom * size.height
    )
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