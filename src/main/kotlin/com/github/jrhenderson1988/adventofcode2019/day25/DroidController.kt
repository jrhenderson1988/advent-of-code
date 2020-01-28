package com.github.jrhenderson1988.adventofcode2019.day25

import com.github.jrhenderson1988.adventofcode2019.common.generateCombinations
import java.util.Stack

class DroidController(private val program: List<Long>) {
    companion object {
        private const val SECURITY_CHECKPOINT = "security checkpoint"
        private const val PRESSURE_SENSITIVE_FLOOR = "pressure-sensitive floor"

        fun parse(program: String) = DroidController(IntCodeComputer.parseProgram(program))
    }

    fun findPassword(manual: Boolean = false): Int? = if (manual) manuallyPlay() else solve()

    private fun solve(): Int? {
        val (itemPaths, pathToPressureSensitiveFloor) = explore()
        val safeItemPaths = filterSafeItems(itemPaths)
        val safeItems = safeItemPaths.keys
        val finalStep = pathToPressureSensitiveFloor.last()

        val droid = Droid(IntCodeComputer(program))
        droid.collectAllItemsAndBacktrack(safeItemPaths)
        droid.walk(pathToPressureSensitiveFloor.dropLast(1))
        droid.drop(safeItems)

        generateCombinations(safeItems).forEach { eachItem ->
            droid.take(eachItem)
            droid.walk(finalStep)

            if (droid.isFinished() && droid.doorCode() != null) {
                return droid.doorCode()
            }

            droid.drop(eachItem)
        }

        return null
    }

    private fun manuallyPlay(): Int {
        val cpu = IntCodeComputer(program)
        cpu.execute()
        println(cpu.dequeueAllOutput().map { it.toChar() }.joinToString(""))

        loop@ while (true) {
            val input = (readLine() ?: "").trim()
            if (input.isEmpty()) {
                println("Try again:")
                continue
            }

            val spacePos = input.indexOf(' ')
            val direction = when ((if (spacePos > -1) input.substring(0, spacePos) else input).toLowerCase()) {
                "n", "north" -> North
                "e", "east" -> East
                "s", "south" -> South
                "w", "west" -> West
                "i", "inv", "inventory" -> Inventory
                "t", "take" -> Take(input.substring(spacePos).trim())
                "d", "drop" -> Drop(input.substring(spacePos).trim())
                "x", "exit" -> break@loop
                else -> {
                    println("Invalid input")
                    continue@loop
                }
            }

            direction.ascii.forEach { cpu.queueInput(it) }
            cpu.execute()
            println(cpu.dequeueAllOutput().map { it.toChar() }.joinToString(""))
        }

        return 0
    }

    private fun explore(): Pair<Map<String, List<Direction>>, List<Direction>> {
        val droid = Droid(IntCodeComputer(program))
        val path = Stack<Direction>()
        val explored = mutableSetOf<Pair<String, Direction>>()
        val area = mutableMapOf<String, List<Direction>>()
        var pathToPressureSensitiveFloor: List<Direction>? = null

        while (true) {
            val unexplored = droid.doors().filter { !explored.contains(Pair(droid.location(), it)) }
            if (unexplored.isEmpty() && path.isEmpty()) {
                break
            }

            if (unexplored.isNotEmpty()) {
                val direction = unexplored.first()
                path.push(direction)
                explored.add(Pair(droid.location(), direction))

                droid.executeCommand(direction)
                explored.add(Pair(droid.location(), direction.opposite()))
                droid.items().forEach { item -> area[item] = path.toList() }

                if (droid.isLocation(SECURITY_CHECKPOINT) && droid.isPreviousLocation(PRESSURE_SENSITIVE_FLOOR)) {
                    // At this point, we've found the path that we need to take from the beginning to the
                    // pressure-sensitive floor, but we're actually at the security checkpoint after being teleported
                    // back. So we need to record the path to the PSF and pop off the last item from the path stack
                    // since we're actually back the security checkpoint.
                    pathToPressureSensitiveFloor = path.toList()
                    path.pop()
                }
            } else {
                droid.executeCommand(path.pop()!!.opposite())
            }
        }

        return Pair(
            area.toMap(),
            pathToPressureSensitiveFloor ?: error("Could not find path to pressure-sensitive floor")
        )
    }

    // Filter out safe items from the given map of items -> paths. If an item can be taken, moved and dropped, then it
    // is considered safe
    private fun filterSafeItems(itemPaths: Map<String, List<Direction>>) =
        itemPaths.filter {
            val droid = Droid(IntCodeComputer(program))
            it.value.forEach { direction -> droid.executeCommand(direction) }

            droid.executeCommand(Take(it.key))
                    && droid.executeCommand(it.value.last().opposite())
                    && droid.executeCommand(Drop(it.key))
        }
}