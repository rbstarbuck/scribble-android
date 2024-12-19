package com.rbstarbuck.scribble.game.color

import android.graphics.Color.HSVToColor
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun HuePicker(
    hueStateFlow: MutableStateFlow<Float>,
    modifier: Modifier = Modifier
) {
    val pointStateFlow = remember { MutableStateFlow(0f) }
    val point by pointStateFlow.collectAsState()

    val colorStops = remember {
        arrayOf(
            0f to Color.Red,
            0.1667f to Color.Yellow,
            0.3333f to Color.Green,
            0.5f to Color.Cyan,
            0.6667f to Color.Blue,
            0.8333f to Color.Magenta,
            1.0f to Color.Red
        )
    }

    Canvas(
        modifier = modifier
            .background(Brush.verticalGradient(colorStops = colorStops))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        pointStateFlow.value = offset.y
                        hueStateFlow.value = offset.y / size.height.toFloat() * 360f
                    }
                )
            }.pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, _ ->
                        val y = change.position.y.coerceIn(0f, size.height.toFloat())
                        pointStateFlow.value = y
                        hueStateFlow.value = y / size.height.toFloat() * 360f
                    }
                )
            }
    ) {
        pointStateFlow.value = hueStateFlow.value / 360f * size.height.toFloat()

        val strokeWidth = size.width / 20f
        val center = size.width / 2f
        val radius = center - strokeWidth

        drawCircle(
            color = Color.Black,
            radius = radius,
            center = Offset(x = center, y = point),
            style = Stroke(width = strokeWidth)
        )
    }
}

@Preview
@Composable
fun HuePickerPreview() {
    val hueStateFlow = remember { MutableStateFlow(0f) }
    val hue by hueStateFlow.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        HuePicker(
            hueStateFlow = hueStateFlow,
            modifier = Modifier
                .width(50.dp)
                .height(400.dp)
        )

        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color(HSVToColor(floatArrayOf(hue, 1f, 1f))))
        )
    }
}