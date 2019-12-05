package com.github.jrhenderson1988.adventofcode2019.day5

import java.lang.IllegalArgumentException

class OpCode(input: Int) {
    init {
        if (input > 99999 || input < 1) {
            throw IllegalArgumentException("OpCode is invalid.")
        }
    }

    private val opCode = input.toString().padStart(5, '0')

    val instruction = opCode.substring(3).toInt()

    val argumentCount = when (instruction) {
        ADD, MULTIPLY, LESS_THAN, EQUAL_TO -> 3
        INPUT, OUTPUT -> 1
        JUMP_IF_TRUE, JUMP_IF_FALSE -> 2
        TERMINATE -> 0
        else -> throw IllegalArgumentException("Invalid OpCode [$opCode]")
    }

    val modes = opCode.substring(0, 3).reversed().map { it.toString().toInt() }.onEach {
        if (it < 0 || it > 1) {
            throw IllegalArgumentException("Invalid argument found [$it]")
        }
    }

    override fun toString(): String {
        return "OpCode [instruction=$instruction, argumentCount=$argumentCount, modes=$modes]"
    }

    companion object {
        const val ADD = 1
        const val MULTIPLY = 2
        const val INPUT = 3
        const val OUTPUT = 4
        const val JUMP_IF_TRUE = 5
        const val JUMP_IF_FALSE = 6
        const val LESS_THAN = 7
        const val EQUAL_TO = 8
        const val TERMINATE = 99

        const val POSITIONAL = 0
        const val IMMEDIATE = 1
    }
}