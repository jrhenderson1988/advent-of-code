package com.github.jrhenderson1988.adventofcode2019.day5

import org.junit.Assert.assertEquals
import org.junit.Test

class IntCodeComputerTest {
    companion object {
        const val INPUT_EQUALS_8_POSITION_MODE = "3,9,8,9,10,9,4,9,99,-1,8"
        const val INPUT_LESS_THAN_8_POSITION_MODE = "3,9,7,9,10,9,4,9,99,-1,8"
        const val INPUT_EQUALS_8_IMMEDIATE_MODE = "3,3,1108,-1,8,3,4,3,99"
        const val INPUT_LESS_THAN_8_IMMEDIATE_MODE = "3,3,1107,-1,8,3,4,3,99"
        const val INPUT_NON_ZERO_WITH_JUMPS_POSITION_MODE = "3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9"
        const val INPUT_NON_ZERO_WITH_JUMPS_IMMEDIATE_MODE = "3,3,1105,-1,9,1101,0,0,12,4,12,99,1"
        const val OUTPUT_999_IF_INPUT_LESS_THAN_8_1000_IF_EQUALS_8_1001_IF_GREATER_THAN_8 =
            "3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0," +
                    "1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99"
    }

    @Test
    fun `Execute properly executes program with input 1`() =
        mapOf("1002,4,3,4,33" to emptyList(), "3,3,1101,99,99,1,4,1,99" to listOf(100))
            .forEach { (input, expected) ->
                assertEquals(expected, IntCodeComputer(input).execute(1))
            }

    @Test
    fun execute() =
        mapOf(
            Pair(INPUT_EQUALS_8_POSITION_MODE, 8) to listOf(1),
            Pair(INPUT_EQUALS_8_POSITION_MODE, 7) to listOf(0),
            Pair(INPUT_LESS_THAN_8_POSITION_MODE, 7) to listOf(1),
            Pair(INPUT_LESS_THAN_8_POSITION_MODE, 9) to listOf(0),
            Pair(INPUT_EQUALS_8_IMMEDIATE_MODE, 8) to listOf(1),
            Pair(INPUT_EQUALS_8_IMMEDIATE_MODE, 5) to listOf(0),
            Pair(INPUT_LESS_THAN_8_IMMEDIATE_MODE, 5) to listOf(1),
            Pair(INPUT_LESS_THAN_8_IMMEDIATE_MODE, 9) to listOf(0),
            Pair(INPUT_NON_ZERO_WITH_JUMPS_POSITION_MODE, 8) to listOf(1),
            Pair(INPUT_NON_ZERO_WITH_JUMPS_POSITION_MODE, 0) to listOf(0),
            Pair(INPUT_NON_ZERO_WITH_JUMPS_IMMEDIATE_MODE, 8) to listOf(1),
            Pair(INPUT_NON_ZERO_WITH_JUMPS_IMMEDIATE_MODE, 0) to listOf(0),
            Pair(OUTPUT_999_IF_INPUT_LESS_THAN_8_1000_IF_EQUALS_8_1001_IF_GREATER_THAN_8, 7) to listOf(999),
            Pair(OUTPUT_999_IF_INPUT_LESS_THAN_8_1000_IF_EQUALS_8_1001_IF_GREATER_THAN_8, 8) to listOf(1000),
            Pair(OUTPUT_999_IF_INPUT_LESS_THAN_8_1000_IF_EQUALS_8_1001_IF_GREATER_THAN_8, 9) to listOf(1001)
        )
            .forEach { (input, expected) ->
                assertEquals(expected, IntCodeComputer(input.first).execute(input.second))
            }
}