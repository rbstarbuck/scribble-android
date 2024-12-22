package com.rbstarbuck.scribble.game.transform

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class TransformViewModel: ViewModel() {
    val selectedTransformTypeStateFlow = MutableStateFlow(TransformType.TRANSLATE)
}