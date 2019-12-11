package com.github.jrhenderson1988.adventofcode2019.day10

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2

class Space(map: String) {
    private val asteroids = parseAsteroidCoordinates(map)

    fun maximumVisibleAsteroidsFromBestAsteroid() =
        calculateTotalAsteroidsVisibleFromEachAsteroid()
            .values
            .max()

    fun calculateNthDestroyedAsteroid(n: Int): Pair<Int, Int> {
        val base = calculateTotalAsteroidsVisibleFromEachAsteroid().maxBy { it.value }!!.key

        val toDestroy = mutableListOf<Pair<Pair<Int, Int>, Double>>()

        asteroids
            .filter { it != base }
            .groupBy { angle(base, it) }
            .forEach { (angle, asteroids) ->
                asteroids.sortedBy { distance(base, it) }
                    .forEachIndexed { index, asteroid ->
                        toDestroy.add(Pair(asteroid, angle + (360 * index)))
                    }
            }

        if (n >= toDestroy.size) {
            error("Asteroid $n does not exist")
        }

        return toDestroy.sortedBy { it.second }.map { it.first }[n]
    }

    fun calculateTotalAsteroidsVisibleFromEachAsteroid() =
        asteroids
            .map { it to totalAsteroidsVisibleFrom(it) }
            .toMap()

    fun totalAsteroidsVisibleFrom(target: Pair<Int, Int>): Int {
        return asteroids
            .map { other -> if (target != other && isAsteroidVisible(target, other)) 1 else 0 }
            .sum()
    }

    fun isAsteroidVisible(a: Pair<Int, Int>, b: Pair<Int, Int>) =
        pointsBetween(a, b).intersect(asteroids).isEmpty()

    companion object {
        const val R90 = PI / 2
        const val R180 = PI
        const val R270 = R90 + R180
        const val R360 = PI * 2

        fun distance(a: Pair<Int, Int>, b: Pair<Int, Int>) = abs(a.first - b.first) + abs(a.second - b.second)

        fun angle(a: Pair<Int, Int>, b: Pair<Int, Int>): Double {
            val newA = Pair(a.first, a.second * -1)
            val newB = Pair(b.first, b.second * -1)
            val direction = Pair((newB.first - newA.first).toDouble(), (newB.second - newA.second).toDouble())
            val angle = ((R360 - (atan2(direction.second, direction.first))) + R90) % R360

            return angle * (180 / PI)
        }

        fun pointsBetween(a: Pair<Int, Int>, b: Pair<Int, Int>): Set<Pair<Int, Int>> {
            val points = mutableSetOf<Pair<Int, Int>>()

            val delta = delta(a, b)
            val iterations = when {
                delta.first != 0 -> (abs(a.first - b.first) / abs(delta.first)) - 1
                delta.second != 0 -> (abs(a.second - b.second) / abs(delta.second)) - 1
                else -> 0
            }

            var current = a
            (0 until iterations).forEach { _ ->
                current = Pair(current.first + delta.first, current.second + delta.second)
                points.add(current)
            }

            return points.toSet()
        }

        fun hcf(a: Int, b: Int): Int = if (b == 0) a else hcf(b, a % b)

        fun delta(a: Pair<Int, Int>, b: Pair<Int, Int>): Pair<Int, Int> {
            val deltaX = abs(a.first - b.first)
            val deltaY = abs(a.second - b.second)
            val hcf = hcf(deltaX, deltaY)

            return Pair(
                if (a.first == b.first) 0 else (deltaX / hcf) * (if (a.first > b.first) -1 else 1),
                if (a.second == b.second) 0 else (deltaY / hcf) * (if (a.second > b.second) -1 else 1)
            )
        }

        fun parseAsteroidCoordinates(map: String): Set<Pair<Int, Int>> {
            val coordinates = mutableSetOf<Pair<Int, Int>>()

            map.lines()
                .forEachIndexed { y, line ->
                    line.forEachIndexed { x, ch ->
                        if (ch == '#') {
                            coordinates.add(Pair(x, y))
                        }
                    }
                }

            return coordinates
        }
    }
}