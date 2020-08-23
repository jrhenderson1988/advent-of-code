package com.github.jrhenderson1988.adventofcode2019.day23

class Node(val address: Long, program: List<Long>) {
    private val cpu = IntCodeComputer(program)
    var terminated = false

    init {
        cpu.queueInput(address)
    }

    fun queue(message: Packet) {
        cpu.queueInput(message.x)
        cpu.queueInput(message.y)
    }

    fun execute(send: (packet: Packet) -> Unit?): Boolean {
        if (cpu.terminated || terminated) {
            terminated = true
            return false
        }

        if (!cpu.hasInput()) {
            cpu.queueInput(-1)
        }

        cpu.execute()
        if (cpu.hasOutput()) {
            val destination = cpu.dequeueOutput()!!
            val x = if (cpu.hasOutput()) cpu.dequeueOutput()!! else error("Second output missing")
            val y = if (cpu.hasOutput()) cpu.dequeueOutput()!! else error("Third output missing")

            send(Packet(destination, x, y))
            return true
        }

        return false
    }

    fun terminate() {
        terminated = true
    }

    fun hasPacketQueued() = cpu.hasInput()

    override fun toString() = "Node[$address]"
}