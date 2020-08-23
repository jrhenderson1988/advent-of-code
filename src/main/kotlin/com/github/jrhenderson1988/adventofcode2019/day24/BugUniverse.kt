package com.github.jrhenderson1988.adventofcode2019.day24

import com.github.jrhenderson1988.adventofcode2019.common.Direction

class BugUniverse(grid: Map<Pair<Int, Int>, Char>) {
    private val universe = mapOf(0 to grid)
    private val maxY = grid.keys.map { it.second }.max()!!
    private val maxX = grid.keys.map { it.first }.max()!!
    private val center = Pair(maxX / 2, maxY / 2)

    fun calculateTotalBugsAfterMinutes(minutes: Int) =
        countBugs((0 until minutes).fold(universe) { u, _ -> mutate(u) })

    private fun mutate(u: Map<Int, Map<Pair<Int, Int>, Char>>): Map<Int, Map<Pair<Int, Int>, Char>> {
        val minZ = u.keys.min()!!
        val maxZ = u.keys.max()!!

        val mutated = mutableMapOf<Int, Map<Pair<Int, Int>, Char>>()
        (minZ - 1..maxZ + 1).forEach { level ->
            val grid = mutableMapOf<Pair<Int, Int>, Char>()
            (0..maxY).forEach { y ->
                (0..maxX).forEach { x ->
                    grid[Pair(x, y)] = mutateTile(
                        tile(x, y, level, u),
                        neighboursOf(x, y, level).map { tile(it.first, it.second, it.third, u) }
                    )
                }
            }
            mutated[level] = grid
        }

        return mutated
    }

    fun neighboursOf(x: Int, y: Int, level: Int): Set<Triple<Int, Int, Int>> {
        val neighbours = Direction.neighboursOf(Pair(x, y))
            .filter { it.first in 0..maxX && it.second in 0..maxY }
            .map { Triple(it.first, it.second, level) }
            .toMutableSet()

        val mid = Triple(center.first, center.second, level)
        if (neighbours.contains(mid)) {
            neighbours.remove(mid)

            neighbours.addAll(when {
                x == mid.first && y == mid.second - 1 -> (0..maxX).map { _x -> Triple(_x, 0, level + 1) }
                x == mid.first && y == mid.second + 1 -> (0..maxX).map { _x -> Triple(_x, maxY, level + 1) }
                x == mid.first - 1 && y == mid.second -> (0..maxY).map { _y -> Triple(0, _y, level + 1) }
                x == mid.first + 1 && y == mid.second -> (0..maxY).map { _y -> Triple(maxX, _y, level + 1) }
                else -> emptyList()
            })
        }

        mapOf(
            Triple(-1, 0, -1) to (x == 0),
            Triple(0, -1, -1) to (y == 0),
            Triple(1, 0, -1) to (x == maxX),
            Triple(0, 1, -1) to (y == maxY)
        ).filter { it.value }.forEach { (delta, _) ->
            neighbours.add(Triple(mid.first + delta.first, mid.second + delta.second, mid.third + delta.third))
        }

        return neighbours
    }

    private fun mutateTile(ch: Char, neighbours: List<Char>) =
        when (ch) {
            '#' -> if (neighbours.filter { tile -> tile == '#' }.size == 1) '#' else '.'
            '.' -> if (neighbours.filter { tile -> tile == '#' }.size in setOf(1, 2)) '#' else '.'
            else -> error("Invalid tile '$ch'.")
        }

    private fun tile(x: Int, y: Int, level: Int, u: Map<Int, Map<Pair<Int, Int>, Char>>) =
        if (Pair(x, y) == center) {
            '.'
        } else {
            (u[level] ?: mapOf())[Pair(x, y)] ?: '.'
        }

    private fun countBugs(u: Map<Int, Map<Pair<Int, Int>, Char>>) =
        u.map { level -> level.value.filter { grid -> grid.key != center && grid.value == '#' }.size }.sum()

    private fun render(u: Map<Int, Map<Pair<Int, Int>, Char>>) =
        u.map { (level, grid) ->
            "Level: ${level}\n" + (0..maxY).joinToString("\n") { y ->
                (0..maxX).joinToString("") { x ->
                    (if (Pair(x, y) == center) '?' else grid[Pair(x, y)] ?: '.').toString()
                }
            }
        }.joinToString("\n\n")

    companion object {
        fun parse(input: String): BugUniverse {
            val grid = mutableMapOf<Pair<Int, Int>, Char>()
            input.lines().forEachIndexed { y, line ->
                line.forEachIndexed { x, ch ->
                    grid[Pair(x, y)] = ch
                }
            }

            return BugUniverse(grid)
        }
    }
}