package com.github.jrhenderson1988.adventofcode2019.day7

class Amplifier(program: String, private val phase: Int) {
    private val instructions = parseIntoList(program).toMutableList()
    private var initialised = false
    private var ip = 0
    var lastOutput: Int = -1
    var terminated = false

    // Phase is the amps initial input
    // Signal is the second input which it receives as either 0 (first amp) or from a previous amp's output
    // The phase should never change, but the signal passed to the execute method can be different each time
    // The execution must block and wait for the next input if it sees another "3" opcode but can continue otherwise
    // Maybe keep track of a "terminated" flag so that the execute method always just returns again with its previous
    //  output if it is ever called again (it shouldn't be)
    //

    fun execute(signal: Int): Int {
        if (terminated) {
            return lastOutput
        }

        var halt = false
        while (!halt) {
            val opCode = OpCode(instructions[ip])

            var nextPointer: Int? = null
            when (opCode.instruction) {
                OpCode.ADD -> instructions[outputIndex(opCode, ip, instructions)] =
                    value(opCode, 0, ip, instructions) + value(opCode, 1, ip, instructions)
                OpCode.MULTIPLY -> instructions[outputIndex(opCode, ip, instructions)] =
                    value(opCode, 0, ip, instructions) * value(opCode, 1, ip, instructions)
                OpCode.INPUT -> {
                    // The first time the computer runs it should accept the phase as its input. Every other time it
                    // runs it should accept the input signal as its input
                    instructions[outputIndex(opCode, ip, instructions)] = if (!initialised) phase else signal
                    initialised = true
                }
                OpCode.OUTPUT -> {
                    lastOutput = value(opCode, 0, ip, instructions)
                    halt = true
                }
                OpCode.JUMP_IF_TRUE -> nextPointer =
                    if (value(opCode, 0, ip, instructions) != 0) value(opCode, 1, ip, instructions) else null
                OpCode.JUMP_IF_FALSE -> nextPointer =
                    if (value(opCode, 0, ip, instructions) == 0) value(opCode, 1, ip, instructions) else null
                OpCode.LESS_THAN -> instructions[outputIndex(opCode, ip, instructions)] =
                    if (value(opCode, 0, ip, instructions) < value(opCode, 1, ip, instructions)) 1 else 0
                OpCode.EQUAL_TO -> instructions[outputIndex(opCode, ip, instructions)] =
                    if (value(opCode, 0, ip, instructions) == value(opCode, 1, ip, instructions)) 1 else 0
                OpCode.TERMINATE -> {
                    terminated = true
                    halt = true
                }
            }

            ip = nextPointer ?: ip + opCode.argumentCount + 1
        }

        return lastOutput
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