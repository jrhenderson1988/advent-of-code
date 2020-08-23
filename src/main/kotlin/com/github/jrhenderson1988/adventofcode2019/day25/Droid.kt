package com.github.jrhenderson1988.adventofcode2019.day25

class Droid(private val cpu: IntCodeComputer) {
    companion object {
        private const val DOORS_START = "Doors here lead:"
        private const val DOORS_END = "\n\n"
        private const val ITEMS_START = "Items here:"
        private const val ITEMS_END = "\n\n"
        private const val COMPLETE = "Analysis complete! You may proceed."
        private const val CANT_MOVE = "you can't move"
        private const val COMMAND = "Command?"
        private const val CANT_GO_THAT_WAY = "you can't go that way"
        private const val INFINITE_LOOP = "infinite loop"
        private val LOCATION_REGEX = Regex("==\\s+(.+)\\s+==")
        private val DOOR_CODE_REGEX = Regex("You should be able to get in by typing (\\d+) on the keypad")
    }

    private var previousLocation: String? = null
    private var location: String? = null
    private var doors: List<Direction> = listOf()
    private var items: List<String> = listOf()
    private var finished = false
    private var doorCode: Int? = null

    init {
        updateState()
    }

    fun executeCommand(command: Command): Boolean {
        return updateState(command)
    }

    private fun updateState(command: Command? = null): Boolean {
        command?.ascii?.forEach { cpu.queueInput(it) }

        // Nasty hack: can't think of another way to stop infinite loop as IntCodeComputer runs forever and there
        // doesn't seem to be a reliable way of detecting and bailing out of an infinite loop.
        if (command is Take && command.item == INFINITE_LOOP) {
            return false
        }

        cpu.execute()
        val output = cpu.dequeueAllOutput().map { it.toChar() }.joinToString("")

        if (output.contains(COMPLETE, true)) {
            finished = true
            doorCode = parseDoorCode(output)
            return true
        }

        if (!output.contains(COMMAND, true) || output.contains(CANT_MOVE, true)) {
            return false
        }

        when (command) {
            is Take -> items = items.filter { it != command.item }
            is Drop -> items = items.plus(command.item)
            is Direction ->
                if (!output.contains(CANT_GO_THAT_WAY, true)) {
                    updateLocation(output)
                }
            Inventory -> println(output)
            null -> updateLocation(output)
        }

        return true
    }

    fun location() = location!!

    fun previousLocation() = previousLocation

    fun items() = items

    fun doors() = doors

    fun isFinished() = finished

    fun doorCode() = doorCode

    fun isLocation(input: String) = input == location

    fun isPreviousLocation(input: String) = input == previousLocation

    fun drop(item: String) = executeCommand(Drop(item))

    fun drop(items: Set<String>) = items.forEach { drop(it) }

    fun take(item: String) = executeCommand(Take(item))

    fun take(items: Set<String>) = items.forEach { take(it) }

    fun walk(direction: Direction) = executeCommand(direction)

    fun walk(path: List<Direction>) = path.forEach { walk(it) }

    private fun collectItemAndBacktrack(item: String, path: List<Direction>) =
        listOf(path, listOf(Take(item)), path.reversed().map { it.opposite() })
            .flatten()
            .forEach { executeCommand(it) }

    fun collectAllItemsAndBacktrack(itemPaths: Map<String, List<Direction>>) =
        itemPaths.forEach { (item, path) -> collectItemAndBacktrack(item, path) }

    private fun updateLocation(input: String) {
        val segments = parseSegments(input)
        when (segments.size) {
            1 -> {
                val (newLocation, segment) = segments.first()
                previousLocation = location
                location = newLocation
                doors = parseDoors(segment)
                items = parseItems(segment)
            }
            2 -> {
                val (firstNewLocation, _) = segments.first()
                val (secondNewLocation, secondSegment) = segments.last()
                previousLocation = firstNewLocation
                location = secondNewLocation
                doors = parseDoors(secondSegment)
                items = parseItems(secondSegment)
            }
            else -> error("Invalid output: Expected exactly 1 or 2 location segments (Received: ${segments.size}")
        }
    }

    private fun parseSegments(input: String): List<Pair<String, String>> {
        val locations = LOCATION_REGEX
            .findAll(input)
            .map { it.groups }
            .map { it[1]!!.value.trim().toLowerCase() to it[0]!!.range }
            .toList()

        return locations.mapIndexed { index, (location, range) ->
            location to input.substring(
                range.last + 1,
                if (index + 1 >= locations.size) input.length else locations[index + 1].second.first
            ).trim()
        }
    }

    private fun parseDoors(segment: String): List<Direction> {
        val dStart = segment.indexOf(DOORS_START, 0, true)
        return if (dStart == -1) listOf() else
            segment.substring(dStart + DOORS_START.length, segment.indexOf(DOORS_END, dStart + 1, true))
                .trim()
                .lines()
                .map { it.trimStart('-', ' ').trim() }
                .map { Direction.parse(it) }
    }

    private fun parseItems(segment: String): List<String> {
        val iStart = segment.indexOf(ITEMS_START, 0, true)
        return if (iStart == -1) listOf() else
            segment.substring(iStart + ITEMS_START.length, segment.indexOf(ITEMS_END, iStart + 1, true))
                .trim()
                .lines()
                .map { it.trimStart('-', ' ').trim() }
    }

    private fun parseDoorCode(output: String): Int {
        val result = DOOR_CODE_REGEX.find(output)

        return result?.groupValues?.elementAt(1)?.toInt()!!
    }
}