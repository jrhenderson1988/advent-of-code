package com.github.jrhenderson1988.adventofcode2019.day23

class Network(private val totalNodes: Int, private val program: List<Long>) {
    companion object {
        const val IDLE_ROUNDS = 3
    }

    fun firstYSentToAddress255(): Long {
        var result: Long? = null
        val nodes = createNodes()
        val send = { packet: Packet ->
            if (packet.destination == 255L) {
                result = packet.y
            } else {
                nodes[packet.destination]?.queue(packet)
            }
        }

        while (result == null) {
            round(nodes, send)
        }

        return result!!
    }

    fun firstYSentToResumeNetworkTwiceInARow(): Long? {
        val nodes = createNodes()
        var natPacket: Packet? = null
        val send = { packet: Packet ->
            if (packet.destination == 255L) {
                natPacket = packet
            } else {
                nodes[packet.destination]?.queue(packet)
            }
        }

        var idleRounds = 0
        var lastResumptionPacket: Packet? = null
        while (true) {
            val messagesSent = round(nodes, send)
            if (messagesSent == 0) {
                idleRounds++
            }

            if (idleRounds >= IDLE_ROUNDS) {
                idleRounds = 0
                val resumptionPacket = resume(nodes[0] ?: error("Could not get first node"), natPacket)
                if (resumptionPacket != null) {
                    if (lastResumptionPacket != null && lastResumptionPacket.y == resumptionPacket.y) {
                        return resumptionPacket.y
                    } else {
                        lastResumptionPacket = resumptionPacket
                    }
                }
            }
        }
    }

    private fun createNodes() = (0 until totalNodes).map { it.toLong() to Node(it.toLong(), program) }.toMap()

    private fun round(nodes: Map<Long, Node>, send: (packet: Packet) -> Unit?) =
        nodes.keys.fold(0) { messagesSent, key ->
            messagesSent + if ((nodes[key] ?: error("Could not get node $key")).execute(send)) 1 else 0
        }

    private fun resume(firstNode: Node, lastNatPacket: Packet?): Packet? {
        if (lastNatPacket != null) {
            val packet = Packet(firstNode.address, lastNatPacket.x, lastNatPacket.y)
            firstNode.queue(packet)
            return packet
        }

        return null
    }
}