package com.github.jrhenderson1988.adventofcode2019.day17

import com.github.jrhenderson1988.adventofcode2019.readFileAsString

class Application {
    fun part1(args: Array<String>) =
        ASCII(IntCodeComputer.createFromString(readFileAsString(args.first())))
            .calculateSumOfAlignmentParameters()

    fun part2(args: Array<String>) =
        ASCII(IntCodeComputer.createFromString(readFileAsString(args.first())))
            .solve(false)
}