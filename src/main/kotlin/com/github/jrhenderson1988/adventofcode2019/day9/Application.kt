package com.github.jrhenderson1988.adventofcode2019.day9

import com.github.jrhenderson1988.adventofcode2019.readFileAsString

class Application {
    fun part1(args: Array<String>) = IntCodeComputer(readFileAsString(args.first()), listOf(1)).complete().first()
    fun part2(args: Array<String>) = IntCodeComputer(readFileAsString(args.first()), listOf(2)).complete().first()
}