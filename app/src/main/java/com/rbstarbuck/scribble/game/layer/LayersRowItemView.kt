package com.rbstarbuck.scribble.game.layer

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import com.rbstarbuck.scribble.R
import com.rbstarbuck.scribble.game.draw.CanvasBackgroundView
import com.rbstarbuck.scribble.game.draw.CommittedStrokesCanvasView
import kotlinx.coroutines.flow.StateFlow

@Composable
fun LayersRowItemView(
    layer: Layers.Layer,
    backgroundStateFlow: StateFlow<Color>,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    modifier: Modifier = Modifier
) {
    val selected by layer.selectedStateFlow.collectAsState()

    val animatedCanvasSize = animateFloatAsState(
        targetValue = if (selected) 80f else 60f,
        label = "canvasSize",
        animationSpec = tween(500)
    )

    Box(
        modifier = modifier
            .background(
                colorResource(
                    if (selected) {
                        R.color.selected_row_item_background
                    } else {
                        R.color.row_item_background
                    }
                )
            ),
        contentAlignment = Alignment.BottomStart
    ) {
        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val visible by layer.visibleStateFlow.collectAsState()
            val deleteDialogEnabled = remember { MutableStateFlow(false) }

            val grayscale = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })

            Box(
                modifier = Modifier
                    .size(animatedCanvasSize.value.dp)
                    .clickable { layer.select() }
            ) {
                CanvasBackgroundView(
                    backgroundStateFlow = backgroundStateFlow,
                    modifier = Modifier.fillMaxSize()
                )

                CommittedStrokesCanvasView(
                    strokes = layer.strokes,
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            width = if (selected) 3.dp else 1.5.dp,
                            color = colorResource(
                                if (selected) {
                                    R.color.selected_row_item_image_border
                                } else {
                                    R.color.row_item_image_border
                                }
                            )
                        )
                )
            }

            Spacer(Modifier.weight(1f))

            Image(
                imageVector = ImageVector.vectorResource(R.drawable.delete),
                contentDescription = stringResource(R.string.delete),
                modifier = Modifier
                    .size(36.dp)
                    .clickable { deleteDialogEnabled.value = true }
            )

            Spacer(Modifier.width(10.dp))

            Image(
                imageVector = ImageVector.vectorResource(R.drawable.visibility),
                contentDescription = stringResource(R.string.visibility),
                modifier = Modifier
                    .size(36.dp)
                    .clickable { layer.visible = !layer.visible },
                colorFilter = if (visible) null else grayscale
            )

            Spacer(Modifier.width(10.dp))

            Image(
                imageVector = ImageVector.vectorResource(R.drawable.arrow_up),
                contentDescription = stringResource(R.string.move_up),
                modifier = Modifier
                    .size(36.dp)
                    .clickable { layer.moveUp() },
                colorFilter = if (canMoveUp) null else grayscale
            )

            Spacer(Modifier.width(10.dp))

            Image(
                imageVector = ImageVector.vectorResource(R.drawable.arrow_down),
                contentDescription = stringResource(R.string.move_down),
                modifier = Modifier
                    .size(36.dp)
                    .clickable { layer.moveDown() },
                colorFilter = if (canMoveDown) null else grayscale
            )

            DeleteLayerConfirmationDialog(layer, deleteDialogEnabled)
        }

        if (canMoveUp) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.5.dp)
                    .background(colorResource(R.color.selected_row_item_background))
            )
        }
    }
}

@Preview
@Composable
fun LayersRowItemViewPreview() {
    val layers = Layers(MutableStateFlow(Color.Black), MutableStateFlow(10f))
    val layer by layers.selectedLayerStateFlow.collectAsState()

    LayersRowItemView(
        layer = layer,
        canMoveUp = true,
        canMoveDown = true,
        backgroundStateFlow = MutableStateFlow(Color.White)
    )
}

@Composable
private fun DeleteLayerConfirmationDialog(
    layer: Layers.Layer,
    enabledStateFlow: MutableStateFlow<Boolean>
) {
    val enabled by enabledStateFlow.collectAsState(

    )
    if (enabled) {
        AlertDialog(
            onDismissRequest = { enabledStateFlow.value = false },
            icon = {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.delete),
                    contentDescription = stringResource(R.string.delete_layer),
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { Text(stringResource(R.string.delete_layer)) },
            text = { Text(stringResource(R.string.delete_layer_dialog_text)) },
            confirmButton = {
                TextButton(
                    onClick = { layer.remove() }
                ) {
                    Text(stringResource(R.string.delete))
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