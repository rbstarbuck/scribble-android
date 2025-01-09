package com.rbstarbuck.scribble.koin

import com.rbstarbuck.scribble.koin.state.SelectedStrokeWidth
import com.rbstarbuck.scribble.koin.state.SelectedStrokeWidthImpl
import org.koin.dsl.module

object Module {
    val stateModule = module {
        single<SelectedStrokeWidth> { SelectedStrokeWidthImpl() }
    }
}