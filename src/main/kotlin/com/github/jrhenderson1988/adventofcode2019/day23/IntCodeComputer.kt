package com.github.jrhenderson1988.adventofcode2019.day23

class IntCodeComputer(program: List<Long>) {
    private val originalInstructions = program.mapIndexed { index, value -> index.toLong() to value }.toMap()
    var instructions = originalInstructions.toMutableMap()
    private var ip = 0L
    private var relativeBase = 0L
    val outputs = mutableListOf<Long>()
    var terminated = false
    var paused = false
    private val inputs = mutableListOf<Long>()
    var inputReceiver: (() -> Long)? = null

    private fun queueInput(value: Long) {
        inputs.add(value)
    }

    fun dequeueInput(): Long {
        val value = inputs[0]
        inputs.removeAt(0)
        return value
    }

    private fun queueOutput(value: Long) {
        outputs.add(value)
    }

    fun dequeueOutput(): Long? {
        val value = if (outputs.size > 0) outputs[0] else null
        if (value == null) {
            outputs.removeAt(0)
        }

        return value
    }

    fun execute(input: Long? = null): Long? {
        paused = false
        while (!terminated && !paused) {
            val opCode = OpCode(get(ip).toInt())

            var nextPointer: Long? = null
            when (opCode.instruction) {
                OpCode.ADD -> set(outputIndex(opCode), value(opCode, 0) + value(opCode, 1))
                OpCode.MULTIPLY -> set(outputIndex(opCode), value(opCode, 0) * value(opCode, 1))
                OpCode.INPUT -> {
                    set(
                        outputIndex(opCode),
                        when {
                            input != null -> input
                            inputReceiver != null -> inputReceiver!!()
                            inputs.isNotEmpty() -> dequeueInput()
                            else -> error("Input was required but no input or inputReceiver was provided")
                        }
                    )
                    paused = true
                }
                OpCode.OUTPUT -> queueOutput(value(opCode, 0))
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

    class OpCode(input: Int) {
        init {
            if (input > 99999 || input < 1) {
                error("OpCode is invalid.")
            }
        }

        private val opCode = input.toString().padStart(5, '0')

        val instruction = opCode.substring(3).toInt()

        val argumentCount = when (instruction) {
            ADD, MULTIPLY, LESS_THAN, EQUAL_TO -> 3
            INPUT, OUTPUT, RELATIVE_BASE_OFFSET -> 1
            JUMP_IF_TRUE, JUMP_IF_FALSE -> 2
            TERMINATE -> 0
            else -> throw IllegalArgumentException("Invalid OpCode [$opCode]")
        }

        val modes = opCode.substring(0, 3).reversed().map { it.toString().toInt() }.onEach {
            if (!listOf(POSITIONAL, IMMEDIATE, RELATIVE).contains(it)) {
                error("Invalid argument mode found [$it]")
            }


        }

        override fun toString() = "OpCode[$opCode]"

        companion object {
            const val ADD = 1
            const val MULTIPLY = 2
            const val INPUT = 3
            const val OUTPUT = 4
            const val JUMP_IF_TRUE = 5
            const val JUMP_IF_FALSE = 6
            const val LESS_THAN = 7
            const val EQUAL_TO = 8
            const val RELATIVE_BASE_OFFSET = 9
            const val TERMINATE = 99

            const val POSITIONAL = 0
            const val IMMEDIATE = 1
            const val RELATIVE = 2
        }
    }

    companion object {
        fun createFromString(program: String) = IntCodeComputer(parseProgram(program))

        fun parseProgram(program: String) = program.split(',').map { it.trim().toLong() }
    }
}