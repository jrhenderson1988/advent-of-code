package com.github.jrhenderson1988.adventofcode2019.day13

import com.github.jrhenderson1988.adventofcode2019.readFileAsString

class Application {
    fun part1(args: Array<String>) =
        Game(IntCodeComputer.createFromString(readFileAsString(args.first())))
            .totalBlocksOnScreen()

    fun part2(args: Array<String>) =
        Game(IntCodeComputer.createFromString(readFileAsString(args.first())))
            .play()
}