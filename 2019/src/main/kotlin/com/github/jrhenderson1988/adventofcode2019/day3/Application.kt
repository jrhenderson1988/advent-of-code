package com.github.jrhenderson1988.adventofcode2019.day3

import com.github.jrhenderson1988.adventofcode2019.readFileAsLines

class Application {
    fun part1(args: Array<String>): Int? {
        val (lineA, lineB) = readFileAsLines(args.first())
        val wireA = Wire(lineA.split(',').map { Path.parse(it) })
        val wireB = Wire(lineB.split(',').map { Path.parse(it) })
        return Panel(wireA, wireB).calculateDistanceOfClosestIntersection()
    }

    fun part2(args: Array<String>): Int? {
        val (lineA, lineB) = readFileAsLines(args.first())
        val wireA = Wire(lineA.split(',').map { Path.parse(it) })
        val wireB = Wire(lineB.split(',').map { Path.parse(it) })
        return Panel(wireA, wireB).calculateCombinedFewestRequiredToReachNearestIntersection()
    }
}