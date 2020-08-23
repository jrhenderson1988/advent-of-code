package com.github.jrhenderson1988.adventofcode2019.day3

class Wire(val path: List<Path>) {
    fun calculatePoints() : List<Pair<Int, Int>> {
        return path.fold(listOf(Pair(0, 0))) { points, path ->
            listOf(points, path.nextCoordinates(points.last())).flatten()
        }
    }

    override fun toString(): String {
        return path.toString()
    }
}