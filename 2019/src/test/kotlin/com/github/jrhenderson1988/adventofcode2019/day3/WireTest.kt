package com.github.jrhenderson1988.adventofcode2019.day3

import org.junit.Assert.assertEquals
import org.junit.Test

class WireTest {
    @Test
    fun `calculatePoints generates a list of points after following all paths from 0,0`() =
        mapOf(
            listOf(Path(Direction.U, 2), Path(Direction.R, 1), Path(Direction.U, 1)) to listOf(
                Pair(0, 0),
                Pair(0, 1),
                Pair(0, 2),
                Pair(1, 2),
                Pair(1, 3)
            ),
            listOf(Path(Direction.L, 1), Path(Direction.D, 1), Path(Direction.R, 3)) to listOf(
                Pair(0, 0),
                Pair(-1, 0),
                Pair(-1, -1),
                Pair(0, -1),
                Pair(1, -1),
                Pair(2, -1)
            )
        ).forEach { (input, expected) ->
            assertEquals(expected, Wire(input).calculatePoints())
        }
}