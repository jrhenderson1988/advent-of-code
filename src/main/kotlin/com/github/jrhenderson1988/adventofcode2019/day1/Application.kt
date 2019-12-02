package com.github.jrhenderson1988.adventofcode2019.day1

import com.github.jrhenderson1988.adventofcode2019.readFileAsLines

class Application {
    fun part1(args: Array<String>): Int {
        val lines = readFileAsLines(args.first())
        val modules = lines.map { Module(it.toInt()) }

        return FuelCounterUpper(modules).calculateFuelRequirement()
    }

    fun part2(args: Array<String>): Int {
        val lines = readFileAsLines(args.first())
        val modules = lines.map { Module(it.toInt()) }

        return FuelCounterUpper(modules).calculateFinalFuelRequirement()
    }
}