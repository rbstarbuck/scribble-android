package com.rbstarbuck.scribble.game.transform

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.rbstarbuck.scribble.R
import com.rbstarbuck.scribble.util.pxToDp
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun TranslateView(
    viewModel: TranslateViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, _, ->

                    }
                )
            }
    ) {
        val selectedLayer by viewModel.selectedLayer.collectAsState()

        val boundsStateFlow = remember { MutableStateFlow(Rect(Offset(0f, 0f), Size(0f, 0f))) }
        val bounds by boundsStateFlow.collectAsState()

        Canvas(Modifier.fillMaxSize()) {
            boundsStateFlow.value = drawTransformBox(selectedLayer.strokes, context)
        }

        Image(
            imageVector = ImageVector.vectorResource(R.drawable.translate),
            contentDescription = stringResource(R.string.move),
            modifier = Modifier
                .size(32.dp)
                .offset(
                    x = bounds.center.x.pxToDp(context) - 16.dp,
                    y = bounds.center.y.pxToDp(context) - 16.dp
                )
                .background(Color.White),
            colorFilter = ColorFilter.tint(Color.DarkGray)
        )
    }
}