package com.github.jrhenderson1988.adventofcode2019.day3

import kotlin.math.abs

class Panel(val a: Wire, val b: Wire) {
    fun calculateDistanceOfClosestIntersection() =
        findIntersections(a.calculatePoints(), b.calculatePoints())
            .map { manhattanDistance(Pair(0, 0), it) }
            .min()

    fun calculateCombinedFewestRequiredToReachNearestIntersection(): Int? {
        val aPoints = a.calculatePoints()
        val bPoints = b.calculatePoints()
        return findIntersections(aPoints, bPoints)
            .map { aPoints.indexOf(it) + bPoints.indexOf(it) }
            .min()
    }

    companion object {
        fun manhattanDistance(a: Pair<Int, Int>, b: Pair<Int, Int>) = abs(a.first - b.first) + abs(a.second - b.second)

        fun findIntersections(aPoints: List<Pair<Int, Int>>, bPoints: List<Pair<Int, Int>>) =
            aPoints.intersect(bPoints).subtract(setOf(Pair(0, 0)))
    }
}