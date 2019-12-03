package com.github.jrhenderson1988.adventofcode2019.day3

import kotlin.math.abs

class Panel(val a: Wire, val b: Wire) {
    fun findIntersections() = a.calculatePoints().intersect(b.calculatePoints()).subtract(setOf(Pair(0, 0)))

    fun calculateDistanceOfClosestIntersection() =
        findIntersections().map { manhattanDistance(Pair(0, 0), it) }.min()

    companion object {
        fun manhattanDistance(a: Pair<Int, Int>, b: Pair<Int, Int>) = abs(a.first - b.first) + abs(a.second - b.second)
    }
}