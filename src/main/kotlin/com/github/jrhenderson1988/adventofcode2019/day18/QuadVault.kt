package com.github.jrhenderson1988.adventofcode2019.day18

import com.github.jrhenderson1988.adventofcode2019.common.Direction
import com.github.jrhenderson1988.adventofcode2019.common.bfs

class QuadVault(
    grid: Set<Pair<Int, Int>>,
    private val keys: Map<Char, Pair<Int, Int>>,
    private val doors: Map<Char, Pair<Int, Int>>,
    private val positions: Set<Pair<Int, Int>>
) {
    private val cells = optimise(grid)
    private val positionsToKeys = positions.map { position -> getPathsBetweenKeys(position, keys) }
    private val keysToKeys =
        keys.map { k -> k.key to getPathsBetweenKeys(k.value, keys.filter { it.key != k.key }) }.toMap()

    fun shortestPathToCollectAllKeys(
        currentKeys: Set<Char?> = positions.map { null }.toSet(),
        collectedKeys: Set<Char> = setOf()
    ): Int {

        return 0
    }

    private fun getPathsBetweenKeys(from: Pair<Int, Int>, keys: Map<Char, Pair<Int, Int>>) =
        keys.map { it.key to getPathBetween(from, it.value) }.toMap().filter { it.value != null }

    private fun getPathBetween(a: Pair<Int, Int>, b: Pair<Int, Int>): Path? {
        val path = bfs(a, b) { Direction.neighboursOf(it).filter { p -> p in cells } }
        return if (path != null) {
            Path(path.drop(1).toSet(), doors, keys)
        } else {
            null
        }
    }

    override fun toString(): String =
        (0..cells.map { it.second }.max()!! + 1).joinToString("\n") { y ->
            (0..cells.map { it.first }.max()!! + 1).joinToString("") { x ->
                when (Pair(x, y)) {
                    in positions -> "@"
                    in keys.values -> keys.filter { it.value == Pair(x, y) }.keys.first().toString()
                    in doors.values -> doors.filter { it.value == Pair(x, y) }.keys.first().toString().toUpperCase()
                    in cells -> "."
                    else -> "#"
                }
            }
        }

    private fun optimise(paths: Set<Pair<Int, Int>>): Set<Pair<Int, Int>> {
        val optimised = paths.toMutableSet()
        while (true) {
            val deadEnds = findDeadEnds(optimised)
            if (deadEnds.isEmpty()) {
                return optimised
            }

            optimised.removeAll(deadEnds)
        }
    }

    private fun findDeadEnds(paths: Set<Pair<Int, Int>>) =
        paths.filter { pos -> isDeadEnd(pos, paths) }

    private fun isDeadEnd(pos: Pair<Int, Int>, paths: Set<Pair<Int, Int>>) =
        Direction.neighboursOf(pos).filter { neighbour -> neighbour in paths }.size == 1 && isOpenCell(pos, paths)

    private fun isOpenCell(pos: Pair<Int, Int>, paths: Set<Pair<Int, Int>>) =
        pos in paths && pos !in positions && pos !in keys.values && pos !in doors.values

    companion object {
        fun parse(input: String): QuadVault {
            val grid = mutableSetOf<Pair<Int, Int>>()
            var initialPosition: Pair<Int, Int>? = null
            val keys = mutableMapOf<Char, Pair<Int, Int>>()
            val doors = mutableMapOf<Char, Pair<Int, Int>>()

            input.trim().split('\n').forEachIndexed { y, line ->
                line.forEachIndexed { x, char ->
                    val pos = Pair(x, y)
                    if (char == '@') {
                        initialPosition = pos
                        grid.add(pos)
                    } else if (char.isLetter() && char.isUpperCase()) {
                        doors[char.toLowerCase()] = pos
                        grid.add(pos)
                    } else if (char.isLetter()) {
                        keys[char] = pos
                        grid.add(pos)
                    } else if (char == '.') {
                        grid.add(pos)
                    }
                }
            }

            val positions = mutableSetOf<Pair<Int, Int>>()
            setOf(Pair(-1, 0), Pair(0, -1), Pair(1, 0), Pair(0, 1), Pair(0, 0)).forEach {
                grid.remove(Pair(initialPosition!!.first + it.first, initialPosition!!.second + it.second))
            }

            setOf(Pair(-1, -1), Pair(1, -1), Pair(1, 1), Pair(-1, 1)).forEach {
                val pos = Pair(initialPosition!!.first + it.first, initialPosition!!.second + it.second)
                positions.add(pos)
                grid.add(pos)
            }

            return QuadVault(grid, keys, doors, positions)
        }
    }
}