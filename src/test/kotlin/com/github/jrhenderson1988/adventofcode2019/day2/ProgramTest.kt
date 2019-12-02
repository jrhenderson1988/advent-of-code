package com.github.jrhenderson1988.adventofcode2019.day2

import org.junit.Assert.assertEquals
import org.junit.Test

class ProgramTest {
    @Test
    fun `Program outputs correct values`() {
        mapOf(
            listOf(1, 0, 0, 0, 99) to listOf(2, 0, 0, 0, 99),
            listOf(2, 3, 0, 3, 99) to listOf(2, 3, 0, 6, 99),
            listOf(2, 4, 4, 5, 99, 0) to listOf(2, 4, 4, 5, 99, 9801),
            listOf(1, 1, 1, 4, 99, 5, 6, 0, 99) to listOf(30, 1, 1, 4, 2, 5, 6, 0, 99)
        ).forEach { (input, expectedOutput) ->
            assertEquals(expectedOutput, Program(input).execute())
        }
    }
}