package com.rbstarbuck.scribble.game.brush

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ColorMatrixColorFilter
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.rbstarbuck.scribble.R

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
            lineThicknessStateFlow = viewModel.lineThicknessStateFlow,
            colorStateFlow = viewModel.colorStateFlow
        )

        Row(Modifier.fillMaxWidth()) {
            val brushType by viewModel.brushTypeStateFlow.collectAsState()
            val fillType by viewModel.fillTypeStateFlow.collectAsState()

            Column {
                Row {
                    BrushButton(
                        onClick = {
                            viewModel.brushTypeStateFlow.value = BrushType.Pencil
                        },
                        icon = ImageVector.vectorResource(R.drawable.pencil),
                        contentDescription = stringResource(R.string.pencil),
                        selected = brushType == BrushType.Pencil
                    )

                    Spacer(Modifier.width(6.dp))

                    BrushButton(
                        onClick = {
                            viewModel.brushTypeStateFlow.value = BrushType.Eraser

                        },
                        icon = ImageVector.vectorResource(R.drawable.eraser),
                        contentDescription = stringResource(R.string.eraser),
                        selected = brushType == BrushType.Eraser
                    )
                }

                Spacer(Modifier.height(10.dp))

                Row {
                    BrushButton(
                        onClick = {
                            viewModel.brushTypeStateFlow.value = BrushType.Line
                        },
                        icon = ImageVector.vectorResource(R.drawable.line),
                        contentDescription = stringResource(R.string.line),
                        selected = brushType == BrushType.Line
                    )

                    Spacer(Modifier.width(6.dp))

                    BrushButton(
                        onClick = {
                            viewModel.brushTypeStateFlow.value = BrushType.Polygon
                        },
                        icon = ImageVector.vectorResource(R.drawable.polygon),
                        contentDescription = stringResource(R.string.polygon),
                        selected = brushType == BrushType.Polygon
                    )

                    Spacer(Modifier.width(6.dp))

                    BrushButton(
                        onClick = {
                            viewModel.brushTypeStateFlow.value = BrushType.Circle
                        },
                        icon = ImageVector.vectorResource(R.drawable.circle),
                        contentDescription = stringResource(R.string.circle),
                        selected = brushType == BrushType.Circle
                    )

                    Spacer(Modifier.width(15.dp))

                    FillTypeButton(
                        onClick = {
                            viewModel.fillTypeStateFlow.value = FillType.Stroke
                        },
                        enabled = brushType == BrushType.Polygon || brushType == BrushType.Circle,
                        selected = fillType == FillType.Stroke,
                        fillType = FillType.Stroke
                    )

                    Spacer(Modifier.width(6.dp))

                    FillTypeButton(
                        onClick = {
                            viewModel.fillTypeStateFlow.value = FillType.Filled
                        },
                        enabled = brushType == BrushType.Polygon || brushType == BrushType.Circle,
                        selected = fillType == FillType.Filled,
                        fillType = FillType.Filled,
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Image(
                imageVector = ImageVector.vectorResource(R.drawable.undo),
                contentDescription = stringResource(R.string.undo),
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
fun ButtonContainer(
    onClick: () -> Unit,
    enabled: Boolean,
    selected: Boolean,
    content: (@Composable RowScope.() -> Unit)
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.size(48.dp),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = if (selected) {
            ButtonColors(
                containerColor = Color.White,
                contentColor = Color.White,
                disabledContainerColor = colorResource(R.color.disabled_button),
                disabledContentColor = Color.White
            )
        } else {
            ButtonColors(
                containerColor = colorResource(R.color.deselected_button),
                contentColor = Color.White,
                disabledContainerColor = colorResource(R.color.disabled_button),
                disabledContentColor = Color.White
            )
        },
        contentPadding = PaddingValues(6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            content()
        }
    }
}

@Composable
fun BrushButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String,
    selected: Boolean
) {
    ButtonContainer(
        onClick = onClick,
        enabled = true,
        selected = selected
    ) {
        Image(
            imageVector = icon,
            contentDescription = contentDescription,
            colorFilter = if (selected) {
                null
            } else {
                ColorMatrixColorFilter(ColorMatrix().apply { setToSaturation(0f) })
            }
        )
    }
}

@Composable
fun FillTypeButton(
    onClick: () -> Unit,
    enabled: Boolean,
    selected: Boolean,
    fillType: FillType
) {
    ButtonContainer(
        onClick = onClick,
        enabled = enabled,
        selected = selected
    ) {
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