package com.github.jrhenderson1988.adventofcode2019.day8

class Image(data: String, private val width: Int, private val height: Int) {
    private val layers = buildLayers(data, width, height)

    fun calculateChecksum(): Int {
        val targetLayer = layers.minBy { it.filter { pixel -> pixel == 0 }.size } ?: error("No valid layers")

        return targetLayer.filter { it == 1 }.size * targetLayer.filter { it == 2 }.size
    }

    fun render(renderPixels: Boolean = true) =
        stackLayers()
            .chunked(width)
            .joinToString("\n") {
                it.joinToString("") { p -> if (renderPixels) renderPixel(p) else p.toString() }
            }

    private fun renderPixel(pixel: Int) =
        when (pixel) {
            TRANSPARENT -> " "
            BLACK -> "#"
            WHITE -> "-"
            else -> error("Invalid pixel")
        }

    private fun stackLayers() =
        layers.reduce { acc, layer -> (0 until width * height).map { multiply(acc[it], layer[it]) } }

    private fun multiply(first: Int, second: Int) =
        when (first) {
            TRANSPARENT -> second
            else -> first
        }

    companion object {
        const val WHITE = 0
        const val BLACK = 1
        const val TRANSPARENT = 2

        fun buildLayers(data: String, width: Int, height: Int) =
            data
                .map { it.toString().toInt() }
                .chunked(width * height)
    }
}