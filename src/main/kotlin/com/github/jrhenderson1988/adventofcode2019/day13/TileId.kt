package com.github.jrhenderson1988.adventofcode2019.day13

enum class TileId(val id: Int) {
    EMPTY(0),
    WALL(1),
    BLOCK(2),
    PADDLE(3),
    BALL(4);

    companion object {
        fun fromValue(value: Int) = values().find { it.id == value }
    }
}