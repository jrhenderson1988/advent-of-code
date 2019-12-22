package com.github.jrhenderson1988.adventofcode2019.day20

import com.github.jrhenderson1988.adventofcode2019.readFileAsString

class Application {
    fun part1(args: Array<String>) = DonutMaze.parse(readFileAsString(args.first())).calculateDistanceOfShortestPath()
}