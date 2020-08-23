package com.github.jrhenderson1988.adventofcode2019

import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsTest {
    @Test
    fun manhattanDistance() =
        mapOf(
            Pair(Pair(0, 0), Pair(3, 3)) to 6,
            Pair(Pair(1, 2), Pair(3, 3)) to 3,
            Pair(Pair(-1, 2), Pair(3, 3)) to 5
        ).forEach { (input, expected) ->
            assertEquals(expected, manhattanDistance(input.first, input.second))
        }

    @Test
    fun hcf() =
        mapOf(Pair(3, 6) to 3, Pair(4, 8) to 4, Pair(12, 8) to 4, Pair(12, 9) to 3, Pair(11, 23) to 1)
            .forEach { (input, expected) -> assertEquals(expected, hcf(input.first, input.second)) }

    @Test
    fun delta() =
        mapOf(
            Pair(Pair(4, 4), Pair(5, 5)) to Pair(1, 1),
            Pair(Pair(0, 0), Pair(0, 2)) to Pair(0, 1),
            Pair(Pair(1, 2), Pair(4, 5)) to Pair(1, 1),
            Pair(Pair(0, 4), Pair(4, 0)) to Pair(1, -1),
            Pair(Pair(0, 0), Pair(2, 4)) to Pair(1, 2),
            Pair(Pair(0, 1), Pair(4, 9)) to Pair(1, 2),
            Pair(Pair(1, 8), Pair(-3, 14)) to Pair(-2, 3)
        ).forEach { (input, expected) ->
            assertEquals(expected, delta(input.first, input.second))
        }

    @Test
    fun angle() =
        mapOf(
            Pair(Pair(2, 2), Pair(2, 1)) to 0.0,
            Pair(Pair(2, 2), Pair(3, 2)) to 90.0,
            Pair(Pair(2, 2), Pair(2, 3)) to 180.0,
            Pair(Pair(2, 2), Pair(1, 2)) to 270.0,
            Pair(Pair(1, 1), Pair(2, 0)) to 45.0,
            Pair(Pair(1, 1), Pair(2, 2)) to 135.0,
            Pair(Pair(1, 1), Pair(0, 2)) to 225.0,
            Pair(Pair(1, 1), Pair(0, 0)) to 315.0
        ).forEach { (input, expected) ->
            assertEquals(expected, angle(input.first, input.second), 0.1)
        }

    @Test
    fun pointsBetween() =
        mapOf(
            Pair(Pair(4, 4), Pair(5, 5)) to emptySet(),
            Pair(Pair(0, 0), Pair(0, 2)) to setOf(Pair(0, 1)),
            Pair(Pair(1, 2), Pair(4, 5)) to setOf(Pair(2, 3), Pair(3, 4)),
            Pair(Pair(0, 4), Pair(4, 0)) to setOf(Pair(1, 3), Pair(2, 2), Pair(3, 1)),
            Pair(Pair(0, 0), Pair(2, 4)) to setOf(Pair(1, 2)),
            Pair(Pair(0, 1), Pair(4, 9)) to setOf(Pair(1, 3), Pair(2, 5), Pair(3, 7)),
            Pair(Pair(2, 2), Pair(0, 2)) to setOf(Pair(1, 2))
        ).forEach { (input, expected) ->
            assertEquals(expected, pointsBetween(input.first, input.second))
        }
}