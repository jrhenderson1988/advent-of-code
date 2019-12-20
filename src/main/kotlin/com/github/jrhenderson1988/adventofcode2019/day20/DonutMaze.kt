package com.github.jrhenderson1988.adventofcode2019.day20

class DonutMaze(
    val points: Set<Pair<Int, Int>>,
    val portals: Map<Pair<Int, Int>, Pair<Int, Int>>,
    val start: Pair<Int, Int>,
    val end: Pair<Int, Int>
) {
    fun render(): String {
        val xs = points.map { it.first }
        val ys = points.map { it.second }
        val minX = xs.min()!!
        val minY = ys.min()!!
        val maxX = xs.max()!!
        val maxY = ys.max()!!

        return (minY until maxY).joinToString("\n") { y ->
            (minX until maxX).joinToString("") { x ->
                if (Pair(x, y) in points) "." else " "
            }
        }
    }

    companion object {
        fun parse(input: String): DonutMaze {
            val points = mutableSetOf<Pair<Int, Int>>()
            input.lines().forEachIndexed { y, line ->
                line.mapIndexed { x, ch ->
                    if (ch == '.') {
                        points.add(Pair(x, y))
                    }
                }
            }

            return DonutMaze(points, emptyMap(), Pair(0, 0), Pair(2, 2))
        }
    }
}