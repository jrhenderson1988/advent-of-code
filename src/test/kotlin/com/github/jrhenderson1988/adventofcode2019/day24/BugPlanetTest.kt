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
}