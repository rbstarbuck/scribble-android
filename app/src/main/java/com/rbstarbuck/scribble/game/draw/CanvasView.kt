package com.rbstarbuck.scribble.game.draw

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
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
fun CommittedStrokesCanvasView(
    strokes: Strokes,
    modifier: Modifier = Modifier
) {
    val recomposeCommittedStrokes by strokes.recomposeCommittedStrokesStateFlow.collectAsState()

    key(recomposeCommittedStrokes) {
        Canvas(modifier.clipToBounds()) {
            for (stroke in strokes.committedStrokes) {
                drawStroke(stroke)
            }
        }
    }
}

@Composable
fun CurrentStrokeCanvasView(
    strokes: Strokes,
    modifier: Modifier = Modifier
) {
    val recomposeCurrentStroke by strokes.recomposeCurrentStrokeStateFlow.collectAsState()

    key(recomposeCurrentStroke) {
        Canvas(modifier.clipToBounds()) {
            val currentStroke = strokes.currentStroke
            if (currentStroke != null) {
                drawStroke(currentStroke)
            }
        }
    }
}

private fun DrawScope.drawStroke(stroke: Stroke) {
    if (stroke.points.size == 1) {
        val point = stroke.points.first()

        drawCircle(
            color = stroke.color,
            radius = stroke.width * size.width / 2f,
            center = Offset(
                x = point.x * size.width,
                y = point.y * size.height
            )
        )
    } else {
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

        drawPath(
            path = path,
            color = stroke.color,
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = stroke.width * size.width,
                cap = StrokeCap.Round
            )
        )
    }
}