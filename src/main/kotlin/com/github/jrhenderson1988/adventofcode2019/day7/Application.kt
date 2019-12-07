package com.github.jrhenderson1988.adventofcode2019.day7

import com.github.jrhenderson1988.adventofcode2019.readFileAsString

class Application {
    fun part1(args: Array<String>)=
        AmplifierController(readFileAsString(args.first()))
            .calculateLargestOutputSignal((0..4).toList(), 0)
}