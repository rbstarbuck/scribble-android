package com.rbstarbuck.scribble.game.brush

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rbstarbuck.scribble.R
import com.rbstarbuck.scribble.util.SelectionButton
import com.rbstarbuck.scribble.util.SelectionButtonContainer

@Composable
fun BrushView(
    viewModel: BrushViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        StrokeWidthSlider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            colorStateFlow = viewModel.colorStateFlow
        )

        Row(Modifier.fillMaxWidth()) {
            val brushType by viewModel.brushTypeStateFlow.collectAsState()
            val fillType by viewModel.fillTypeStateFlow.collectAsState()

            Column {
                Row {
                    SelectionButton(
                        onClick = {
                            viewModel.brushTypeStateFlow.value = BrushType.Pencil
                        },
                        icon = ImageVector.vectorResource(R.drawable.pencil),
                        contentDescription = stringResource(R.string.pencil),
                        size = 44.dp,
                        selected = brushType == BrushType.Pencil
                    )

                    Spacer(Modifier.width(6.dp))

                    SelectionButton(
                        onClick = {
                            viewModel.brushTypeStateFlow.value = BrushType.Eraser

                        },
                        icon = ImageVector.vectorResource(R.drawable.eraser),
                        contentDescription = stringResource(R.string.eraser),
                        size = 44.dp,
                        selected = brushType == BrushType.Eraser
                    )

                    Spacer(Modifier.width(6.dp))

                    SelectionButton(
                        onClick = {
                            viewModel.brushTypeStateFlow.value = BrushType.Line
                        },
                        icon = ImageVector.vectorResource(R.drawable.line),
                        contentDescription = stringResource(R.string.line),
                        size = 44.dp,
                        selected = brushType == BrushType.Line
                    )

                    Spacer(Modifier.weight(1f))

                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.undo),
                        contentDescription = stringResource(R.string.undo),
                        modifier = Modifier
                            .size(44.dp)
                            .clickable {
                                viewModel.selectedLayerStateFlow.value.undo()
                            }
                    )
                }

                Spacer(Modifier.height(10.dp))

                Row {
                    val fillTypeEnabled = when (brushType) {
                        BrushType.Polygon, BrushType.Rectangle, BrushType.Circle -> true
                        else -> false
                    }

                    SelectionButton(
                        onClick = {
                            viewModel.brushTypeStateFlow.value = BrushType.Rectangle
                        },
                        icon = ImageVector.vectorResource(R.drawable.square),
                        contentDescription = stringResource(R.string.rectangle),
                        size = 44.dp,
                        contentPadding = 3.dp,
                        selected = brushType == BrushType.Rectangle
                    )

                    Spacer(Modifier.width(6.dp))

                    SelectionButton(
                        onClick = {
                            viewModel.brushTypeStateFlow.value = BrushType.Circle
                        },
                        icon = ImageVector.vectorResource(R.drawable.circle),
                        contentDescription = stringResource(R.string.circle),
                        size = 44.dp,
                        contentPadding = 2.dp,
                        selected = brushType == BrushType.Circle
                    )

                    Spacer(Modifier.width(6.dp))

                    SelectionButton(
                        onClick = {
                            viewModel.brushTypeStateFlow.value = BrushType.Polygon
                        },
                        icon = ImageVector.vectorResource(R.drawable.polygon),
                        contentDescription = stringResource(R.string.polygon),
                        size = 44.dp,
                        selected = brushType == BrushType.Polygon
                    )

                    Spacer(Modifier.width(15.dp))

                    FillTypeButton(
                        onClick = {
                            viewModel.fillTypeStateFlow.value = FillType.Stroke
                        },
                        size = 44.dp,
                        enabled = fillTypeEnabled,
                        selected = fillType == FillType.Stroke,
                        fillType = FillType.Stroke
                    )

                    Spacer(Modifier.width(6.dp))

                    FillTypeButton(
                        onClick = {
                            viewModel.fillTypeStateFlow.value = FillType.Filled
                        },
                        size = 44.dp,
                        enabled = fillTypeEnabled,
                        selected = fillType == FillType.Filled,
                        fillType = FillType.Filled,
                    )
                }
            }
        }
    }
}

@Composable
private fun FillTypeButton(
    onClick: () -> Unit,
    size: Dp,
    enabled: Boolean,
    selected: Boolean,
    fillType: FillType
) {
    SelectionButtonContainer(onClick, size, enabled, selected) {
        Canvas(Modifier.size(20.dp)) {
            drawCircle(
                color = if (enabled && selected) {
                    Color.Blue
                } else {
                    Color.DarkGray
                },
                style = when (fillType) {
                    FillType.Filled -> Fill
                    FillType.Stroke -> Stroke(width = 2.dp.toPx())
                }
            )
        }
    }
}