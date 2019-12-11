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