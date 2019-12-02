package com.github.jrhenderson1988.adventofcode2019.day2

import com.github.jrhenderson1988.adventofcode2019.readFileAsString

class Application {
    fun part1(args: Array<String>): List<Int> {
        val input = readFileAsString(args.first()).split(',')
            .filter { it.trim() != "" }
            .map { it.trim().toInt() }
            .toMutableList()

        input[1] = 12
        input[2] = 2

        return Program(input).execute()
    }

    fun part2(args: Array<String>) {

    }
}