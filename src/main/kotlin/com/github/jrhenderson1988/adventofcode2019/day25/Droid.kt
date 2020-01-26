package com.github.jrhenderson1988.adventofcode2019.day25

class Droid(private val cpu: IntCodeComputer) {
    var previousState: State? = null
    lateinit var state: State

    init {
        updateState()
    }

    fun executeCommand(command: Command): Boolean {
        return updateState(command)
    }

    private fun updateState(command: Command? = null): Boolean {
        command?.ascii?.forEach { cpu.queueInput(it) }

        // Nasty hack: can't think of another way to stop infinite loop as IntCodeComputer runs forever and there
        // doesn't seem to be a reliable way of detecting and bailing out of an infinite loop.
        if (command is Take && command.item == "infinite loop") {
            return false
        }

        cpu.execute()
        val output = getOutput(cpu)

        if (!output.contains("Command?", true) || output.contains("you can't move", true)) {
            return false
        }

        when (command) {
            is Take -> {
                previousState = state
                state = State(state.location, state.doors, state.items.filter { it != command.item })
            }
            is Drop -> {
                previousState = state
                state = State(state.location, state.doors, state.items.plus(command.item))
            }
            is Direction -> {
                if (!output.contains("you can't go that way", true)) {
                    val parse = State.parse(output)
                    if (parse.size > 1) {
                        println("Two states: $parse")
//                        println(output)
//                        println("#####")
                        previousState = parse[0]
                        state = parse[1]
                    } else {
                        previousState = state
                        state = parse[0]
                    }
                }
            }
            Inventory -> println(output)
            null -> {
                previousState = null
                state = State.parse(output).last()
            }
        }

        return true
    }

    fun getInventory(): String {
        Inventory.ascii.forEach { cpu.queueInput(it) }
        cpu.execute()
        return cpu.dequeueAllOutput().map { it.toChar() }.joinToString("")
    }

    private fun getOutput(cpu: IntCodeComputer) = cpu.dequeueAllOutput().map { it.toChar() }.joinToString("")
}