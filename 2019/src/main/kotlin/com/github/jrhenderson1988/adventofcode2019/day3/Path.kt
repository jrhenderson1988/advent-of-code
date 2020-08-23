package com.github.jrhenderson1988.adventofcode2019.day3

class Path(val direction: Direction, val amount: Int) {
    fun nextCoordinates(start: Pair<Int, Int>) =
        (1..amount).map { i ->
            Pair(
                start.first + (direction.mask.first * i),
                start.second + (direction.mask.second * i)
            )
        }

    companion object {
        fun parse(command: String) = Path(Direction.valueOf(command.substring(0, 1)), command.substring(1).toInt())
    }
}