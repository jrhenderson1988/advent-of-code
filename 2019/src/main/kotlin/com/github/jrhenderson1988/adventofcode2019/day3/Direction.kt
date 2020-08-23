package com.github.jrhenderson1988.adventofcode2019.day3

enum class Direction(val mask: Pair<Int, Int>) {
    U(Pair(0, 1)),
    D(Pair(0, -1)),
    L(Pair(-1, 0)),
    R(Pair(1, 0))
}