package com.rbstarbuck.scribble.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ColorMatrixColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.rbstarbuck.scribble.game.draw.DrawingView
import com.rbstarbuck.scribble.R
import com.rbstarbuck.scribble.game.color.ColorPickerView
import com.rbstarbuck.scribble.game.layer.LayersView
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun GameView(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { TabView() }
    ) { padding ->
        Column(
            modifier = modifier.padding(padding),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.weight(1f))

            DrawingView(
                viewModel = viewModel.drawingViewModel,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .border(
                        width = 2.dp,
                        color = colorResource(R.color.canvas_border)
                    )
            )

            val recompose by TabItem.recomposeFlag.collectAsState()

            key(recompose) {
                if (TabItem.ColorTabItem.isSelected) {
                    ColorPickerView(
                        viewModel = viewModel.colorPickerViewModel,
                        modifier = Modifier
                            .height(200.dp)
                            .padding(horizontal = 20.dp)
                    )
                }

                if (TabItem.LayersTabItem.isSelected) {
                    LayersView(
                        viewModel = viewModel.layersViewModel,
                        modifier = Modifier
                            .height(200.dp)
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun TabView() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val recompose by TabItem.recomposeFlag.collectAsState()

        for (item in TabItem.tabItems) {
            Column(
                modifier = Modifier.padding(top = 20.dp, bottom = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val (titleTextColor, colorFilter) = if (item.isSelected) {
                    Color.Black to null
                } else {
                    colorResource(R.color.tab_item_title) to
                            ColorMatrixColorFilter(ColorMatrix().apply { setToSaturation(0f) })
                }

                key(recompose) {
                    Image(
                        imageVector = ImageVector.vectorResource(item.icon),
                        contentDescription = stringResource(item.title),
                        modifier = Modifier
                            .size(36.dp)
                            .clickable {
                                TabItem.selectedItem.isSelected = false
                                TabItem.selectedItem = item
                                item.isSelected = true
                                TabItem.recomposeFlag.value = !TabItem.recomposeFlag.value
                            },
                        colorFilter = colorFilter
                    )

                    Text(
                        text = stringResource(item.title),
                        color = titleTextColor
                    )
                }
            }
        }
    }
}

open class TabItem(val title: Int, val icon: Int, var isSelected: Boolean = false) {
    object ColorTabItem: TabItem(
        title = R.string.color,
        icon = R.drawable.color,
        isSelected = true
    )

    object LayersTabItem: TabItem(
        title = R.string.layers,
        icon = R.drawable.layers
    )

    companion object {
        val tabItems: List<TabItem> = listOf(ColorTabItem, LayersTabItem)
        var selectedItem: TabItem = ColorTabItem
        val recomposeFlag = MutableStateFlow(false)
    }
}