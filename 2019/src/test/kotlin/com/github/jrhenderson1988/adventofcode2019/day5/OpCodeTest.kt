package com.github.jrhenderson1988.adventofcode2019.day5

import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import java.lang.IllegalArgumentException

class OpCodeTest {
    @Test
    fun `Correctly parses instruction`() =
        mapOf(
            10001 to OpCode.ADD,
            1 to OpCode.ADD,
            102 to OpCode.MULTIPLY,
            99 to OpCode.TERMINATE,
            3 to OpCode.INPUT,
            4 to OpCode.OUTPUT
        ).forEach { (input, expected) ->
            assertEquals(expected, OpCode(input).instruction)
        }

    @Test
    fun `Returns correct number of arguments`() =
        mapOf(10001 to 3, 1 to 3, 102 to 3, 99 to 0, 3 to 1, 4 to 1)
            .forEach { (input, expected) ->
                assertEquals(expected, OpCode(input).argumentCount)
            }

    @Test
    fun `Returns correct argument mode`() =
        mapOf(
            Pair(1001, 0) to OpCode.POSITIONAL,
            Pair(1001, 1) to OpCode.IMMEDIATE,
            Pair(1001, 2) to OpCode.POSITIONAL,
            Pair(1, 0) to OpCode.POSITIONAL,
            Pair(1102, 0) to OpCode.IMMEDIATE,
            Pair(1102, 1) to OpCode.IMMEDIATE,
            Pair(1102, 2) to OpCode.POSITIONAL,
            Pair(3, 0) to OpCode.POSITIONAL,
            Pair(4, 0) to OpCode.POSITIONAL
        ).forEach { (input, expected) ->
            assertEquals(expected, OpCode(input.first).modes[input.second])
        }

    @Test
    fun `Throws IllegalArgumentException when input is too long or too short`() =
        listOf(100000, 100001, 0, -1).forEach {
            try {
                OpCode(it)
                fail("IllegalArgumentException was not thrown for input [$it]")
            } catch (ex: IllegalArgumentException) {
            }
        }


    @Test
    fun `Throws IllegalArgumentException when OpCode contains invalid argument modes`() =
        listOf(1201, 21111, 901, 99999).forEach {
            try {
                OpCode(it)
            } catch (ex: IllegalArgumentException) {
            }
        }

    @Test
    fun `Throws IllegalArgumentException when OpCode contains invalid instruction`() =
        listOf(5, 1000, 11, 1112).forEach {
            try {
                OpCode(it)
            } catch (ex: IllegalArgumentException) {
            }
        }
}