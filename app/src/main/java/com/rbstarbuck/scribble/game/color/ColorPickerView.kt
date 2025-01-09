package com.rbstarbuck.scribble.game.color

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rbstarbuck.scribble.R
import org.koin.java.KoinJavaComponent.inject

@Composable
fun ColorPickerView(modifier: Modifier = Modifier) {
    val viewModel: ColorPickerViewModel by inject(ColorPickerViewModel::class.java)

    val color by viewModel.selectedColor.stateFlow.collectAsState()

    Row(modifier) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SaturationAndValuePicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            TextButton(
                onClick = {
                    viewModel.selectedBackgroundColor.color = color.toColor()
                }
            ) {
                Text(stringResource(R.string.set_background_color))
            }
        }

        Spacer(Modifier.width(25.dp))

        HuePicker(
            modifier = Modifier
                .fillMaxHeight()
                .width(30.dp)
        )

        Spacer(Modifier.width(25.dp))

        SavedColorsView(Modifier.fillMaxHeight())
    }
}

@Preview
@Composable
fun ColorPickerViewPreview() {
    ColorPickerView(
        modifier = Modifier
            .height(200.dp)
            .padding(horizontal = 20.dp)
    )
}