package com.rbstarbuck.scribble.game.transform

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rbstarbuck.scribble.R
import com.rbstarbuck.scribble.game.layer.emptyLayer
import com.rbstarbuck.scribble.util.SelectionButton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun TransformView(
    viewModel: TransformViewModel,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val transformType by viewModel.selectedTransformTypeStateFlow.collectAsState()

            Column(Modifier.fillMaxHeight()) {
                val size = this@BoxWithConstraints.maxHeight * 0.3f

                SelectionButton(
                    onClick = {
                        viewModel.selectedTransformTypeStateFlow.value = TransformType.TRANSLATE
                    },
                    icon = ImageVector.vectorResource(R.drawable.move),
                    contentDescription = stringResource(R.string.move),
                    size = size,
                    selected = transformType == TransformType.TRANSLATE
                )

                Spacer(Modifier.weight(1f))

                SelectionButton(
                    onClick = {
                        viewModel.selectedTransformTypeStateFlow.value = TransformType.SCALE
                    },
                    icon = ImageVector.vectorResource(R.drawable.scale),
                    contentDescription = stringResource(R.string.scale),
                    size = size,
                    selected = transformType == TransformType.SCALE
                )

                Spacer(Modifier.weight(1f))

                SelectionButton(
                    onClick = {
                        viewModel.selectedTransformTypeStateFlow.value = TransformType.ROTATE
                    },
                    icon = ImageVector.vectorResource(R.drawable.rotate),
                    contentDescription = stringResource(R.string.rotate),
                    size = size,
                    selected = transformType == TransformType.ROTATE
                )
            }

            when (transformType) {
                TransformType.TRANSLATE -> TranslateControlsView(
                    viewModel = viewModel,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 20.dp)
                )

                TransformType.SCALE -> ScaleControlsView(
                    viewModel = viewModel,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 20.dp)
                )

                TransformType.ROTATE -> RotateControlsView(
                    viewModel = viewModel,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 20.dp)
                )
            }

        }
    }
}

@Composable
private fun TranslateControlsView(
    viewModel: TransformViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        val selectedLayer by viewModel.selectedLayerStateFlow.collectAsState()
        val translation by selectedLayer.strokes.translationStateFlow.collectAsState()

        val boundingBox = remember { selectedLayer.strokes.boundingBox() }

        val translateXStateFlow = MutableStateFlow(boundingBox.center.x + translation.x)
        val translateYStateFlow =  MutableStateFlow(boundingBox.center.y + translation.y)

        TransformSlider(
            stateFlow = translateXStateFlow,
            label = stringResource(R.string.x)
        ) { _, change ->
            selectedLayer.strokes.translate(x = change, y = 0f)
        }

        TransformSlider(
            stateFlow = translateYStateFlow,
            label = stringResource(R.string.y)
        ) { _, change ->
            selectedLayer.strokes.translate(x = 0f, y = change)
        }
    }
}

@Composable
private fun ScaleControlsView(
    viewModel: TransformViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        val selectedLayer by viewModel.selectedLayerStateFlow.collectAsState()
        val scale by selectedLayer.strokes.scaleStateFlow.collectAsState()

        val scaleXYStateFlow = MutableStateFlow((scale.x + scale.y) / 2f)
        val scaleXStateFlow = MutableStateFlow(scale.x)
        val scaleYStateFlow = MutableStateFlow(scale.y)

        TransformSlider(
            stateFlow = scaleXYStateFlow,
            label = stringResource(R.string.xy),
            valueRange = 0f..4f
        ) { _, change ->
            selectedLayer.strokes.scale(dX = change + 1f, dY = change + 1f)
        }

        TransformSlider(
            stateFlow = scaleXStateFlow,
            label = stringResource(R.string.x),
            valueRange = 0f..4f
        ) { _, change ->
            selectedLayer.strokes.scale(dX = change + 1f, dY = 1f)
        }

        TransformSlider(
            stateFlow = scaleYStateFlow,
            label = stringResource(R.string.y),
            valueRange = 0f..4f
        ) { _, change ->
            selectedLayer.strokes.scale(dX = 1f, dY = change + 1f)
        }
    }
}

@Composable
private fun RotateControlsView(
    viewModel: TransformViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        val selectedLayer by viewModel.selectedLayerStateFlow.collectAsState()

        val boundsStateFlow = MutableStateFlow(selectedLayer.strokes.boundingBox())
        val bounds by boundsStateFlow.collectAsState()

        TransformSlider(
            stateFlow = selectedLayer.strokes.rotationStateFlow,
            label = stringResource(R.string.z),
            valueRange = -180f..180f,
            onFinished = { boundsStateFlow.value = selectedLayer.strokes.boundingBox() }
        ) { _, change ->
            selectedLayer.strokes.rotateZ(
                degrees = change,
                strokesCenter = bounds.center
            )
        }
    }
}

@Composable
private fun TransformSlider(
    stateFlow: StateFlow<Float>,
    label: String,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    modifier: Modifier = Modifier,
    onFinished: (() -> Unit)? = null,
    onValueChange: (Float, Float) -> Unit
) {
    val state by stateFlow.collectAsState()

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 20.sp,
            modifier = Modifier.width(30.dp)
        )

        Slider(
            value = state,
            valueRange = valueRange,
            modifier = Modifier.height(30.dp),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            onValueChange = {
                onValueChange(it, it - state)
            },
            onValueChangeFinished = onFinished
        )
    }
}

@Preview
@Composable
fun TransformViewPreview() {
    val viewModel = TransformViewModel(MutableStateFlow(emptyLayer()))

    TransformView(
        viewModel = viewModel,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .width(400.dp)
            .height(175.dp)
    )
}