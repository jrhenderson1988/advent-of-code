package com.github.jrhenderson1988.adventofcode2019.day18

class Path(val path: Set<Pair<Int, Int>>, doors: Map<Char, Pair<Int, Int>>, keys: Map<Char, Pair<Int, Int>>) {
    val size = path.size
    val doors = doors.filter { it.value in path }.keys
    val keys = keys.filter { it.value in path }.keys

    override fun toString() = "Path[size=${size}, doors=$doors, keys=$keys]"
}