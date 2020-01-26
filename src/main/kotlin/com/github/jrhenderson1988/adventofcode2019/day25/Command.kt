package com.github.jrhenderson1988.adventofcode2019.day25

sealed class Command(val command: String) {
    val ascii = (command.trim() + "\n").map { it.toLong() }

    override fun toString() = command

    companion object {
        fun parse(input: String, state: State): Command {
            val trimmed = input.trim()
            val firstSpace = trimmed.indexOf(' ')
            val cmd = (if (firstSpace > -1) trimmed.substring(0, firstSpace) else trimmed).toLowerCase()
            val arg = (if (firstSpace > -1) trimmed.substring(firstSpace) else "").trim()

            println("cmd: $cmd, arg: $arg, state: $state")

            return when (cmd) {
                in setOf("north", "n") -> North
                in setOf("south", "s") -> South
                in setOf("east", "e") -> East
                in setOf("west", "w") -> West
                in setOf("take", "t") -> Take(arg)
                in setOf("drop", "d") -> Drop(arg)
                in setOf("inv", "i") -> Inventory
                else -> error("Invalid command")
            }
        }
    }
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
