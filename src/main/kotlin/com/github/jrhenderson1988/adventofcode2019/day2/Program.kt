package com.github.jrhenderson1988.adventofcode2019.day2

import java.lang.IllegalArgumentException

class Program(private val input: List<Int>) {
    fun execute(): List<Int> {
        var pointer = 0
        val instructions = input.toMutableList()

        while (true) {
            val opcode = instructions[pointer]
            if (opcode == 99) {
                break
            }

            val a = instructions[pointer + 1]
            val b = instructions[pointer + 2]
            val result = instructions[pointer + 3]

            instructions[result] = when (opcode) {
                1 -> instructions[a] + instructions[b]
                2 -> instructions[a] * instructions[b]
                else -> throw IllegalArgumentException("Unexpected opcode [$opcode]")
            }

            pointer += 4
        }

        return instructions.toList()
    }
}