package com.github.jrhenderson1988.adventofcode2019.day12

import com.github.jrhenderson1988.adventofcode2019.readFileAsString

class Application {
    fun part1(args: Array<String>) = Jupiter.parse(readFileAsString(args.first())).calculateTotalEnergyAfterSteps(1000)
    fun part2(args: Array<String>) = Jupiter.parse(readFileAsString(args.first())).numberOfStepsUntilStateRepeated()
}