package com.github.jrhenderson1988.adventofcode2019.day9

class IntCodeComputer(program: String, originalInputs: List<Long>) {
    private val originalInstructions = parseIntoMap(program)
    private var instructions = originalInstructions.toMutableMap()
    private var inputs = originalInputs.toList()
    private var ip = 0L
    private var inputPointer = 0
    private var relativeBase = 0L
    private var terminated = false
    private var paused = false
    private val outputs = mutableListOf<Long>()

    fun complete(): List<Long?> {
        while (!terminated) {
            execute()
        }

        return outputs
    }

    fun execute(): Long? {
        paused = false
        while (!terminated && !paused) {
            val opCode = OpCode(get(ip).toInt())

            var nextPointer: Long? = null
            when (opCode.instruction) {
                OpCode.ADD -> set(outputIndex(opCode), value(opCode, 0) + value(opCode, 1))
                OpCode.MULTIPLY -> set(outputIndex(opCode), value(opCode, 0) * value(opCode, 1))
                OpCode.INPUT -> set(outputIndex(opCode), nextInput())
                OpCode.OUTPUT -> {
                    outputs.add(value(opCode, 0))
                    paused = true
                }
                OpCode.JUMP_IF_TRUE -> nextPointer = if (value(opCode, 0) != 0L) value(opCode, 1) else null
                OpCode.JUMP_IF_FALSE -> nextPointer = if (value(opCode, 0) == 0L) value(opCode, 1) else null
                OpCode.LESS_THAN -> set(outputIndex(opCode), if (value(opCode, 0) < value(opCode, 1)) 1 else 0)
                OpCode.EQUAL_TO -> set(outputIndex(opCode), if (value(opCode, 0) == value(opCode, 1)) 1 else 0)
                OpCode.RELATIVE_BASE_OFFSET -> relativeBase += value(opCode, 0)
                OpCode.TERMINATE -> terminated = true
            }

            ip = nextPointer ?: ip + opCode.argumentCount + 1
        }

        return if (outputs.isEmpty()) null else outputs.last()
    }

    private fun nextInput() = inputs[inputPointer++]

    private fun value(opCode: OpCode, argument: Int) =
        when (opCode.modes[argument]) {
            OpCode.IMMEDIATE -> get(ip + argument + 1)
            OpCode.POSITIONAL -> get(get(ip + argument + 1))
            OpCode.RELATIVE -> get(relativeBase + get(ip + argument + 1))
            else -> error("Invalid parameter mode for OpCode [$opCode] and argument [$argument].")
        }

    private fun get(position: Long) =
        if (position >= 0) instructions.getOrDefault(position, 0L) else error("Out of bounds")

    private fun set(position: Long, value: Long) =
        if (position < 0) {
            error("Out of bounds")
        } else {
            instructions[position] = value
        }

    private fun outputIndex(opCode: OpCode) = when (opCode.modes[opCode.argumentCount - 1]) {
        OpCode.POSITIONAL -> get(ip + opCode.argumentCount)
        OpCode.RELATIVE -> relativeBase + get(ip + opCode.argumentCount)
        else -> error("Only positional and relative OpCodes can be used to write data")
    }

    companion object {
        fun parseIntoMap(input: String) =
            input.split(',')
                .mapIndexed { index, value -> index.toLong() to value.trim().toLong() }
                .toMap()
    }
}