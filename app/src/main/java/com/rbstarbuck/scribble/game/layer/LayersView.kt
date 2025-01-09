package com.rbstarbuck.scribble.game.layer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rbstarbuck.scribble.R
import org.koin.java.KoinJavaComponent.inject

@Composable
fun LayersView(modifier: Modifier = Modifier) {
    val viewModel: LayersViewModel by inject(LayersViewModel::class.java)

    Column(modifier) {
        val layers by viewModel.layers.layersStateFlow.collectAsState()

        Row {
            TextButton(
                onClick = { viewModel.layers.addAndSelect() }
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.add),
                    contentDescription = stringResource(R.string.add_layer),
                    modifier = Modifier.size(36.dp)
                )

                Spacer(Modifier.width(6.dp))

                Text(
                    text = stringResource(R.string.add_layer),
                    fontSize = 16.sp
                )
            }
        }

        LazyColumn(Modifier.fillMaxWidth()) {
            items(
                count = layers.size,
                key = { i ->
                    layers[i].key
                }
            ) { i ->
                LayersColumnItemView(
                    layer = layers[i],
                    canMoveUp = i > 0,
                    canMoveDown = i < layers.size - 1,
                    modifier = Modifier.animateItem()
                )
            }
        }
    }
}