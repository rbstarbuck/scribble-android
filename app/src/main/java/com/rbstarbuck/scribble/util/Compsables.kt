package com.rbstarbuck.scribble.util

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
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