package com.github.jrhenderson1988.adventofcode2019.day3

import com.github.jrhenderson1988.adventofcode2019.day3.Direction.*
import org.junit.Assert.assertEquals
import org.junit.Test

class PathTest {
    @Test
    fun `Parse correctly created a Path instance`() =
        mapOf(
            "U21" to Path(U, 21),
            "D34" to Path(D, 34),
            "L1" to Path(L, 1),
            "R100" to Path(R, 100)
        )
            .forEach { (input, expectedPath) ->
                val actual = Path.parse(input)
                assertEquals(expectedPath.direction, actual.direction)
                assertEquals(expectedPath.amount, actual.amount)
            }

    @Test
    fun `nextCoordinates generates list of coordinates from given point`() =
        mapOf(
            Pair(Pair(0, 0), Path(R, 3)) to listOf(Pair(1, 0), Pair(2, 0), Pair(3, 0)),
            Pair(Pair(1, 1), Path(U, 2)) to listOf(Pair(1, 2), Pair(1, 3)),
            Pair(Pair(10, 2), Path(D, 3)) to listOf(Pair(10, 1), Pair(10, 0), Pair(10, -1))
        )
            .forEach { (input, expected) ->
                val (point, path) = input
                assertEquals(expected, path.nextCoordinates(point))
            }

}