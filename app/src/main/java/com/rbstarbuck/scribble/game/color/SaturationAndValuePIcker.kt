package com.rbstarbuck.scribble.game.color

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color.HSVToColor
import android.graphics.ComposeShader
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.RectF
import android.graphics.Shader
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SaturationAndValuePicker(
    saturationValueStateFlow: MutableStateFlow<Pair<Float, Float>>,
    hueStateFlow: StateFlow<Float>,
    modifier: Modifier = Modifier
) {
    val hue by hueStateFlow.collectAsState()

    val pointStateFlow = remember { MutableStateFlow(Offset(0f, 0f)) }
    val point by pointStateFlow.collectAsState()

    Canvas(
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color.Black
            ).pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        val saturation =
                            1f / size.width * offset.x.coerceIn(0.0000001f, size.width.toFloat())
                        val value = 1f -
                                1f / size.height *
                                offset.y.coerceIn(0.0000001f, size.height.toFloat())

                        pointStateFlow.value = offset
                        saturationValueStateFlow.value = saturation to value
                    }
                )
            }.pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, _ ->
                        val x = change.position.x.coerceIn(0f, size.width.toFloat())
                        val y = change.position.y.coerceIn(0f, size.height.toFloat())
                        val saturation = x / size.width
                        val value = 1f - y / size.height

                        pointStateFlow.value = Offset(x, y)
                        saturationValueStateFlow.value = saturation to value
                    }
                )
            }
    ) {
        pointStateFlow.value = Offset(
            x = saturationValueStateFlow.value.first * size.width,
            y = (1f - saturationValueStateFlow.value.second) * size.height
        )

        val rgb = HSVToColor(floatArrayOf(hue, 1f, 1f))

        val bitmap = Bitmap.createBitmap(
            /* width = */ size.width.toInt(),
            /* height = */ size.height.toInt(),
            /* config = */ Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)

        val panel = RectF(
            /* left = */ 0f,
            /* top = */ 0f,
            /* right = */ bitmap.width.toFloat(),
            /* bottom = */ bitmap.height.toFloat()
        )

        val saturationShader = LinearGradient(
            /* x0 = */ panel.left,
            /* y0 = */ panel.top,
            /* x1 = */ panel.right,
            /* y1 = */ panel.top,
            /* color0 = */ -0x1,
            /* color1 = */ rgb,
            /* tile = */ Shader.TileMode.CLAMP
        )

        val valueShader = LinearGradient(
            /* x0 = */ panel.left,
            /* y0 = */ panel.top,
            /* x1 = */ panel.left,
            /* y1 = */ panel.bottom,
            /* color0 = */ -0x1,
            /* color1 = */ -0x1000000,
            /* tile = */ Shader.TileMode.CLAMP
        )

        canvas.drawRect(
            /* rect = */ panel,
            /* paint = */ Paint().apply {
                shader = ComposeShader(
                    /* shaderA = */ valueShader,
                    /* shaderB = */ saturationShader,
                    /* mode = */ PorterDuff.Mode.MULTIPLY
                )
            }
        )

        drawIntoCanvas { canvas ->
            canvas.nativeCanvas.drawBitmap(
                /* bitmap = */ bitmap,
                /* src = */ null,
                /* dst = */ panel,
                /* paint = */ null
            )
        }

        val radius = size.width / 20f
        val strokeWidth = size.width / 160f
        val fillColor = Color(
            HSVToColor(
                floatArrayOf(
                    hue,
                    saturationValueStateFlow.value.first,
                    saturationValueStateFlow.value.second
                )
            )
        )

        drawCircle(
            color = Color.White,
            radius = radius + strokeWidth,
            center = point,
            style = Stroke(width = strokeWidth)
        )

        drawCircle(
            color = Color.Black,
            radius = radius,
            center = point,
            style = Stroke(width = strokeWidth)
        )

        drawCircle(
            color = fillColor,
            radius = radius,
            center = point
        )

    }
}

@Preview
@Composable
fun SaturationAndValuePickerPreview() {
    val saturationValueStateFlow = remember { MutableStateFlow(0f to 0f) }
    val saturationValue by saturationValueStateFlow.collectAsState()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        SaturationAndValuePicker(
            saturationValueStateFlow = saturationValueStateFlow,
            hueStateFlow = MutableStateFlow(0f),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )

        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    Color(
                        HSVToColor(floatArrayOf(0f, saturationValue.first, saturationValue.second))
                    )
                )
        )
    }
}