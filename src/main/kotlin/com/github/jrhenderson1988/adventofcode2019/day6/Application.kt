package com.github.jrhenderson1988.adventofcode2019.day6

import com.github.jrhenderson1988.adventofcode2019.readFileAsLines

class Application {
    fun part1(args: Array<String>) =
        OrbitMap(readFileAsLines(args.first()))
            .calculateChecksum()

    fun part2(args: Array<String>) =
        OrbitMap(readFileAsLines(args.first()))
            .calculateMinimumOrbitalTransfersBetween("YOU", "SAN")

}