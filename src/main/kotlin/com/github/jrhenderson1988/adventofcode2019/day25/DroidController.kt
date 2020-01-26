package com.github.jrhenderson1988.adventofcode2019.day25

import java.util.*

class DroidController(private val program: List<Long>) {
    // Explore area, building out a map of some coordinates to names/items
    // When everything is explored, determine the target area (pressure-sensitive floor)
    // Determine which items are safe/dangerous by visiting each item and picking it up, potentially need to restart
    //  droid when it died
    // When we're ready, set the droid off picking up all safe items and then send it to target area
    // Now we need to just try every combination of items until the target area tells us we can continue
    // Continue in the "unexplored direction" and display output
    fun findPassword(): Int? {
//        manuallyPlay()

        val (itemPaths, pathToPressureSensitiveFloor) = explore()
        val safeItemPaths = filterSafeItems(itemPaths)

        // Create a new droid
        val droid = Droid(IntCodeComputer(program))

        // Pick up every safe item and backtrack to the beginning (for simplicity)
        safeItemPaths.forEach {
            it.value.forEach { direction -> droid.executeCommand(direction) }
            droid.executeCommand(Take(it.key))
            it.value.reversed().forEach { direction -> droid.executeCommand(direction.opposite()) }
        }

        // Determine the path to the security checkpoint and the final step onto the pressure-sensitive floor by
        // grabbing the last element off the path to the pressure-sensitive floor and dropping the last element
        val finalStep = pathToPressureSensitiveFloor.last()
        val pathToSecurityCheckpoint = pathToPressureSensitiveFloor.dropLast(1)

        // Walk to the security checkpoint and drop all of the items there
        pathToSecurityCheckpoint.forEach { direction -> droid.executeCommand(direction) }
        safeItemPaths.keys.forEach { item -> droid.executeCommand(Drop(item)) }

//        generateCombinations(safeItemPaths.keys).forEach { println(it) }
        generateCombinations(safeItemPaths.keys)/*.filterIndexed { i, _ -> i < 10 }*/.forEachIndexed { index, items ->
            // Pick up each item
            items.forEach { item -> droid.executeCommand(Take(item)) }

            // Walk last step
            println("Current: ${droid.state}, Previous: ${droid.previousState}")
            println("About to walk $finalStep")
            droid.executeCommand(finalStep)
//            println(droid.state.input)
            println("Walked $finalStep")
            println("Current: ${droid.state}, Previous: ${droid.previousState}")

            // If it works, yay (do something, detect the finish somehow?)
            if (droid.state.location != "security checkpoint") {
//                println("Found it bitches ($index): $items")
//                println(droid.state.input)
//                println(droid.previousState?.input)
//                println("current: ${droid.state.location}")
//                println("prev: ${droid.previousState?.location}")
                return 0
            }

            // If it doesn't and we're back to the security checkpoint, drop all items, ready for the next attempt
            items.forEach { item -> droid.executeCommand(Drop(item)) }
        }

        return null
    }

    private fun manuallyPlay() {
        val cpu = IntCodeComputer(program)
        cpu.execute()
        println(cpu.dequeueAllOutput().map { it.toChar() }.joinToString(""))

        loop@while (true) {
            val direction = when ((readLine() ?: "").trim().toLowerCase().substring(0, 1)) {
                "n" -> North
                "e" -> East
                "s" -> South
                "w" -> West
                "x" -> break@loop
                else -> {
                    println("Invalid input (n, e, s, w, x)")
                    continue@loop
                }
            }

            direction.ascii.forEach { cpu.queueInput(it) }
            cpu.execute()
            println(cpu.dequeueAllOutput().map { it.toChar() }.joinToString(""))
        }
    }

    private fun explore(): Pair<Map<String, List<Direction>>, List<Direction>> {
        val droid = Droid(IntCodeComputer(program))
        val path = Stack<Direction>()
        val explored = mutableSetOf<Pair<String, Direction>>()
        val area = mutableMapOf<String, List<Direction>>()
        var pathToPressureSensitiveFloor: List<Direction>? = null

        while (true) {
            val unexplored = droid.state.doors.filter { !explored.contains(Pair(droid.state.location, it)) }
            if (unexplored.isEmpty() && path.isEmpty()) {
                break
            }

            if (unexplored.isNotEmpty()) {
                val direction = unexplored.first()
                path.push(direction)
                explored.add(Pair(droid.state.location, direction))

                droid.executeCommand(direction)
                explored.add(Pair(droid.state.location, direction.opposite()))
                droid.state.items.forEach { item -> area[item] = path.toList() }

                if (droid.state.location == "security checkpoint" && droid.previousState?.location == "pressure-sensitive floor") {
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


    companion object {
        fun parse(program: String) = DroidController(IntCodeComputer.parseProgram(program))

        // TODO - Problemo: Currently only generating 248 combinations instead of 256 for 8 items
        private fun <T> generateCombinations(items: Set<T>): Set<Set<T>> {
            val length = items.size

            val combinations = mutableSetOf<Set<T>>()
            for (i in length until (1 shl length)) {
                val set = mutableSetOf<T>()
                for (j in 0 until length) {
                    if ((i and (1 shl j)) > 0) {
                        set.add(items.elementAt(j))
                    }
                }

                combinations.add(set)
            }

            return combinations
        }
    }
}