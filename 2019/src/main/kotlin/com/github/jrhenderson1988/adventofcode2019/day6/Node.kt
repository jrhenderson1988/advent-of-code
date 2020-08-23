package com.github.jrhenderson1988.adventofcode2019.day6

class Node(var parent: Node?, val identifier: String) {
    private val childNodes: MutableSet<Node> = mutableSetOf()

    val children: Set<Node>
        get() = this.childNodes.toSet()

    fun addChild(node: Node) {
        if (node.parent != null && node.parent != node) {
            error("$node already has a parent, ${node.parent}!")
        }

        node.parent = this
        childNodes.add(node)
    }

    override fun toString() = "Node{$identifier}"
}