package com.github.jrhenderson1988.adventofcode2019.day25

sealed class Command(val command: String) {
    val ascii = (command.trim() + "\n").map { it.toLong() }

    override fun toString() = command
}

sealed class Direction(val direction: String) : Command(direction) {
    abstract fun delta(): Pair<Int, Int>
    abstract fun opposite(): Direction
    fun next(current: Pair<Int, Int>) = Pair(current.first + delta().first, current.second + delta().second)

    companion object {
        fun parse(input: String) =
            setOf(North, East, South, West).firstOrNull { it.command == input } ?: error("Invalid direction '$input'")
    }
}
object North : Direction("north") {
    override fun opposite() = South
    override fun delta() = Pair(0, -1)
}
object South : Direction("south") {
    override fun opposite() = North
    override fun delta() = Pair(0, 1)
}
object East : Direction("east") {
    override fun opposite() = West
    override fun delta() = Pair(1, 0)
}
object West : Direction("west") {
    override fun opposite() = East
    override fun delta() = Pair(-1, 0)
}

sealed class Action(instruction: String) : Command(instruction)
data class Take(val item: String) : Command("take $item")
data class Drop(val item: String) : Command("drop $item")
object Inventory : Command("inv")
