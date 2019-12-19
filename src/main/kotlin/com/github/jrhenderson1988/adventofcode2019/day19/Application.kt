package com.github.jrhenderson1988.adventofcode2019.day19

import com.github.jrhenderson1988.adventofcode2019.readFileAsString

class Application {
    fun part1(args: Array<String>) =
        TractorBeam(readFileAsString(args.first()).split(',').map { it.trim().toLong() })
            .totalPointsAffectedInGrid(50, 50)

    fun part2(args: Array<String>): Int {
        val (x, y) = TractorBeam(readFileAsString(args.first()).split(',').map { it.trim().toLong() })
            .findClosestSquareOfSize(100)

        return x * 10000 + y
    }
}