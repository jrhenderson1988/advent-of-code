package com.github.jrhenderson1988.adventofcode2019.day4

import com.github.jrhenderson1988.adventofcode2019.readFileAsString

class Application {
    fun part1(args: Array<String>) =
        getInputRange(args.first())
            .asSequence()
            .map { Password(it) }
            .filter { it.isSixDigits() }
            .filter { it.containsAdjacentDigits() }
            .filter { it.containsNoDescendingDigits() }
            .count()

    fun part2(args: Array<String>) =
        getInputRange(args.first())
            .asSequence()
            .map { Password(it) }
            .filter { it.isSixDigits() }
            .filter { it.containsNoDescendingDigits() }
            .filter { it.containsPair() }
            .count()


    private fun getInputRange(file: String): IntRange {
        val (from, to) = readFileAsString(file).split('-').map { it.toInt() }

        return (from..to)
    }
}