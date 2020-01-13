package com.github.jrhenderson1988.adventofcode2019.day18

import com.github.jrhenderson1988.adventofcode2019.common.Direction
import com.github.jrhenderson1988.adventofcode2019.common.bfs
import kotlin.math.min

class Vault(
    cells: Set<Pair<Int, Int>>,
    private val keys: Map<Char, Pair<Int, Int>>,
    private val doors: Map<Char, Pair<Int, Int>>,
    private val position: Pair<Int, Int>
) {
    private val cells = optimise(cells)
    private val positionToKeys: Map<Char, Path> = getPathsBetweenKeys(position, keys)
    private val keysToKeys: Map<Char, Map<Char, Path>> =
        keys.map { k -> k.key to getPathsBetweenKeys(k.value, keys.filter { it.key != k.key }) }.toMap()
    private val cache = mutableMapOf<Pair<Char?, Set<Char>>, Int>()

    fun fewestStepsToCollectAllKeysInAllQuadrants(): Int {
        return 0
    }

    fun shortestPathToCollectAllKeys(currentKey: Char? = null, collectedKeys: Set<Char> = setOf()): Int {
        if (collectedKeys.size == keys.size) {
            return 0
        }

        val cacheKey = Pair(currentKey, collectedKeys)
        if (cache.containsKey(cacheKey)) {
            return cache[cacheKey]!!
        }

        val paths = if (currentKey == null) positionToKeys else keysToKeys[currentKey] ?: error("Key not found")
        val openPaths = reachablePaths(paths, collectedKeys)

        var best = Int.MAX_VALUE
        for (p in openPaths) {
            best = min(best, p.value.size + shortestPathToCollectAllKeys(p.key, collectedKeys.union(p.value.keys)))
        }

        cache[cacheKey] = best

        return best
    }

    private fun getPathsBetweenKeys(from: Pair<Int, Int>, keys: Map<Char, Pair<Int, Int>>) =
        keys.map { it.key to getPathBetween(from, it.value) }.toMap()

    private fun getPathBetween(a: Pair<Int, Int>, b: Pair<Int, Int>): Path =
        Path(bfs(a, b, { Direction.neighboursOf(it).filter { p -> p in cells } })!!.drop(1).toSet(), doors, keys)

    private fun reachablePaths(paths: Map<Char, Path>, collectedKeys: Set<Char>) =
        paths.filter { it.value.doors.subtract(collectedKeys).isEmpty() && it.key !in collectedKeys }

    override fun toString(): String =
        (0..cells.map { it.second }.max()!! + 1).joinToString("\n") { y ->
            (0..cells.map { it.first }.max()!! + 1).joinToString("") { x ->
                when (Pair(x, y)) {
                    position -> "@"
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
        pos in paths && pos != position && pos !in keys.values && pos !in doors.values

    companion object {
        fun parse(input: String): Vault {
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

            return Vault(grid, keys, doors, initialPosition!!)
        }
    }
}