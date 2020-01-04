package com.github.jrhenderson1988.adventofcode2019.day23

class Network(totalNodes: Int, private val program: List<Long>) {
    val nodes = (0 until totalNodes).map { it.toLong() to Node(this, it.toLong(), program) }.toMap()

    fun simulate() {
        nodes.values.toList().subList(1, nodes.values.size).forEach { node ->
            println(node)
            node.nextPacket()
        }
    }

    fun send(packet: Packet) {
        nodes[packet.destination]?.queue(packet)
    }
}