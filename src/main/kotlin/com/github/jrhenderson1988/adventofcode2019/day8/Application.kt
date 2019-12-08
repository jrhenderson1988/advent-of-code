package com.github.jrhenderson1988.adventofcode2019.day8

import com.github.jrhenderson1988.adventofcode2019.readFileAsString

class Application {
    fun part1(args: Array<String>) =
        Image(readFileAsString(args.first()), 25, 6).calculateChecksum()

    fun part2(args: Array<String>) =
        Image(readFileAsString(args.first()), 25, 6).render(true)
}