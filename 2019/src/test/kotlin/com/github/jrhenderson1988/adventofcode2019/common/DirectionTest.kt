package com.github.jrhenderson1988.adventofcode2019.common

import org.junit.Assert.assertEquals
import org.junit.Test

class DirectionTest {
    @Test
    fun nextPoint() =
        mapOf(
            Pair(Direction.NORTH, Pair(0, 0)) to Pair(0, -1),
            Pair(Direction.SOUTH, Pair(0, 0)) to Pair(0, 1),
            Pair(Direction.EAST, Pair(0, 0)) to Pair(1, 0),
            Pair(Direction.WEST, Pair(0, 0)) to Pair(-1, 0),
            Pair(Direction.NORTH, Pair(2, 1)) to Pair(2, 0),
            Pair(Direction.SOUTH, Pair(2, 1)) to Pair(2, 2),
            Pair(Direction.EAST, Pair(2, 1)) to Pair(3, 1),
            Pair(Direction.WEST, Pair(2, 1)) to Pair(1, 1)
        ).forEach { (input, expected) ->
            val (direction, point) = input
            assertEquals(expected, direction.nextPoint(point))
        }

    @Test
    fun oppositeDirection() =
        mapOf(
            Direction.NORTH to Direction.SOUTH,
            Direction.SOUTH to Direction.NORTH,
            Direction.EAST to Direction.WEST,
            Direction.WEST to Direction.EAST
        ).forEach { (input, expected) ->
            assertEquals(expected, input.opposite())
        }
}