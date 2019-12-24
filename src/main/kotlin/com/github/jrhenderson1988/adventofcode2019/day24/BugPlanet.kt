package com.github.jrhenderson1988.adventofcode2019.day24

import com.github.jrhenderson1988.adventofcode2019.common.Direction
import com.github.jrhenderson1988.adventofcode2019.common.powerOf

class BugPlanet(val grid: Map<Pair<Int, Int>, Char>) {
    val maxY = grid.keys.map { it.second }.max()!!
    val maxX = grid.keys.map { it.first }.max()!!

    fun biodiversityRatingOfFirstRepetition(): Long {
        var g = grid.toMap()
        val biodiversityRatings = mutableSetOf(biodiversityRating(g))

        while (true) {
            g = mutate(g)
            val bdr = biodiversityRating(g)

            if (biodiversityRatings.contains(bdr)) {
                return bdr
            } else {
                biodiversityRatings.add(bdr)
            }
        }
    }

    private fun mutate(g: Map<Pair<Int, Int>, Char>) =
        g.map {
            val neighbours = Direction.neighboursOf(it.key).map { pt -> if (g.containsKey(pt)) g[pt] else '.' }
            it.key to when (it.value) {
                '#' -> if (neighbours.filter { tile -> tile == '#' }.size == 1) '#' else '.'
                '.' -> if (neighbours.filter { tile -> tile == '#' }.size in setOf(1, 2)) '#' else '.'
                else -> error("Invalid tile")
            }
        }.toMap()

    private fun biodiversityRating(g: List<List<Char>>) =
        g.flatten().mapIndexed { i, ch -> if (ch == '#') powerOf(2L, i.toLong()) else 0 }.sum()

    private fun biodiversityRating(g: Map<Pair<Int, Int>, Char>) =
        (0..maxY).map { y -> (0..maxX).map { x -> g[Pair(x, y)] } }
            .flatten()
            .mapIndexed { i, ch -> if (ch == '#') powerOf(2L, i.toLong()) else 0 }
            .sum()

    companion object {
        fun render(g: Map<Pair<Int, Int>, Char>) =
            (0..g.keys.map { it.second }.max()!!).joinToString("\n") { y ->
                (0..g.keys.map { it.first }.max()!!).joinToString("") { x ->
                    if (g.containsKey(Pair(x, y))) g[Pair(x, y)].toString() else " "
                }
            }

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