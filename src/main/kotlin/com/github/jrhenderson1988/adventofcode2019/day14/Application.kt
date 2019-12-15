package com.github.jrhenderson1988.adventofcode2019.day14

import com.github.jrhenderson1988.adventofcode2019.readFileAsString

class Application {
    fun part1(args: Array<String>) =
        NanoFactory.parse(readFileAsString(args.first()))
            .calculateTotalOresRequiredToMakeFuel()
}