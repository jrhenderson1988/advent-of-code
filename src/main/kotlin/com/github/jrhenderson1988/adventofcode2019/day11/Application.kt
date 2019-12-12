package com.github.jrhenderson1988.adventofcode2019.day11

import com.github.jrhenderson1988.adventofcode2019.readFileAsString

class Application {
    fun part1(args: Array<String>) = Robot(readFileAsString(args.first())).calculateTotalPaintedPanels()
}