package com.rbstarbuck.scribble.game.layer

import com.rbstarbuck.scribble.game.brush.BrushType
import com.rbstarbuck.scribble.game.brush.FillType
import com.rbstarbuck.scribble.game.color.HSVColor
import com.rbstarbuck.scribble.game.draw.Strokes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class Layers(
    private val selectedColorStateFlow: StateFlow<HSVColor>,
    private val selectedStrokeWidthStateFlow: StateFlow<Float>,
    private val selectedBrushTypeStateFlow: StateFlow<BrushType>,
    private val selectedFillTypeStateFlow: StateFlow<FillType>
) {
    private val _layersStateFlow = MutableStateFlow(listOf(Layer()))
    val layersStateFlow = _layersStateFlow.asStateFlow()

    private val _selectedLayerStateFlow = MutableStateFlow(_layersStateFlow.value.first())
    val selectedLayerStateFlow = _selectedLayerStateFlow.asStateFlow()

    fun addAndSelect() {
        val layer = Layer()
        _layersStateFlow.value = listOf(layer) + _layersStateFlow.value
        layer.select()
        _layersStateFlow
    }

    inner class Layer {
        val key = generateKey()
        val strokes = Strokes(
            selectedColorStateFlow,
            selectedStrokeWidthStateFlow,
            selectedBrushTypeStateFlow,
            selectedFillTypeStateFlow
        )

        private val _visibleStateFlow = MutableStateFlow(true)
        val visibleStateFlow = _visibleStateFlow.asStateFlow()
        var visible: Boolean
            get() = visibleStateFlow.value
            set(value) {
                _visibleStateFlow.value = value
            }

        private val _selectedStateFlow = MutableStateFlow(true)
        val selectedStateFlow = _selectedStateFlow.asStateFlow()
        private var selected: Boolean
            get() = selectedStateFlow.value
            set(value) {
                _selectedStateFlow.value = value
            }

        fun select() {
            _selectedLayerStateFlow.value.selected = false
            selected = true
            _selectedLayerStateFlow.value = this
        }

        fun remove() {
            val layers = _layersStateFlow.value

            if (selected) {
                val index = layers.indexOfFirst { it.key == key }
                if (index < layers.size - 1) {
                    layers[index + 1].select()
                } else if (index > 0) {
                    layers[index - 1].select()
                }
            }

            _layersStateFlow.value = layersStateFlow.value.filter { it.key != key }
        }

        fun moveUp() {
            val layers = layersStateFlow.value

            if (layers.size > 1) {
                val index = layers.indexOfFirst { it.key == key }
                if (index > 0) {
                    _layersStateFlow.value = layers.subList(0, index - 1) +
                            layers[index] +
                            layers[index - 1] +
                            if (index < layers.size - 1) {
                                layers.subList(index + 1, layers.size)
                            } else {
                                emptyList()
                            }
                }
            }
        }

        fun moveDown() {
            val layers = layersStateFlow.value

            if (layers.size > 1) {
                val index = layers.indexOfFirst { it.key == key }
                if (index >= 0 && index < layers.size - 1) {
                    _layersStateFlow.value = layers.subList(0, index) +
                                layers[index + 1] +
                                layers[index] +
                                if (index < layers.size - 2) {
                                    layers.subList(index + 2, layers.size)
                                } else {
                                    emptyList()
                                }

                }
            }
        }

        fun mergeDown() {
            val layers = layersStateFlow.value
            val index = layers.indexOfFirst { it.key == key }

            if (index >= 0 && index < layers.size - 1) {
                layers[index].strokes.mergeInto(layers[index + 1].strokes)
                layers[index + 1].select()
                layers[index].remove()
            }
        }

        fun undo() {
            strokes.undo()
        }
    }
}

private val chars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
fun generateKey(size: Int = 32) = Array(size) { chars.random() }.joinToString("")