package com.rbstarbuck.scribble.koin

import com.rbstarbuck.scribble.game.brush.BrushViewModel
import com.rbstarbuck.scribble.game.color.ColorPickerViewModel
import com.rbstarbuck.scribble.game.color.SavedColorsViewModel
import com.rbstarbuck.scribble.game.draw.DrawingViewModel
import com.rbstarbuck.scribble.game.layer.Layers
import com.rbstarbuck.scribble.game.layer.LayersViewModel
import com.rbstarbuck.scribble.game.prompt.GamePromptViewModel
import com.rbstarbuck.scribble.game.transform.RotateViewModel
import com.rbstarbuck.scribble.game.transform.ScaleViewModel
import com.rbstarbuck.scribble.game.transform.TransformControlsViewModel
import com.rbstarbuck.scribble.game.transform.TranslateViewModel
import com.rbstarbuck.scribble.koin.state.SelectedBackgroundColor
import com.rbstarbuck.scribble.koin.state.SelectedBackgroundColorImpl
import com.rbstarbuck.scribble.koin.state.SelectedBrushType
import com.rbstarbuck.scribble.koin.state.SelectedBrushTypeImpl
import com.rbstarbuck.scribble.koin.state.SelectedColor
import com.rbstarbuck.scribble.koin.state.SelectedColorHue
import com.rbstarbuck.scribble.koin.state.SelectedColorHueImpl
import com.rbstarbuck.scribble.koin.state.SelectedColorImpl
import com.rbstarbuck.scribble.koin.state.SelectedColorSaturationAndValue
import com.rbstarbuck.scribble.koin.state.SelectedColorSaturationAndValueImpl
import com.rbstarbuck.scribble.koin.state.SelectedFillType
import com.rbstarbuck.scribble.koin.state.SelectedFillTypeImpl
import com.rbstarbuck.scribble.koin.state.SelectedLayer
import com.rbstarbuck.scribble.koin.state.SelectedLayerImpl
import com.rbstarbuck.scribble.koin.state.SelectedStrokeWidth
import com.rbstarbuck.scribble.koin.state.SelectedStrokeWidthImpl
import com.rbstarbuck.scribble.koin.state.SelectedTransformType
import com.rbstarbuck.scribble.koin.state.SelectedTransformTypeImpl
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

object Module {
    val stateModule = module {
        single<SelectedLayer> { SelectedLayerImpl() }
        single<SelectedColor> { SelectedColorImpl() }
        single<SelectedColorHue> { SelectedColorHueImpl() }
        single<SelectedColorSaturationAndValue> { SelectedColorSaturationAndValueImpl() }
        single<SelectedBackgroundColor> { SelectedBackgroundColorImpl() }
        single<SelectedStrokeWidth> { SelectedStrokeWidthImpl() }
        single<SelectedBrushType> { SelectedBrushTypeImpl() }
        single<SelectedFillType> { SelectedFillTypeImpl() }
        single<SelectedTransformType> { SelectedTransformTypeImpl() }
    }

    val typesModule = module {
        single<Layers> { Layers() }
    }

    val viewModelsModule = module {
        viewModel { GamePromptViewModel() }
        viewModel { DrawingViewModel() }
        viewModel { BrushViewModel() }
        viewModel { ColorPickerViewModel() }
        viewModel { SavedColorsViewModel() }
        viewModel { LayersViewModel() }
        viewModel { TransformControlsViewModel() }
        viewModel { TranslateViewModel() }
        viewModel { ScaleViewModel() }
        viewModel { RotateViewModel() }
    }
}