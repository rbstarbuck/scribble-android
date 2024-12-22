package com.rbstarbuck.scribble.util

import android.graphics.Bitmap
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun SoftwareLayer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    AndroidView(
        factory = { context ->
            ComposeView(context).apply {
                setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            }
        },
        update = { composeView ->
            composeView.setContent(content)
        },
        modifier = modifier,
    )
}

@Composable
fun drawToBitmap(
    size: Size,
    content: @Composable DrawScope.() -> Unit
): ImageBitmap {
    val drawScope = CanvasDrawScope()
    val bitmap =
        ImageBitmap(
            width = size.width.toInt().coerceIn(1, Int.MAX_VALUE),
            height = size.height.toInt().coerceIn(1, Int.MAX_VALUE)
        )
    val canvas = Canvas(bitmap)

    drawScope.draw(
        density = Density(1f),
        layoutDirection = LayoutDirection.Ltr,
        canvas = canvas,
        size = size,
    ) {
        content()
    }

    return bitmap
}