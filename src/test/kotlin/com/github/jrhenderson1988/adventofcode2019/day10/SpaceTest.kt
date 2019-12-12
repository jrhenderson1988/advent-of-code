package com.github.jrhenderson1988.adventofcode2019.day10

import org.junit.Assert.assertEquals
import org.junit.Test

class SpaceTest {
    companion object {
        const val TEST_GRID = ".#..#\n.....\n#####\n....#\n...##"
    }

    @Test
    fun maximumVisibleAsteroidsFromBestAsteroid() =
        mapOf(TEST_GRID to 8)
            .forEach { (grid, expected) ->
                assertEquals(
                    expected,
                    Space(grid).maximumVisibleAsteroidsFromBestAsteroid()
                )
            }

    @Test
    fun parseAsteroidCoordinates() =
        mapOf(
            TEST_GRID to setOf(
                Pair(1, 0),
                Pair(4, 0),
                Pair(0, 2),
                Pair(1, 2),
                Pair(2, 2),
                Pair(3, 2),
                Pair(4, 2),
                Pair(4, 3),
                Pair(3, 4),
                Pair(4, 4)
            )
        ).forEach { (input, expected) ->
            assertEquals(expected, Space.parseAsteroidCoordinates(input))
        }

    @Test
    fun calculateTotalAsteroidsVisibleFromEachAsteroid() =
        mapOf(
            TEST_GRID to mapOf(
                Pair(1, 0) to 7,
                Pair(4, 0) to 7,
                Pair(0, 2) to 6,
                Pair(1, 2) to 7,
                Pair(2, 2) to 7,
                Pair(3, 2) to 7,
                Pair(4, 2) to 5,
                Pair(4, 3) to 7,
                Pair(3, 4) to 8,
                Pair(4, 4) to 7
            )
        ).forEach { (grid, expected) ->
            assertEquals(expected, Space(grid).calculateTotalAsteroidsVisibleFromEachAsteroid())
        }

    @Test
    fun totalAsteroidsVisibleFrom() =
        mapOf(
            Pair(TEST_GRID, Pair(1, 0)) to 7,
            Pair(TEST_GRID, Pair(4, 0)) to 7,
            Pair(TEST_GRID, Pair(0, 2)) to 6,
            Pair(TEST_GRID, Pair(1, 2)) to 7,
            Pair(TEST_GRID, Pair(2, 2)) to 7,
            Pair(TEST_GRID, Pair(3, 2)) to 7,
            Pair(TEST_GRID, Pair(4, 2)) to 5,
            Pair(TEST_GRID, Pair(4, 3)) to 7,
            Pair(TEST_GRID, Pair(3, 4)) to 8,
            Pair(TEST_GRID, Pair(4, 4)) to 7
        ).forEach { (input, expected) ->
            assertEquals(expected, Space(input.first).totalAsteroidsVisibleFrom(input.second))
        }

    @Test
    fun isAsteroidVisible() =
        mapOf(
            Pair(TEST_GRID, Pair(Pair(1, 0), Pair(4, 0))) to true,
            Pair(TEST_GRID, Pair(Pair(1, 0), Pair(3, 4))) to false,
            Pair(TEST_GRID, Pair(Pair(1, 0), Pair(4, 4))) to true,
            Pair(TEST_GRID, Pair(Pair(2, 2), Pair(3, 2))) to true,
            Pair(TEST_GRID, Pair(Pair(2, 2), Pair(4, 2))) to false,
            Pair(TEST_GRID, Pair(Pair(2, 2), Pair(4, 3))) to true,
            Pair(TEST_GRID, Pair(Pair(2, 2), Pair(0, 2))) to false
        ).forEach { (input, expected) ->
            assertEquals(expected, Space(input.first).isAsteroidVisible(input.second.first, input.second.second))
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
            assertEquals(expected, Space.pointsBetween(input.first, input.second))
        }

    @Test
    fun hcf() =
        mapOf(Pair(3, 6) to 3, Pair(4, 8) to 4, Pair(12, 8) to 4, Pair(12, 9) to 3, Pair(11, 23) to 1)
            .forEach { (input, expected) -> assertEquals(expected, Space.hcf(input.first, input.second)) }

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
            assertEquals(expected, Space.delta(input.first, input.second))
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
            assertEquals(expected, Space.angle(input.first, input.second), 0.1)
        }

    @Test
    fun calculateNthDestroyedAsteroid() =
        mapOf(
            Pair(TEST_GRID, 0) to Pair(3, 2),
            Pair(TEST_GRID, 1) to Pair(4, 0),
            Pair(TEST_GRID, 2) to Pair(4, 2),
            Pair(TEST_GRID, 3) to Pair(4, 3),
            Pair(TEST_GRID, 4) to Pair(4, 4),
            Pair(TEST_GRID, 5) to Pair(0, 2),
            Pair(TEST_GRID, 6) to Pair(1, 2),
            Pair(TEST_GRID, 7) to Pair(2, 2),
            Pair(TEST_GRID, 8) to Pair(1, 0)
        ).forEach { (input, expected) ->
            assertEquals(expected, Space(input.first).calculateNthDestroyedAsteroid(input.second))
        }
}