package com.github.jrhenderson1988.adventofcode2019.day5

import org.junit.Assert.assertEquals
import org.junit.Test

class IntCodeComputerTest {
    @Test
    fun `Execute properly executes program with input 1`() =
        mapOf("1002,4,3,4,33" to emptyList(), "3,3,1101,99,99,1,4,1,99" to listOf(100))
            .forEach { (input, expected) ->
                assertEquals(expected, IntCodeComputer(input).execute(1))
            }

    @Test
    fun execute() =
        mapOf(
            Pair("3,9,8,9,10,9,4,9,99,-1,8", 8) to listOf(1), // 1 if input == 8
            Pair("3,9,8,9,10,9,4,9,99,-1,8", 7) to listOf(0), // 0 if input != 8
            Pair("3,9,7,9,10,9,4,9,99,-1,8", 7) to listOf(1), // 1 if input < 8
            Pair("3,9,7,9,10,9,4,9,99,-1,8", 9) to listOf(0), // 1 if input >= 8
            Pair("3,3,1108,-1,8,3,4,3,99", 8) to listOf(1), // 1 if input == 8
            Pair("3,3,1108,-1,8,3,4,3,99", 5) to listOf(0), // 0 if input != 8
            Pair("3,3,1107,-1,8,3,4,3,99", 5) to listOf(1), // 1 if input < 8
            Pair("3,3,1107,-1,8,3,4,3,99", 9) to listOf(0), // 0 if input >= 8
            Pair("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9", 8) to listOf(1), // 1 if input != 0 (Jumping)
            Pair("3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9", 0) to listOf(0), // 0 if input == 0 (Jumping)
            Pair("3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99", 7) to listOf(999), // 999 if input < 8
            Pair("3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99", 8) to listOf(1000), // 1000 if input == 8
            Pair("3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99", 9) to listOf(1001) // 1000 if input > 8
        )
            .forEach { (input, expected) ->
                assertEquals(expected, IntCodeComputer(input.first).execute(input.second))
            }
}