package com.rbstarbuck.scribble.game.brush

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BrushView(
    viewModel: BrushViewModel,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        LineThicknessSlider(
            modifier = Modifier.fillMaxWidth(),
            lineThicknessStateFlow = viewModel.lineThicknessStateFlow,
            colorStateFlow = viewModel.colorStateFlow
        )
    }
}