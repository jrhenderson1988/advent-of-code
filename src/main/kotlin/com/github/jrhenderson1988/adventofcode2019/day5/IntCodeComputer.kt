package com.github.jrhenderson1988.adventofcode2019.day5

class IntCodeComputer(private val program: String) {
    fun execute(input: Int): List<Int> {
        var pointer = 0
        val instructions = parseIntoList(program).toMutableList()
        val output = mutableListOf<Int>()

        loop@ while (true) {
            val opCode = OpCode(instructions[pointer])

            var nextPointer: Int? = null
            when (opCode.instruction) {
                OpCode.ADD -> instructions[outputIndex(opCode, pointer, instructions)] =
                    value(opCode, 0, pointer, instructions) + value(opCode, 1, pointer, instructions)
                OpCode.MULTIPLY -> instructions[outputIndex(opCode, pointer, instructions)] =
                    value(opCode, 0, pointer, instructions) * value(opCode, 1, pointer, instructions)
                OpCode.INPUT -> instructions[outputIndex(opCode, pointer, instructions)] = input
                OpCode.OUTPUT -> output.add(value(opCode, 0, pointer, instructions))
                OpCode.JUMP_IF_TRUE -> nextPointer =
                    if (value(opCode, 0, pointer, instructions) != 0) value(opCode, 1, pointer, instructions) else null
                OpCode.JUMP_IF_FALSE -> nextPointer =
                    if (value(opCode, 0, pointer, instructions) == 0) value(opCode, 1, pointer, instructions) else null
                OpCode.LESS_THAN -> instructions[outputIndex(opCode, pointer, instructions)] =
                    if (value(opCode, 0, pointer, instructions) < value(opCode, 1, pointer, instructions)) 1 else 0
                OpCode.EQUAL_TO -> instructions[outputIndex(opCode, pointer, instructions)] =
                    if (value(opCode, 0, pointer, instructions) == value(opCode, 1, pointer, instructions)) 1 else 0
                OpCode.TERMINATE -> break@loop
            }

            pointer = nextPointer ?: pointer + opCode.argumentCount + 1
        }

        return output.toList()
    }

    companion object {
        fun parseIntoList(input: String) = input.split(',').map { it.trim().toInt() }

        private fun value(opCode: OpCode, argument: Int, pointer: Int, instructions: List<Int>): Int {
            return if (opCode.modes[argument] == OpCode.IMMEDIATE) {
                instructions[pointer + argument + 1]
            } else {
                instructions[instructions[pointer + argument + 1]]
            }
        }
        
        private fun outputIndex(opCode: OpCode, pointer: Int, instructions: List<Int>) =
            instructions[pointer + opCode.argumentCount]
    }
}