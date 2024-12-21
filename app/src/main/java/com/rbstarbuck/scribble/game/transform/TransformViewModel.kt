package com.rbstarbuck.scribble.game.transform

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class TransformViewModel: ViewModel() {
    val transformTypeStateFlow = MutableStateFlow(TransformType.TRANSLATE)
}