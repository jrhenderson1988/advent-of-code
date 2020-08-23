package com.github.jrhenderson1988.adventofcode2019.common

enum class Direction(val delta: Pair<Int, Int>) {
    NORTH(Pair(0, -1)),
    SOUTH(Pair(0, 1)),
    WEST(Pair(-1, 0)),
    EAST(Pair(1, 0));

    fun nextPoint(current: Pair<Int, Int>) = Pair(current.first + delta.first, current.second + delta.second)

    fun opposite() = when (this) {
        NORTH -> SOUTH
        SOUTH -> NORTH
        EAST -> WEST
        WEST -> EAST
    }

    companion object {
        fun neighboursOf(current: Pair<Int, Int>) = values().map { it.nextPoint(current) }
    }
}