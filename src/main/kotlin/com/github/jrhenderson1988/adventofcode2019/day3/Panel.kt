package com.github.jrhenderson1988.adventofcode2019.day3

import com.github.jrhenderson1988.adventofcode2019.manhattanDistance

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
        fun findIntersections(aPoints: List<Pair<Int, Int>>, bPoints: List<Pair<Int, Int>>) =
            aPoints.intersect(bPoints).subtract(setOf(Pair(0, 0)))
    }
}