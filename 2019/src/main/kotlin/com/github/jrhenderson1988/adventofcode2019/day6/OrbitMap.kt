package com.github.jrhenderson1988.adventofcode2019.day6

import java.util.*

class OrbitMap(items: List<String>) {
    private val rootNode = buildNodeTree(items)

    fun calculateChecksum(): Int {
        var checksum = 0
        val stack = Stack<Node>()
        stack.push(rootNode)

        while (stack.isNotEmpty()) {
            val node = stack.pop()
            checksum += totalDescendantsOfNode(node)
            for (child in node.children) {
                stack.push(child)
            }
        }

        return checksum
    }

    fun calculateMinimumOrbitalTransfersBetween(a: String, b: String) =
        calculateMinimumOrbitalTransfersBetween(
            findNode(a).parent ?: error("Node with identifier [$a] does not have a parent"),
            findNode(b).parent ?: error("Node with identifier [$b] does not have a parent")
        )

    private fun calculateMinimumOrbitalTransfersBetween(a: Node, b: Node): Int {
        val aAncestors = ancestorsWithDistances(a)
        val bAncestors = ancestorsWithDistances(b)

        return aAncestors.keys.intersect(bAncestors.keys)
            .map { distanceTo(it, aAncestors) + distanceTo(it, bAncestors) }
            .min() ?: error("There are no common ancestors between $a and $b")
    }

    private fun distanceTo(node: Node, distances: Map<Node, Int>) =
        distances[node] ?: error("Could not find distance to [$node]")

    private fun totalDescendantsOfNode(item: Node): Int {
        var total = 0
        val stack = Stack<Node>()

        stack.add(item)
        while (stack.isNotEmpty()) {
            val node = stack.pop()
            total += node.children.size
            stack.addAll(node.children)
        }

        return total
    }

    private fun findNode(identifier: String): Node {
        val stack = Stack<Node>()
        stack.add(rootNode)

        while (stack.isNotEmpty()) {
            val node = stack.pop()
            if (node.identifier == identifier) {
                return node
            }

            stack.addAll(node.children)
        }

        error("Could not find node identified by [$identifier].")
    }

    private fun ancestorsWithDistances(node: Node): Map<Node, Int> {
        val ascendants = mutableMapOf<Node, Int>()

        var n = node
        var distance = 1
        while (n.parent != null) {
            n = n.parent!!
            ascendants[n] = distance++
        }

        return ascendants
    }

    companion object {
        fun buildNodeTree(orbits: List<String>): Node {
            val dependencies = mutableMapOf<String, MutableSet<String>>()
            val nodes = mutableMapOf<String, Node>()

            orbits.forEach {
                val (parent, child) = it.trim().toUpperCase().split(')')
                if (dependencies.containsKey(parent)) {
                    dependencies[parent]!!.add(child)
                } else {
                    dependencies[parent] = mutableSetOf(child)
                }

                nodes.putIfAbsent(parent, Node(null, parent))
                nodes.putIfAbsent(child, Node(null, child))
            }

            dependencies.forEach { (parentId, childIds) ->
                val parent = nodes[parentId]
                childIds.forEach { childId ->
                    val child = nodes[childId]
                    parent!!.addChild(child!!)
                }
            }

            return nodes.values.find { it.parent == null }!!
        }
    }
}