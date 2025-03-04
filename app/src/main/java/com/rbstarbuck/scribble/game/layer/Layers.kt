package com.rbstarbuck.scribble.game.layer

import com.rbstarbuck.scribble.game.draw.Strokes
import com.rbstarbuck.scribble.koin.state.SelectedLayer
import com.rbstarbuck.scribble.util.generateKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.java.KoinJavaComponent.inject

class Layers(
    layers: List<Layer> = emptyList()
) {
    val selectedLayer: SelectedLayer by inject(SelectedLayer::class.java)

    private val _layersStateFlow = MutableStateFlow(layers)
    val layersStateFlow = _layersStateFlow.asStateFlow()

    init {
        if (layers.isEmpty()) {
            addAndSelect()
        }
    }

    fun addAndSelect(): Layer {
        val layer = Layer()
        _layersStateFlow.value = listOf(layer) + _layersStateFlow.value
        layer.select()
        return layer
    }

    inner class Layer(
        unpackedKey: String = generateKey(),
        unpackedStrokes: Strokes = Strokes(),
        unpackedVisible: Boolean = true,
        unpackedSelected: Boolean = true
    ) {
        val key = unpackedKey

        private var _strokes = unpackedStrokes
        val strokes: Strokes
            get() = _strokes

        private val _visibleStateFlow = MutableStateFlow(unpackedVisible)
        val visibleStateFlow = _visibleStateFlow.asStateFlow()
        var visible: Boolean
            get() = visibleStateFlow.value
            set(value) {
                _visibleStateFlow.value = value
            }

        private val _selectedStateFlow = MutableStateFlow(unpackedSelected)
        val selectedStateFlow = _selectedStateFlow.asStateFlow()
        private var selected: Boolean
            get() = selectedStateFlow.value
            set(value) {
                _selectedStateFlow.value = value
            }

        fun select() {
            selectedLayer.layer.selected = false
            selected = true
            selectedLayer.layer = this
        }

        fun duplicate() {
            val layers = layersStateFlow.value
            val index = layers.indexOfFirst { it.key == key }

            val duplicatedLayer = Layer()
            duplicatedLayer._strokes = _strokes.copy()
            duplicatedLayer.select()

            _layersStateFlow.value =
                layers.subList(0, index) +
                        duplicatedLayer +
                        layers.subList(index, layers.size)

            duplicatedLayer.select()
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

        fun remove() {
            val layers = layersStateFlow.value

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

        fun undo() {
           strokes.undo()
        }
    }
}

fun emptyLayer(): Layers.Layer {

    val layers = Layers()
    return layers.addAndSelect()
}