package com.github.jrhenderson1988.adventofcode2019.day10

import com.github.jrhenderson1988.adventofcode2019.readFileAsString

class Application {
    fun part1(args: Array<String>) = Space(readFileAsString(args.first())).maximumVisibleAsteroidsFromBestAsteroid()
    fun part2(args: Array<String>): Int {
        Space(readFileAsString(args.first())).calculateNthDestroyedAsteroid(200)
        return 0
    }
}