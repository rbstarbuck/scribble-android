package com.rbstarbuck.scribble.game.color

import android.graphics.Color.HSVToColor
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.rbstarbuck.scribble.R
import com.rbstarbuck.scribble.koin.state.SelectedHue
import com.rbstarbuck.scribble.koin.state.SelectedSaturationAndValue
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

@Composable
fun SavedColorsView(modifier: Modifier = Modifier) {
    val viewModel: SavedColorsViewModel by inject(SavedColorsViewModel::class.java)

    val selectedHue: SelectedHue by inject(SelectedHue::class.java)
    val selectedSaturationAndValue:
            SelectedSaturationAndValue by inject(SelectedSaturationAndValue::class.java)

    val savedColors by viewModel.savedColorsStateFlow.collectAsState()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val listState = rememberLazyListState()
        val scope = rememberCoroutineScope()

        Image(
            imageVector = ImageVector.vectorResource(R.drawable.add),
            contentDescription = stringResource(R.string.add_color),
            modifier = Modifier
                .size(36.dp)
                .clickable {
                    viewModel.addColor()
                    scope.launch {
                        listState.animateScrollToItem(0)
                    }
                }
        )

        LazyColumn(state = listState) {
            items(
                count = savedColors.size,
                key = { i ->  savedColors[i].hashCode() }
            ) { i ->
                val savedColor = savedColors[i]

                Canvas(
                    modifier = Modifier
                        .size(36.dp)
                        .animateItem()
                        .clickable {
                            selectedHue.hue = savedColor.hue
                            selectedSaturationAndValue.saturationAndValue =
                                savedColor.saturation to savedColor.value
                        }
                ) {
                    val color = Color(
                        HSVToColor(
                            floatArrayOf(
                                savedColor.hue,
                                savedColor.saturation,
                                savedColor.value
                            )
                        )
                    )

                    drawCircle(
                        color = Color.Black,
                        radius = 12.dp.toPx(),
                        center = center
                    )

                    drawCircle(
                        color = color,
                        radius = 10.5.dp.toPx(),
                        center = center
                    )
                }
            }
        }
    }
}