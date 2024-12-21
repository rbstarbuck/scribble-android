package com.rbstarbuck.scribble.game.color

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rbstarbuck.scribble.R

@Composable
fun ColorPickerView(
    viewModel: ColorPickerViewModel,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SaturationAndValuePicker(
                hueStateFlow = viewModel.hueStateFlow,
                saturationValueStateFlow = viewModel.saturationValueStateFlow,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            TextButton(
                onClick = {
                    viewModel.backgroundStateFlow.value = viewModel.colorStateFlow.value.toColor()
                }
            ) {
                Text(stringResource(R.string.set_background_color))
            }
        }

        Spacer(Modifier.width(25.dp))

        HuePicker(
            hueStateFlow = viewModel.hueStateFlow,
            saturationValueStateFlow = viewModel.saturationValueStateFlow,
            modifier = Modifier
                .fillMaxHeight()
                .width(30.dp)
        )

        Spacer(Modifier.width(25.dp))

        SavedColorsView(
            viewModel = viewModel.savedColorsViewModel,
            modifier = Modifier.fillMaxHeight()
        )
    }
}

@Preview
@Composable
fun ColorPickerViewPreview() {
    val viewModel = remember { ColorPickerViewModel() }

    val color by viewModel.colorStateFlow.collectAsState()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(50.dp))

        ColorPickerView(
            viewModel = viewModel,
            modifier = Modifier
                .height(200.dp)
                .padding(horizontal = 20.dp)
        )

        Spacer(Modifier.height(50.dp))

        Box(
            modifier = Modifier
                .size(60.dp)
                .background(color.toColor())
        )
    }
}