package com.github.jrhenderson1988.adventofcode2019.day15

import com.github.jrhenderson1988.adventofcode2019.common.Direction
import com.github.jrhenderson1988.adventofcode2019.common.dijkstra
import java.util.Stack

class RepairDroid(private val intCodeComputer: IntCodeComputer) {
    private val priorities = listOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)

    fun calculateFewestStepsToOxygenSystem(): Int {
        val (position, oxygenSystemPosition, maze) = discover(Pair(0, 0))

        val coordinates = maze.filter { it.value == Cell.PATH }.keys
        val path = dijkstra(coordinates, position, oxygenSystemPosition)
            ?: error("The shortest path could not be calculated")

        return path.size - 1
    }

    fun calculateTimeTakenToFillWithOxygen(): Int {
        val (_, oxygenSystemPosition, maze) = discover(Pair(0, 0))

        val filled = maze.toMutableMap()
        filled[oxygenSystemPosition] = Cell.OXYGEN

        var minutes = 0
        while (filled.containsValue(Cell.PATH)) {
            filled
                .filter { it.value == Cell.OXYGEN }
                .forEach { (position, _) ->
                    Direction.neighboursOf(position)
                        .filter { filled[it] == Cell.PATH }
                        .forEach { filled[it] = Cell.OXYGEN }
                }

            minutes++
        }

        return minutes
    }

    private fun discover(position: Pair<Int, Int>): Triple<Pair<Int, Int>, Pair<Int, Int>, Map<Pair<Int, Int>, Cell>> {
        var pos = position
        var oxygenSystemPosition: Pair<Int, Int>? = null
        val maze = mutableMapOf(position to Cell.PATH)
        val history = Stack<Direction>()
        val cpu = intCodeComputer
        while (!cpu.terminated) {
            val (direction, backtracking) = nextDirection(pos, maze, history) ?: break
            val response = cpu.execute(directionToCode(direction)) ?: break
            val cell = codeToCell(response)

            val target = Pair(pos.first + direction.delta.first, pos.second + direction.delta.second)
            when (cell) {
                Cell.WALL -> maze[target] = Cell.WALL
                Cell.OXYGEN_SYSTEM, Cell.PATH -> {
                    maze[target] = Cell.PATH
                    pos = target

                    if (!backtracking) {
                        history.push(direction)
                    }

                    if (cell == Cell.OXYGEN_SYSTEM) {
                        oxygenSystemPosition = pos
                    }
                }
                else -> error("Unexpected cell $cell")
            }
        }

        return Triple(pos, oxygenSystemPosition!!, maze.toMap())
    }

    private fun codeToCell(code: Long) =
        when (code) {
            0L -> Cell.WALL
            1L -> Cell.PATH
            2L -> Cell.OXYGEN_SYSTEM
            else -> error("Invalid cell code [$code]")
        }

    private fun directionToCode(direction: Direction) =
        when (direction) {
            Direction.NORTH -> 1L
            Direction.SOUTH -> 2L
            Direction.WEST -> 3L
            Direction.EAST -> 4L
        }

    private fun nextDirection(
        position: Pair<Int, Int>,
        maze: Map<Pair<Int, Int>, Cell>,
        history: Stack<Direction>
    ): Pair<Direction, Boolean>? {
        val unexplored = priorities.filter { maze[it.nextPoint(position)] == null }
        return when {
            unexplored.isNotEmpty() -> unexplored.first() to false
            history.isNotEmpty() -> history.pop().opposite() to true
            else -> null
        }
    }

    enum class Cell {
        WALL,
        PATH,
        OXYGEN_SYSTEM,
        OXYGEN;
    }
}