package com.github.jrhenderson1988.adventofcode2019.day20

import com.github.jrhenderson1988.adventofcode2019.common.Direction
import com.github.jrhenderson1988.adventofcode2019.common.bfs

class DonutMaze(
    private val map: Map<Pair<Int, Int>, Cell>,
    private val innerPortals: Map<Pair<Int, Int>, Pair<Int, Int>>,
    private val outerPortals: Map<Pair<Int, Int>, Pair<Int, Int>>,
    private val start: Pair<Int, Int>,
    private val end: Pair<Int, Int>
) {
    private val maxRecursionDepth = 100

    fun calculateDistanceOfShortestPath(): Int {
        val pathPoints = map.filter { it.value == Cell.PATH }.keys

        val path = bfs(start, end) { point ->
            val neighbours = Direction.neighboursOf(point).filter { pathPoints.contains(point) }.toMutableSet()
            if (point in innerPortals.keys) {
                neighbours.add(innerPortals[point] ?: error("Should not happen"))
            } else if (point in outerPortals.keys) {
                neighbours.add(outerPortals[point] ?: error("Should not happen"))
            }
            neighbours
        }

        return path!!.size - 1
    }

    fun calculateDistanceOfShortestRecursivePath(): Int? {
        val start = Triple(this.start.first, this.start.second, 0)
        val end = Triple(this.end.first, this.end.second, 0)
        val pathPoints = map.filter { it.value == Cell.PATH }.keys

        val shortestPath = bfs(start, end, { point ->
            val pointAsPair = Pair(point.first, point.second)
            val neighbours = Direction.neighboursOf(pointAsPair)
                .filter { pathPoints.contains(it) }
                .map { Triple(it.first, it.second, point.third) }
                .toMutableSet()

            if (pointAsPair in innerPortals.keys) {
                val target = innerPortals[pointAsPair] ?: error("Should not happen")
                if (point.third + 1 < maxRecursionDepth) {
                    neighbours.add(Triple(target.first, target.second, point.third + 1))
                }
            } else if (pointAsPair in outerPortals.keys && point.third > 0) {
                val target = outerPortals[pointAsPair] ?: error("Should not happen")
                neighbours.add(Triple(target.first, target.second, point.third - 1))
            }

            neighbours
        })

        // At level 0, there are no outer portals (only inner portals), AA and ZZ are the only outer points
        // At level 1+ AA and ZZ do not exist but all portals exist
        // Walking onto an inner portal transports 1 level deeper (level + 1) and walking onto an outer portal
        //  transports 1 level back up
        // The map is the same layout otherwise

        return if (shortestPath == null) null else shortestPath.size - 1
    }

    private fun render(path: Set<Pair<Int, Int>>): String {
        val maxX = map.keys.map { it.first }.max()!!
        val maxY = map.keys.map { it.second }.max()!!

        return " " + (0..maxX).joinToString("") { (it % 10).toString() } + (0..maxY).joinToString("\n") { y ->
            (y % 10).toString() + (0..maxX).joinToString("") { x ->
                when {
                    Pair(x, y) in path -> "+"
                    Pair(x, y) == start -> "@"
                    Pair(x, y) == end -> "X"
                    Pair(x, y) in innerPortals.keys -> "*"
                    Pair(x, y) in outerPortals.keys -> "*"
                    map[Pair(x, y)] == Cell.PATH -> "."
                    map[Pair(x, y)] == Cell.WALL -> "#"
                    else -> " "
                }
            }
        }
    }

    enum class Cell {
        PATH,
        WALL;

        companion object {
            fun parse(ch: Char) = when (ch) {
                '.' -> PATH
                '#' -> WALL
                else -> error("Invalid cell character [$ch].")
            }
        }
    }

    companion object {
        fun parse(input: String): DonutMaze {
            val grid = mutableMapOf<Pair<Int, Int>, Char>()
            input.lines().forEachIndexed { y, line ->
                line.mapIndexed { x, ch ->
                    grid[Pair(x, y)] = ch
                }
            }

            val map = grid.filterValues { it in setOf('.', '#') }.mapValues { Cell.parse(it.value) }
            val labels = extractLabels(grid)
            val start = (labels["AA"] ?: error("No start point defined")).first()
            val end = (labels["ZZ"] ?: error("No end point defined")).first()

            val mazeOnly = grid.filterValues { it in setOf('.', '#') }.keys
            val topLeft = Pair(mazeOnly.map { it.first }.min()!!, mazeOnly.map { it.second }.min()!!)
            val bottomRight = Pair(mazeOnly.map { it.first }.max()!!, mazeOnly.map { it.second }.max()!!)
            val outerPortals = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
            val innerPortals = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
            labels.filterKeys { it !in listOf("AA", "ZZ") }.values.forEach {
                if (it.size != 2) {
                    error("Expected exactly 2 portal coordinates, ${it.size} received.")
                }

                val a = it.first()
                val b = it.last()

                if (isOuter(a, topLeft, bottomRight)) {
                    outerPortals[a] = b
                    innerPortals[b] = a
                } else {
                    outerPortals[b] = a
                    innerPortals[a] = b
                }
            }

            return DonutMaze(map, innerPortals, outerPortals, start, end)
        }

        private fun isOuter(point: Pair<Int, Int>, topLeft: Pair<Int, Int>, bottomRight: Pair<Int, Int>) =
            point.first in setOf(topLeft.first, bottomRight.first) || point.second in setOf(topLeft.second, bottomRight.second)

        private fun extractLabels(grid: Map<Pair<Int, Int>, Char>): Map<String, Set<Pair<Int, Int>>> {
            val labels = mutableMapOf<String, Set<Pair<Int, Int>>>()

            grid.filter { it.value.isLetter() }
                .forEach { (point, _) ->
                    val neighbours = Direction.neighboursOf(point).filter {
                        grid.containsKey(it) && grid[it]?.isLetter() ?: false
                    }
                    val points = listOf(listOf(point), neighbours)
                        .flatten()
                        .sortedWith(compareBy({ it.first }, { it.second }))
                    val label = points.map { grid[it] }.joinToString("")
                    val isHorizontal = points.map { it.second }.distinct().size == 1
                    val targetPoint = points
                        .mapIndexed { i, pt ->
                            Pair(
                                pt.first + when {
                                    isHorizontal && i == 0 -> -1
                                    isHorizontal && i != 0 -> 1
                                    else -> 0
                                },
                                pt.second + when {
                                    !isHorizontal && i == 0 -> -1
                                    !isHorizontal && i != 0 -> 1
                                    else -> 0
                                }
                            )
                        }.first { grid[it] == '.' }

                    val existing = (labels[label] ?: emptySet()).toMutableSet()
                    existing.add(targetPoint)
                    labels[label] = existing
                }

            return labels
        }
    }
}