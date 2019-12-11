package com.github.jrhenderson1988.adventofcode2019.day10

import com.github.jrhenderson1988.adventofcode2019.angle
import com.github.jrhenderson1988.adventofcode2019.delta
import com.github.jrhenderson1988.adventofcode2019.manhattanDistance
import com.github.jrhenderson1988.adventofcode2019.pointsBetween
import kotlin.math.PI
import kotlin.math.abs

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
                asteroids.sortedBy { manhattanDistance(base, it) }
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