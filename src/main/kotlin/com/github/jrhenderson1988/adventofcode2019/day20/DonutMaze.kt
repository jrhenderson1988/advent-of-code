package com.github.jrhenderson1988.adventofcode2019.day20

import com.github.jrhenderson1988.adventofcode2019.common.Direction
import com.github.jrhenderson1988.adventofcode2019.common.dijkstra

class DonutMaze(
    val map: Map<Pair<Int, Int>, Cell>,
    val portals: Map<Pair<Int, Int>, Pair<Int, Int>>,
    val start: Pair<Int, Int>,
    val end: Pair<Int, Int>
) {
    fun calculateDistanceOfShortestPath(): Int {
        val path = dijkstra(map.filter { it.value == Cell.PATH }.keys, start, end) { point ->
            val neighbours = Direction.neighboursOf(point).filter { map.contains(point) }.toMutableSet()
            if (point in portals.keys) {
                neighbours.add(portals[point] ?: error(""))
            }
            neighbours
        }

        println(render(path!!.toSet()))

        return path!!.size - 1
    }

    private fun render(path: Set<Pair<Int, Int>>): String {
        val maxX = map.keys.map { it.first }.max()!!
        val maxY = map.keys.map { it.second }.max()!!

        return " " + (0..maxX).joinToString("") { (it % 10).toString() } + (0..maxY).joinToString("\n") { y ->
            (y % 10).toString() + (0..maxX).joinToString("") { x ->
                when {
                    Pair(x, y) == Pair(19, 2) -> "~"
                    Pair(x, y) in path -> "+"
                    Pair(x, y) == start -> "@"
                    Pair(x, y) == end -> "X"
                    Pair(x, y) in portals.keys -> "*"
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
            val points = mutableMapOf<Pair<Int, Int>, Char>()
            input.lines().forEachIndexed { y, line ->
                line.mapIndexed { x, ch ->
                    points[Pair(x, y)] = ch
                }
            }

            val map = points.filterValues { it in setOf('.', '#') }.mapValues { Cell.parse(it.value) }
            val labels = extractLabels(points)
            val start = (labels["AA"] ?: error("No start point defined")).first()
            val end = (labels["ZZ"] ?: error("No end point defined")).first()

            val portals = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
            labels.filterKeys { it !in listOf("AA", "ZZ") }.values.forEach {
                if (it.size != 2) {
                    error("Expected exactly 2 portal coordinates, ${it.size} received.")
                }

                portals[it.first()] = it.last()
                portals[it.last()] = it.first()
            }

            return DonutMaze(map, portals, start, end)
        }

        private fun extractLabels(map: Map<Pair<Int, Int>, Char>): Map<String, Set<Pair<Int, Int>>> {
            val labels = mutableMapOf<String, Set<Pair<Int, Int>>>()

            map.filter { it.value.isLetter() }
                .forEach { (point, ch) ->
                    val neighbours = Direction.neighboursOf(point).filter {
                        map.containsKey(it) && map[it]?.isLetter() ?: false
                    }
                    val points = listOf(listOf(point), neighbours)
                        .flatten()
                        .sortedWith(compareBy({ it.first }, { it.second }))
                    val label = points.map { map[it] }.joinToString("")
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
                        }.first { map[it] == '.' }

                    val existing = (labels[label] ?: emptySet()).toMutableSet()
                    existing.add(targetPoint)
                    labels[label] = existing
                }

            return labels
        }
    }
}