package com.github.jrhenderson1988.adventofcode2019

import java.io.File
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2

const val R90 = PI / 2
const val R360 = PI * 2

fun readFileAsString(name: String): String {
    return File(name).readText()
}

fun readFileAsLines(name: String): List<String> {
    return File(name).readLines()
}

// TODO - Parse grid
// TODO - Parse intcodes

// Calculate the Manhattan distance between two given points
fun manhattanDistance(a: Pair<Int, Int>, b: Pair<Int, Int>) = abs(a.first - b.first) + abs(a.second - b.second)

// Calculate the highest common factor (or greatest common divisor) of two integers using Euclid's algorithm
fun hcf(a: Int, b: Int): Int = if (b == 0) a else hcf(b, a % b)

// Calculate (in degrees) the angle of the line defined by two points assuming:
// - a coordinate system where increasing X (first) moves right and increasing Y (second) moves down (top left to bottom
//   right)
// - The angle 0 (or 360) points straight up and the angle 90 degrees points to the right
fun angle(a: Pair<Int, Int>, b: Pair<Int, Int>): Double {
    val newA = Pair(a.first, a.second * -1)
    val newB = Pair(b.first, b.second * -1)
    val direction = Pair((newB.first - newA.first).toDouble(), (newB.second - newA.second).toDouble())
    val angle = ((R360 - (atan2(direction.second, direction.first))) + R90) % R360

    return angle * (180 / PI)
}

// Calculate a "delta" (x, y) pair from the two given (x, y) pairs such that applying the delta repeatedly to point a,
// by adding the x values and y values will eventually result in reaching point b. The "delta" becomes effectively the
// step.
fun delta(a: Pair<Int, Int>, b: Pair<Int, Int>): Pair<Int, Int> {
    val deltaX = abs(a.first - b.first)
    val deltaY = abs(a.second - b.second)
    val hcf = hcf(deltaX, deltaY)

    return Pair(
        if (a.first == b.first) 0 else (deltaX / hcf) * (if (a.first > b.first) -1 else 1),
        if (a.second == b.second) 0 else (deltaY / hcf) * (if (a.second > b.second) -1 else 1)
    )
}

// Calculate a set of points (integers) that exist between points a and b (exclusive).
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