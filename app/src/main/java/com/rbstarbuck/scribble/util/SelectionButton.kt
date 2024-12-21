package com.rbstarbuck.scribble.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ColorMatrixColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rbstarbuck.scribble.R

@Composable
fun SelectionButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String,
    size: Dp,
    contentPadding: Dp = 0.dp,
    selected: Boolean,
    enabled: Boolean = true
) {
    SelectionButtonContainer(onClick, size, enabled, selected) {
        Image(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.padding(contentPadding),
            colorFilter = if (selected) {
                null
            } else {
                ColorMatrixColorFilter(ColorMatrix().apply { setToSaturation(0f) })
            }
        )
    }
}

@Composable
fun SelectionButtonContainer(
    onClick: () -> Unit,
    size: Dp,
    enabled: Boolean,
    selected: Boolean,
    content: (@Composable RowScope.() -> Unit)
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.size(size),
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