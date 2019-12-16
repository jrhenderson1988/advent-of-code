package com.github.jrhenderson1988.adventofcode2019.day16

import org.junit.Assert.assertEquals
import org.junit.Test

class FFTTest {
    @Test
    fun applyPhases() =
        mapOf(Pair(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9), 4) to listOf(0, 1, 0, 2, 9, 4, 9, 8))
            .forEach { (input, expected) ->
                assertEquals(expected, FFT(input.first).applyPhases(input.second))
            }
}