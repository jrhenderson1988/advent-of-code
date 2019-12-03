package com.github.jrhenderson1988.adventofcode2019.day2

import com.github.jrhenderson1988.adventofcode2019.readFileAsString

class Application {
    fun part1(args: Array<String>): Int {
        val input = getInput(args.first())

        return Program(input).execute(12, 2).first()
    }

    fun part2(args: Array<String>): Int {
        val input = getInput(args.first())
        val program = Program(input)

        for (noun in 0..99) {
            for (verb in 0..99) {
                val result = program.execute(noun, verb)
                if (result[0] == 19690720) {
                    return (noun * 100) + verb
                }
            }
        }

        return -1
    }

    private fun getInput(path: String) =
        readFileAsString(path).split(',')
            .filter { it.trim() != "" }
            .map { it.trim().toInt() }
            .toMutableList()
}