package com.github.jrhenderson1988.adventofcode2019.day15

import com.github.jrhenderson1988.adventofcode2019.readFileAsString

class Application {
    fun part1(args: Array<String>) =
        RepairDroid(IntCodeComputer.createFromString(readFileAsString(args.first())))
            .findShortestPathToOxygenSystem()
}