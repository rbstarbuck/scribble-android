package com.rbstarbuck.scribble.game.layer

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun LayersView(viewModel: LayersViewModel, modifier: Modifier = Modifier) {
    val layers by viewModel.layers.layersStateFlow.collectAsState()

    LazyRow(modifier = modifier) {
        items(
            count = layers.size,
            key = { i ->
                layers[i].key
            }
        ) { i ->
            LayersRowItemView(
                layer = layers[i],
                backgroundStateFlow = viewModel.backgroundStateFlow,
                canMoveUp = i > 0,
                canMoveDown = i < layers.size - 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem()
            )
        }
    }
}