package com.github.jrhenderson1988.adventofcode2019.day10

import com.github.jrhenderson1988.adventofcode2019.readFileAsString

class Application {
    fun part1(args: Array<String>) = Space(readFileAsString(args.first())).maximumVisibleAsteroidsFromBestAsteroid()
    fun part2(args: Array<String>): Int {
        val asteroid = Space(readFileAsString(args.first())).calculateNthDestroyedAsteroid(199)
        return asteroid.first * 100 + asteroid.second
    }
}