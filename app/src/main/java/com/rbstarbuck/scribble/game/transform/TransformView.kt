package com.rbstarbuck.scribble.game.transform

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rbstarbuck.scribble.R
import com.rbstarbuck.scribble.util.SelectionButton

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
            val transformType by viewModel.transformTypeStateFlow.collectAsState()

            val size = this@BoxWithConstraints.maxWidth * 0.3f

            SelectionButton(
                onClick = {
                    viewModel.transformTypeStateFlow.value = TransformType.TRANSLATE
                },
                icon = ImageVector.vectorResource(R.drawable.move),
                contentDescription = stringResource(R.string.move),
                size = size,
                selected = transformType == TransformType.TRANSLATE
            )

            Spacer(Modifier.weight(1f))

            SelectionButton(
                onClick = {
                    viewModel.transformTypeStateFlow.value = TransformType.SCALE
                },
                icon = ImageVector.vectorResource(R.drawable.scale),
                contentDescription = stringResource(R.string.scale),
                size = size,
                selected = transformType == TransformType.SCALE
            )

            Spacer(Modifier.weight(1f))

            SelectionButton(
                onClick = {
                    viewModel.transformTypeStateFlow.value = TransformType.ROTATE
                },
                icon = ImageVector.vectorResource(R.drawable.rotate),
                contentDescription = stringResource(R.string.rotate),
                size = size,
                selected = transformType == TransformType.ROTATE
            )
        }
    }
}

@Preview
@Composable
fun TransformViewPreview() {
    val viewModel = TransformViewModel()

    TransformView(
        viewModel = viewModel,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .width(400.dp)
            .height(175.dp)
    )
}