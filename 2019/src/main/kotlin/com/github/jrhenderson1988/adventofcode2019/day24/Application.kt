package com.github.jrhenderson1988.adventofcode2019.day24

import com.github.jrhenderson1988.adventofcode2019.readFileAsString

class Application {
    fun part1(args: Array<String>) =
        BugPlanet.parse(readFileAsString(args.first())).biodiversityRatingOfFirstRepetition()

    fun part2(args: Array<String>) =
        BugPlanet.parse(readFileAsString(args.first())).calculateTotalBugsInRecursiveUniverseAfterMinutes(200)
}