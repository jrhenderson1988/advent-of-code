package com.github.jrhenderson1988.adventofcode2019.day24

import com.github.jrhenderson1988.adventofcode2019.common.Direction
import com.github.jrhenderson1988.adventofcode2019.common.powerOf

class BugPlanet(private val grid: Map<Pair<Int, Int>, Char>) {
    private val maxY = grid.keys.map { it.second }.max()!!
    private val maxX = grid.keys.map { it.first }.max()!!
    private val center = Pair(maxX / 2, maxY / 2)

    fun biodiversityRatingOfFirstRepetition(): Long {
        var g = grid.toMap()
        val biodiversityRatings = mutableSetOf(biodiversityRating(g))

        while (true) {
            g = mutateGrid(g)
            val bdr = biodiversityRating(g)

            if (biodiversityRatings.contains(bdr)) {
                return bdr
            } else {
                biodiversityRatings.add(bdr)
            }
        }
    }

    fun calculateTotalBugsInRecursiveUniverseAfterMinutes(minutes: Int) =
        countBugs((0 until minutes).fold(mapOf(0 to grid)) { u, _ -> mutateUniverse(u) })

    private fun mutateUniverse(u: Map<Int, Map<Pair<Int, Int>, Char>>): Map<Int, Map<Pair<Int, Int>, Char>> {
        val minLevel = u.keys.min()!!
        val maxLevel = u.keys.max()!!

        val mutated = mutableMapOf<Int, Map<Pair<Int, Int>, Char>>()
        (minLevel - 1..maxLevel + 1).forEach { level ->
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

    private fun tile(x: Int, y: Int, level: Int, u: Map<Int, Map<Pair<Int, Int>, Char>>) =
        if (Pair(x, y) == center) {
            '.'
        } else {
            (u[level] ?: mapOf())[Pair(x, y)] ?: '.'
        }

    private fun countBugs(u: Map<Int, Map<Pair<Int, Int>, Char>>) =
        u.map { level -> level.value.filter { grid -> grid.key != center && grid.value == '#' }.size }.sum()

    private fun mutateGrid(g: Map<Pair<Int, Int>, Char>) =
        g.map { it.key to mutateTile(it.value, Direction.neighboursOf(it.key).map { pt -> g[pt] ?: '.' }) }.toMap()

    private fun mutateTile(ch: Char, neighbours: List<Char>) =
        when (ch) {
            '#' -> if (neighbours.filter { tile -> tile == '#' }.size == 1) '#' else '.'
            '.' -> if (neighbours.filter { tile -> tile == '#' }.size in setOf(1, 2)) '#' else '.'
            else -> error("Invalid tile '$ch'.")
        }

    private fun biodiversityRating(g: Map<Pair<Int, Int>, Char>) =
        (0..maxY).map { y -> (0..maxX).map { x -> g[Pair(x, y)] } }
            .flatten()
            .mapIndexed { i, ch -> if (ch == '#') powerOf(2L, i.toLong()) else 0 }
            .sum()

    companion object {
        fun parse(input: String): BugPlanet {
            val grid = mutableMapOf<Pair<Int, Int>, Char>()
            input.lines().forEachIndexed { y, line ->
                line.forEachIndexed { x, ch ->
                    grid[Pair(x, y)] = ch
                }
            }

            return BugPlanet(grid)
        }
    }
}