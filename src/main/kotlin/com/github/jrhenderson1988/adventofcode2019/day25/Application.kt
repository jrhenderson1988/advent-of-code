package com.github.jrhenderson1988.adventofcode2019.day25

import com.github.jrhenderson1988.adventofcode2019.readFileAsString

class Application {
    fun part1(args: Array<String>) = DroidController.parse(readFileAsString(args.first())).findPassword(
        args.size > 1 && args[1].trim().toLowerCase() in setOf(
            "play",
            "manual",
            "true"
        )
    )
}