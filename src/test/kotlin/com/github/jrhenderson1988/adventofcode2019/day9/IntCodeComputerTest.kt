package com.github.jrhenderson1988.adventofcode2019.day9

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
        const val OUTPUT_SELF = "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99"
    }

    @Test
    fun `Execute properly executes program with input 1`() =
        mapOf("1002,4,3,4,33" to null, "3,3,1101,99,99,1,4,1,99" to 100L)
            .forEach { (input, expected) ->
                assertEquals(expected, IntCodeComputer(input, listOf(1L)).execute())
            }

    @Test
    fun execute() =
        mapOf(
            Pair(INPUT_EQUALS_8_POSITION_MODE, listOf(8L)) to 1L,
            Pair(INPUT_EQUALS_8_POSITION_MODE, listOf(7L)) to 0L,
            Pair(INPUT_LESS_THAN_8_POSITION_MODE, listOf(7L)) to 1L,
            Pair(INPUT_LESS_THAN_8_POSITION_MODE, listOf(9L)) to 0L,
            Pair(INPUT_EQUALS_8_IMMEDIATE_MODE, listOf(8L)) to 1L,
            Pair(INPUT_EQUALS_8_IMMEDIATE_MODE, listOf(5L)) to 0L,
            Pair(INPUT_LESS_THAN_8_IMMEDIATE_MODE, listOf(5L)) to 1L,
            Pair(INPUT_LESS_THAN_8_IMMEDIATE_MODE, listOf(9L)) to 0L,
            Pair(INPUT_NON_ZERO_WITH_JUMPS_POSITION_MODE, listOf(8L)) to 1L,
            Pair(INPUT_NON_ZERO_WITH_JUMPS_POSITION_MODE, listOf(0L)) to 0L,
            Pair(INPUT_NON_ZERO_WITH_JUMPS_IMMEDIATE_MODE, listOf(8L)) to 1L,
            Pair(INPUT_NON_ZERO_WITH_JUMPS_IMMEDIATE_MODE, listOf(0L)) to 0L,
            Pair(OUTPUT_999_IF_INPUT_LESS_THAN_8_1000_IF_EQUALS_8_1001_IF_GREATER_THAN_8, listOf(7L)) to 999L,
            Pair(OUTPUT_999_IF_INPUT_LESS_THAN_8_1000_IF_EQUALS_8_1001_IF_GREATER_THAN_8, listOf(8L)) to 1000L,
            Pair(OUTPUT_999_IF_INPUT_LESS_THAN_8_1000_IF_EQUALS_8_1001_IF_GREATER_THAN_8, listOf(9L)) to 1001L
        )
            .forEach { (input, expected) ->
                assertEquals(expected, IntCodeComputer(input.first, input.second).execute())
            }

    @Test
    fun complete() =
        mapOf(
            Pair(OUTPUT_SELF, emptyList<Long>()) to OUTPUT_SELF.split(',').map { it.toLong() },
            Pair("1102,200,200,7,4,7,99,0", emptyList<Long>()) to listOf(40000L),
            Pair("104,123456789,99", emptyList<Long>()) to listOf(123456789L),
            Pair("104,1125899906842624,99", emptyList<Long>()) to listOf(1125899906842624L),
            Pair("1102,34915192,34915192,7,4,7,99,0", emptyList<Long>()) to listOf(34915192L * 34915192L)
        )
            .forEach { (input, expected) ->
                assertEquals(expected, IntCodeComputer(input.first, input.second).complete())
            }
}