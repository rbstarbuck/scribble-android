package com.rbstarbuck.scribble.game.brush

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rbstarbuck.scribble.game.color.HSVColor
import com.rbstarbuck.scribble.game.color.toColor
import com.rbstarbuck.scribble.koin.state.SelectedStrokeWidth
import com.rbstarbuck.scribble.util.pxToDp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.java.KoinJavaComponent.inject
import kotlin.getValue

const val LINE_THICKNESS_MIN_VALUE = 0.005f
const val LINE_THICKNESS_MAX_VALUE = 0.1f

@Composable
fun StrokeWidthSlider(
    modifier: Modifier = Modifier,
    colorStateFlow: StateFlow<HSVColor>
) {
    val selectedStrokeWidth: SelectedStrokeWidth by inject(SelectedStrokeWidth::class.java)

    val color by colorStateFlow.collectAsState()

    val canvasWidthStateFlow = remember { MutableStateFlow(0f) }
    val canvasWidth by canvasWidthStateFlow.collectAsState()

    val lineThickness by selectedStrokeWidth.stateFlow.collectAsState()

    val initialPoint = (lineThickness - LINE_THICKNESS_MIN_VALUE) /
            (LINE_THICKNESS_MAX_VALUE - LINE_THICKNESS_MIN_VALUE)

    val pointStateFlow = remember { MutableStateFlow(initialPoint) }
    val point by pointStateFlow.collectAsState()

    fun onTouchInput(percentOfWidth: Float) {
        selectedStrokeWidth.width =
            (LINE_THICKNESS_MIN_VALUE +
                    (LINE_THICKNESS_MAX_VALUE - LINE_THICKNESS_MIN_VALUE) *
                    percentOfWidth)
                .coerceIn(LINE_THICKNESS_MIN_VALUE, LINE_THICKNESS_MAX_VALUE)

        pointStateFlow.value = percentOfWidth.coerceIn(0f, 1f)
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height((canvasWidth * LINE_THICKNESS_MAX_VALUE).pxToDp(LocalContext.current))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offset -> onTouchInput(offset.x / size.width) }
                )
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, _ -> onTouchInput(change.position.x / size.width) }
                )
            }
    ) {
        canvasWidthStateFlow.value = size.width

        drawLine(
            color = Color.Black,
            start = Offset(x = 0f, y = size.height / 2f),
            end = Offset(x = size.width, y = size.height / 2f),
            strokeWidth = size.height * point + 1.dp.toPx(),
            cap = StrokeCap.Round
        )

        drawLine(
            color = color.toColor(),
            start = Offset(x = 0f, y = size.height / 2f),
            end = Offset(x = size.width, y = size.height / 2f),
            strokeWidth = size.height * point,
            cap = StrokeCap.Round
        )

        drawCircle(
            color = Color.Black,
            radius = size.height / 2f + 1.dp.toPx(),
            center = Offset(x = size.width * point, y = size.height / 2f)
        )

        drawCircle(
            color = color.toColor(),
            center = Offset(x = size.width * point, y = size.height / 2f)
        )
    }
}

@Preview
@Composable
fun StrokeWidthSliderPreview() {
    Column {
        Spacer(Modifier.weight(1f))

        StrokeWidthSlider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            colorStateFlow = MutableStateFlow(HSVColor(0f, 1f, 1f))
        )

        Spacer(Modifier.weight(1f))
    }
}