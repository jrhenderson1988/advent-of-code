package com.github.jrhenderson1988.adventofcode2019.day24

import com.github.jrhenderson1988.adventofcode2019.common.Direction

class BugUniverse(grid: Map<Pair<Int, Int>, Char>) {
    private val universe = mapOf(0 to grid)
    private val maxY = grid.keys.map { it.second }.max()!!
    private val maxX = grid.keys.map { it.first }.max()!!
    private val center = Pair(maxX / 2, maxY / 2)

    fun calculateTotalBugsAfterMinutes(minutes: Int): Int {
        println(neighboursOf(Triple(0, 0, 0)))
        return countBugs((0..minutes).fold(universe) { u, _ -> mutate(u) })
    }

    private fun mutate(u: Map<Int, Map<Pair<Int, Int>, Char>>): Map<Int, Map<Pair<Int, Int>, Char>> {
        val minLevel = u.keys.min()!!
        val maxLevel = u.keys.max()!!

        val mutated = mutableMapOf<Int, Map<Pair<Int, Int>, Char>>()
        (minLevel - 1..maxLevel + 1).forEach { level ->
            println(level)
        }
        // Start with 0
        // Go up 1, 2, 3... do only the ones that exist plus one more
        // Then go down -1, -2, -3... do only the ones that exist plus one more
        // Trim off the ones with empty grids


        return u
    }

    fun neighboursOf(position: Triple<Int, Int, Int>): Set<Triple<Int, Int, Int>> {
        val neighbours = Direction.neighboursOf(Pair(position.first, position.second))
            .filter { it.first in 0..maxX && it.second in 0..maxY }
            .map { Triple(it.first, it.second, position.third) }
            .toMutableSet()

        val mid = Triple(center.first, center.second, position.third)
        if (neighbours.contains(mid)) {
            neighbours.remove(mid)

            neighbours.addAll(when {
                position.first == mid.first && position.second == mid.second - 1 ->
                    (0..maxX).map { x -> Triple(x, 0, position.third + 1) }
                position.first == mid.first && position.second == mid.second + 1 ->
                    (0..maxX).map { x -> Triple(x, maxY, position.third + 1) }
                position.first == mid.first - 1 && position.second == mid.second ->
                    (0..maxY).map { y -> Triple(0, y, position.third + 1) }
                position.first == mid.first + 1 && position.second == mid.second ->
                    (0..maxY).map { y -> Triple(maxX, y, position.third + 1) }
                else -> emptyList()
            })
        }

        mapOf(
            Triple(-1, 0, -1) to (position.first == 0),
            Triple(0, -1, -1) to (position.second == 0),
            Triple(1, 0, -1) to (position.first == maxX),
            Triple(0, 1, -1) to (position.second == maxY)
        ).filter { it.value }.forEach { (delta, _) ->
            neighbours.add(Triple(mid.first + delta.first, mid.second + delta.second, mid.third + delta.third))
        }

        return neighbours
    }

    private fun mutateTile(ch: Char, neighbours: List<Char>) =
        when (ch) {
            '#' -> if (neighbours.filter { tile -> tile == '#' }.size == 1) '#' else '.'
            '.' -> if (neighbours.filter { tile -> tile == '#' }.size in setOf(1, 2)) '#' else '.'
            else -> error("Invalid tile")
        }

    private fun countBugs(u: Map<Int, Map<Pair<Int, Int>, Char>>) =
        u.map { level -> level.value.filter { grid -> grid.value == '#' }.size }.sum()

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