package com.github.jrhenderson1988.adventofcode2019.day24

class BugUniverse(grid: Map<Pair<Int, Int>, Char>) {
    private val universe = mapOf(0 to grid)
    private val maxY = grid.keys.map { it.second }.max()!!
    private val maxX = grid.keys.map { it.first }.max()!!
    private val center = Pair((maxX - 1) / 2, (maxY - 1) / 2)

    fun calculateTotalBugsAfterMinutes(minutes: Int) =
        countBugs((0..minutes).fold(universe) { u, _ -> mutate(u) })

    private fun mutate(u: Map<Int, Map<Pair<Int, Int>, Char>>): Map<Int, Map<Pair<Int, Int>, Char>> {
        // TODO - implement

        return u
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