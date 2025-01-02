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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.rbstarbuck.scribble.game.brush.BrushView
import com.rbstarbuck.scribble.game.prompt.GamePromptView
import com.rbstarbuck.scribble.game.transform.TransformView
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
            val tabItemRecompose by TabItem.recomposeFlag.collectAsState()

            GamePromptView(
                viewModel = viewModel.gamePromptViewModel,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
                    .padding(top = 10.dp, start = 20.dp, end = 20.dp)
            )

            DrawingView(
                viewModel = viewModel.drawingViewModel,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .border(
                        width = 2.dp,
                        color = colorResource(R.color.canvas_border)
                    )
            )

            key(tabItemRecompose) {
                if (TabItem.BrushTabItem.isSelected) {
                    BrushView(
                        viewModel = viewModel.brushViewModel,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 20.dp)
                    )
                }

                if (TabItem.ColorTabItem.isSelected) {
                    ColorPickerView(
                        viewModel = viewModel.colorPickerViewModel,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 20.dp)
                    )
                }

                if (TabItem.LayersTabItem.isSelected) {
                    LayersView(
                        viewModel = viewModel.layersViewModel,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 20.dp)
                    )
                }

                if (TabItem.TransformTabItem.isSelected) {
                    TransformView(
                        viewModel = viewModel.transformViewModel,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 20.dp, vertical = 10.dp)
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
            fun selectTab() {
                TabItem.selectedItem.isSelected = false
                TabItem.selectedItem = item
                item.isSelected = true
                TabItem.recomposeFlag.value = !TabItem.recomposeFlag.value
            }

            Column(
                modifier = Modifier.padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val colorFilter = if (item.isSelected) {
                    null
                } else {
                    ColorMatrixColorFilter(ColorMatrix().apply { setToSaturation(0f) })
                }

                key(recompose) {
                    Image(
                        imageVector = ImageVector.vectorResource(item.icon),
                        contentDescription = stringResource(item.title),
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { selectTab() },
                        colorFilter = colorFilter
                    )
                }
            }
        }
    }
}

open class TabItem(val title: Int, val icon: Int, var isSelected: Boolean = false) {
    object BrushTabItem: TabItem(
        title = R.string.brush,
        icon = R.drawable.brush,
        isSelected = true
    )

    object ColorTabItem: TabItem(
        title = R.string.color,
        icon = R.drawable.color
    )

    object LayersTabItem: TabItem(
        title = R.string.layers,
        icon = R.drawable.layers
    )

    object TransformTabItem: TabItem(
        title = R.string.transform,
        icon = R.drawable.transform
    )

    companion object {
        val tabItems: List<TabItem> = listOf(
            BrushTabItem,
            ColorTabItem,
            LayersTabItem,
            TransformTabItem
        )
        var selectedItem: TabItem = BrushTabItem
        val recomposeFlag = MutableStateFlow(false)
    }
}