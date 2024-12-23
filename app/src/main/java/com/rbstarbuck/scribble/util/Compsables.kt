package com.rbstarbuck.scribble.util

import android.graphics.Bitmap
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.launch

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

//@Composable
//fun drawToBitmap(
//    size: Size,
//    content: @Composable DrawScope.() -> Unit
//): ImageBitmap {
//    val drawScope = CanvasDrawScope()
//    val bitmap =
//        ImageBitmap(
//            width = size.width.toInt().coerceIn(1, Int.MAX_VALUE),
//            height = size.height.toInt().coerceIn(1, Int.MAX_VALUE)
//        )
//    val canvas = Canvas(bitmap)
//
//    drawScope.draw(
//        density = Density(1f),
//        layoutDirection = LayoutDirection.Ltr,
//        canvas = canvas,
//        size = size,
//    ) {
//        content()
//    }
//
//    return bitmap
//}

@Composable
fun Bitmap(modifier: Modifier = Modifier, composable: @Composable () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()
    Box(
        modifier = modifier
            .drawWithContent {
                // call record to capture the content in the graphics layer
                graphicsLayer.record {
                    // draw the contents of the composable into the graphics layer
                    this@drawWithContent.drawContent()
                }
                // draw the graphics layer on the visible canvas
                drawLayer(graphicsLayer)
            }
    ) {
        composable()
    }
}