package com.rbstarbuck.scribble.game.color

import android.graphics.Color.HSVToColor
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ColorPickerView(
    viewModel: ColorPickerViewModel,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        SaturationAndValuePicker(
            saturationValueStateFlow = viewModel.saturationValueStateFlow,
            hueStateFlow = viewModel.hueStateFlow,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
        )

        Spacer(Modifier.width(15.dp))

        HuePicker(
            hueStateFlow = viewModel.hueStateFlow,
            modifier = Modifier
                .fillMaxHeight()
                .width(30.dp)
        )
    }
}

@Preview
@Composable
fun ColorPickerViewPreview() {
    val viewModel = remember { ColorPickerViewModel() }

    val hue by viewModel.hueStateFlow.collectAsState()
    val saturationValue by viewModel.saturationValueStateFlow.collectAsState()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(50.dp))

        Row {
            Spacer(Modifier.weight(1f))

            ColorPickerView(
                viewModel = viewModel,
                modifier = Modifier.height(200.dp)
            )

            Spacer(Modifier.weight(1f))
        }

        Spacer(Modifier.height(50.dp))

        Box(
            modifier = Modifier
                .size(60.dp)
                .background(
                    Color(
                        HSVToColor(floatArrayOf(hue, saturationValue.first, saturationValue.second))
                    )
                )
        )
    }
}