package com.github.jrhenderson1988.adventofcode2019.day3

import com.github.jrhenderson1988.adventofcode2019.readFileAsLines

class Application {
    fun part1(args: Array<String>) {
        val a = "R75,D30,R83,U83,L12,D49,R71,U7,L72"
        val b = "U62,R66,U55,R34,D71,R55,D58,R83"
//        val (a, b) = readFileAsLines(args.first())
        val wireA = Wire(a.split(',').map { Path.parse(it) })
        println(wireA)
    }
}