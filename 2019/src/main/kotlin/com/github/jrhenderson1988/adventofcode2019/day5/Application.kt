package com.github.jrhenderson1988.adventofcode2019.day5

import com.github.jrhenderson1988.adventofcode2019.readFileAsString

class Application {
    fun part1(args: Array<String>) =
        IntCodeComputer(readFileAsString(args.first())).execute(1).last()

    fun part2(args: Array<String>) =
        IntCodeComputer(readFileAsString(args.first())).execute(5).last()
}