package com.github.jrhenderson1988.adventofcode2019.day23

class Node(private val network: Network, private val address: Long, program: List<Long>) {
    private val cpu = IntCodeComputer(program)

    init {
        cpu.queueInput(address)
    }

    fun queue(message: Packet) {
        cpu.queueInput(message.x)
        cpu.queueInput(message.y)
    }

    fun execute() {
        while (!cpu.terminated) {
            val destination = cpu.execute()!!
            val x = cpu.execute()!!
            val y = cpu.execute()!!

            network.send(Packet(destination, x, y))
        }
    }

    fun nextPacket(): Packet? {
        // Initially, need to pass the address as first input instruction
        // All other input instructions can be packets (or -1 if no packets exist in queue)
        // Return null when terminated
        // Otherwise return a packet (3 output instructions - destination, x, y)
        if (cpu.terminated) {
            return null
        }

        val destination = cpu.execute()!!
        println(destination)
        val x = cpu.execute()!!
        println(x)
        val y = cpu.execute()!!
        println(y)

        return Packet(destination, x, y)
    }

    override fun toString() = "Node[$address]"
}