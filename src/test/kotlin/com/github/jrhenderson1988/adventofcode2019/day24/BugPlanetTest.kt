package com.github.jrhenderson1988.adventofcode2019.day24

import org.junit.Assert.assertEquals
import org.junit.Test

class BugPlanetTest {
    companion object {
        const val A =
            """
            ....#
            #..#.
            #..##
            ..#..
            #....
            """
    }

    @Test
    fun biodiversityRatingOfFirstRepetition() =
        mapOf(A to 2129920L)
            .forEach { (input, expected) ->
                assertEquals(expected, BugPlanet.parse(input.trimIndent().trim()).biodiversityRatingOfFirstRepetition())
            }

    @Test
    fun calculateTotalBugsAfterMinutes() =
        mapOf(/*Pair(A, 0) to 8, */Pair(A, 10) to 99)
            .forEach { (input, expected) ->
                assertEquals(
                    expected,
                    BugUniverse.parse(input.first.trimIndent().trim()).calculateTotalBugsAfterMinutes(input.second)
                )
            }

    @Test
    fun neighboursOf() =
        mapOf(
            Triple(1, 1, 0) to setOf(Triple(0, 1, 0), Triple(1, 0, 0), Triple(2, 1, 0), Triple(1, 2, 0)),
            Triple(0, 1, 0) to setOf(Triple(0, 0, 0), Triple(1, 1, 0), Triple(0, 2, 0), Triple(1, 2, -1)),
            Triple(0, 0, 0) to setOf(Triple(1, 0, 0), Triple(0, 1, 0), Triple(2, 1, -1), Triple(1, 2, -1)),
            Triple(4, 4, 1) to setOf(Triple(3, 4, 1), Triple(4, 3, 1), Triple(3, 2, 0), Triple(2, 3, 0)),
            Triple(2, 1, 0) to setOf(
                Triple(2, 0, 0),
                Triple(1, 1, 0),
                Triple(3, 1, 0),
                Triple(0, 0, 1),
                Triple(1, 0, 1),
                Triple(2, 0, 1),
                Triple(3, 0, 1),
                Triple(4, 0, 1)
            ),
            Triple(1, 2, 2) to setOf(
                Triple(1, 1, 2),
                Triple(1, 3, 2),
                Triple(0, 2, 2),
                Triple(0, 0, 3),
                Triple(0, 1, 3),
                Triple(0, 2, 3),
                Triple(0, 3, 3),
                Triple(0, 4, 3)
            ),
            Triple(2, 3, 2) to setOf(
                Triple(1, 3, 2),
                Triple(2, 4, 2),
                Triple(3, 3, 2),
                Triple(0, 4, 3),
                Triple(1, 4, 3),
                Triple(2, 4, 3),
                Triple(3, 4, 3),
                Triple(4, 4, 3)
            ),
            Triple(3, 2, 2) to setOf(
                Triple(3, 1, 2),
                Triple(4, 2, 2),
                Triple(3, 3, 2),
                Triple(4, 0, 3),
                Triple(4, 1, 3),
                Triple(4, 2, 3),
                Triple(4, 3, 3),
                Triple(4, 4, 3)
            )
        ).forEach { (input, expected) ->
            assertEquals(expected, BugUniverse.parse(A.trimIndent().trim()).neighboursOf(input))
        }
}