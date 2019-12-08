package com.github.jrhenderson1988.adventofcode2019.day8

class Image(data: String, width: Int, height: Int) {
    private val layers = buildLayers(data, width, height)

    fun calculateChecksum(): Int {
        val targetLayer = layers.minBy { it.pixels.filter { pixel -> pixel == 0 }.size } ?: error("No valid layers")

        return targetLayer.pixels.filter { it == 1 }.size * targetLayer.pixels.filter { it == 2 }.size
    }

    companion object {
        fun buildLayers(data: String, width: Int, height: Int): List<Layer> {
            return data
                .map { it.toString().toInt() }
                .chunked(width * height)
                .map { Layer(it, width, height) }
        }
    }
}