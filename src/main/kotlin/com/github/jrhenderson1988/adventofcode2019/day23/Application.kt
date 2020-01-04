package com.github.jrhenderson1988.adventofcode2019.day23

import com.github.jrhenderson1988.adventofcode2019.readFileAsString

class Application {
    fun part1(args: Array<String>) {
        Network(50, IntCodeComputer.parseProgram(readFileAsString(args.first()))).simulate()
    }
}