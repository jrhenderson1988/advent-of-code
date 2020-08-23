package com.github.jrhenderson1988.adventofcode2019.day18

import com.github.jrhenderson1988.adventofcode2019.common.Direction
import com.github.jrhenderson1988.adventofcode2019.common.bfs
import kotlin.math.min

class Vault(
    grid: Set<Pair<Int, Int>>,
    private val keys: Map<Char, Pair<Int, Int>>,
    private val doors: Map<Char, Pair<Int, Int>>,
    private val positions: Set<Pair<Int, Int>>
) {
    private val cells = optimise(grid)
    private val positionsToKeys = positions.map { position -> getPathsBetweenKeys(position, keys) }
    private val keysToKeys =
        keys.map { k -> k.key to getPathsBetweenKeys(k.value, keys.filter { it.key != k.key }) }.toMap()
    private val cache = mutableMapOf<Pair<List<Char?>, Set<Char>>, Int>()

    fun shortestPathToCollectAllKeys(
        robotKeys: List<Char?> = positions.map { null },
        collectedKeys: Set<Char> = setOf()
    ): Int {
        if (collectedKeys.size == keys.size) {
            return 0
        }

        val cacheKey = Pair(robotKeys, collectedKeys)
        if (cache.containsKey(cacheKey)) {
            return cache[cacheKey]!!
        }

        var best = Int.MAX_VALUE
        val potentialMoves: List<Map<Char, Path>> = robotKeys.mapIndexed { robot, key ->
            reachablePaths(
                if (key == null) positionsToKeys[robot] else keysToKeys[key] ?: error("Can't find path from key $key"),
                collectedKeys
            )
        }

        potentialMoves.forEachIndexed { robot, paths ->
            paths.forEach { p ->
                best = min(
                    best,
                    p.value.size + shortestPathToCollectAllKeys(
                        robotKeys.mapIndexed { index, key -> if (index == robot) p.key else key },
                        collectedKeys.union(p.value.keys)
                    )
                )
            }
        }

        cache[cacheKey] = best

        return best
    }

    @Suppress("UNCHECKED_CAST")
    private fun getPathsBetweenKeys(from: Pair<Int, Int>, keys: Map<Char, Pair<Int, Int>>): Map<Char, Path> =
        keys.map { it.key to getPathBetween(from, it.value) }
            .filter { it.second != null }
            .toMap() as Map<Char, Path>

    private fun getPathBetween(a: Pair<Int, Int>, b: Pair<Int, Int>): Path? {
        val path = bfs(a, b) { Direction.neighboursOf(it).filter { p -> p in cells } }
        return if (path != null) {
            Path(path.drop(1).toSet(), doors, keys)
        } else {
            null
        }
    }

    private fun reachablePaths(paths: Map<Char, Path>, collectedKeys: Set<Char>) =
        paths.filter { it.value.doors.subtract(collectedKeys).isEmpty() && it.key !in collectedKeys }

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
        fun parse(input: String, quadrants: Boolean = false): Vault {
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

            val positions = if (quadrants) {
                setOf(Pair(-1, 0), Pair(0, -1), Pair(1, 0), Pair(0, 1), Pair(0, 0)).forEach {
                    grid.remove(Pair(initialPosition!!.first + it.first, initialPosition!!.second + it.second))
                }

                setOf(Pair(-1, -1), Pair(1, -1), Pair(1, 1), Pair(-1, 1)).map {
                    val pos = Pair(initialPosition!!.first + it.first, initialPosition!!.second + it.second)
                    grid.add(pos)
                    pos
                }.toSet()
            } else {
                setOf(initialPosition!!)
            }

            return Vault(grid, keys, doors, positions)
        }
    }
}