package com.github.jrhenderson1988.adventofcode2019.day8

class Layer(val pixels: List<Int>, val width: Int, val height: Int) {
    override fun toString() = "Layer[${width} x ${height}, ${pixels}]"
}