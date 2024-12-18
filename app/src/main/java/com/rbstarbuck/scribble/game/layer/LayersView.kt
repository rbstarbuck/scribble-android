package com.rbstarbuck.scribble.game.layer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rbstarbuck.scribble.R
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun LayersView(
    viewModel: LayersViewModel,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        val layers by viewModel.layers.layersStateFlow.collectAsState()
        Row {
            val selectedLayer by viewModel.layers.selectedLayerStateFlow.collectAsState()

            val mergeDownEnabledStateFlow = remember { MutableStateFlow(false) }

            TextButton(
                onClick = { viewModel.layers.addAndSelect() }
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.add),
                    contentDescription = stringResource(R.string.add_layer),
                    modifier = Modifier.size(36.dp)
                )

                Spacer(Modifier.width(5.dp))

                Text(
                    text = stringResource(R.string.add_layer),
                    fontSize = 16.sp
                )
            }

            Spacer(Modifier.weight(1f))

            TextButton(
                onClick = { mergeDownEnabledStateFlow.value = true },
                enabled = selectedLayer.canMergeDown
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.merge_down),
                    contentDescription = stringResource(R.string.merge_layers),
                    modifier = Modifier.size(36.dp)
                )

                Text(
                    text = stringResource(R.string.merge),
                    fontSize = 16.sp
                )
            }

            MergeLayerConfirmationDialog(selectedLayer, mergeDownEnabledStateFlow)
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
                    backgroundStateFlow = viewModel.backgroundStateFlow,
                    canMoveUp = i > 0,
                    canMoveDown = i < layers.size - 1,
                    modifier = Modifier.animateItem()
                )
            }
        }
    }
}

@Composable
fun MergeLayerConfirmationDialog(
    layer: Layers.Layer,
    enabledStateFlow: MutableStateFlow<Boolean>
) {
    val enabled by enabledStateFlow.collectAsState()

    if (enabled) {
        AlertDialog(
            onDismissRequest = { enabledStateFlow.value = false },
            icon = {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.merge_down),
                    contentDescription = stringResource(R.string.merge_layers),
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { Text(stringResource(R.string.merge_layers)) },
            text = { Text(stringResource(R.string.merge_layers_dialog_text)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        layer.mergeDown()
                        enabledStateFlow.value = false
                    }
                ) {
                    Text(stringResource(R.string.merge))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { enabledStateFlow.value = false }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            },
        )
    }
}