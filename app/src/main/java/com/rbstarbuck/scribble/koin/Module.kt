package com.rbstarbuck.scribble.koin

import com.rbstarbuck.scribble.game.layer.Layers
import com.rbstarbuck.scribble.koin.state.SelectedBackgroundColor
import com.rbstarbuck.scribble.koin.state.SelectedBackgroundColorImpl
import com.rbstarbuck.scribble.koin.state.SelectedBrushType
import com.rbstarbuck.scribble.koin.state.SelectedBrushTypeImpl
import com.rbstarbuck.scribble.koin.state.SelectedColor
import com.rbstarbuck.scribble.koin.state.SelectedColorImpl
import com.rbstarbuck.scribble.koin.state.SelectedFillType
import com.rbstarbuck.scribble.koin.state.SelectedFillTypeImpl
import com.rbstarbuck.scribble.koin.state.SelectedLayer
import com.rbstarbuck.scribble.koin.state.SelectedLayerImpl
import com.rbstarbuck.scribble.koin.state.SelectedStrokeWidth
import com.rbstarbuck.scribble.koin.state.SelectedStrokeWidthImpl
import com.rbstarbuck.scribble.koin.state.SelectedTransformType
import com.rbstarbuck.scribble.koin.state.SelectedTransformTypeImpl
import org.koin.dsl.module

object Module {
    val stateModule = module {
        single<SelectedLayer> { SelectedLayerImpl() }
        single<SelectedColor> { SelectedColorImpl() }
        single<SelectedBackgroundColor> { SelectedBackgroundColorImpl() }
        single<SelectedStrokeWidth> { SelectedStrokeWidthImpl() }
        single<SelectedBrushType> { SelectedBrushTypeImpl() }
        single<SelectedFillType> { SelectedFillTypeImpl() }
        single<SelectedTransformType> { SelectedTransformTypeImpl() }
    }

    val typesModule = module {
        single<Layers> { Layers() }
    }
}