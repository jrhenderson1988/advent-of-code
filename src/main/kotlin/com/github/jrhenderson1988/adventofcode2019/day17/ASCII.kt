package com.github.jrhenderson1988.adventofcode2019.day17

import com.github.jrhenderson1988.adventofcode2019.common.Direction

// L4,L4,L10,R4,R4,L4,L4,R8,R10,L4,L4,L10,R4,R4,L10,R10,L4,L4,L10,R4,R4,L10,R10,R4,L4,L4,R8,R10,R4,L10,R10,R4,L10,R10,R4,L4,L4,R8,R10
//
// B,C,B,A,B,A,C,A,A,C
// A: R4,L10,R10
// B: L4,L4,L10,R4
// C: R4,L4,L4,R8,R10

class ASCII(private val intCodeComputer: IntCodeComputer) {
    fun calculateSumOfAlignmentParameters(): Int {
        val (_, scaffolding) = mapScaffolding()

        return intersections(scaffolding).map { alignmentParameter(it) }.sum()
    }

    fun solve(): Int {
        val (robotPosition, scaffolding) = mapScaffolding()


        println(robotPosition)
        println(render(robotPosition, scaffolding))

//        val cpu = intCodeComputer
//        cpu.instructions[0] = 2


        return -1
    }

    private fun mapScaffolding(): Pair<Pair<Int, Int>, MutableSet<Pair<Int, Int>>> {
        val cpu = intCodeComputer
        val scaffolding = mutableSetOf<Pair<Int, Int>>()
        var y = 0
        var x = 0
        var robotPosition: Pair<Int, Int>? = null
        while (!cpu.terminated) {
            when (val output = cpu.execute()!!.toInt().toChar()) {
                '\n' -> {
                    y++
                    x = -1
                }
                in setOf('^', 'v', '<', '>') -> {
                    robotPosition = Pair(x, y)
                    println(output)
                    scaffolding.add(robotPosition)
                }
                '#' -> scaffolding.add(Pair(x, y))
                '.' -> {
                }
                else -> error("Unexpected output [$output]")
            }
            x++
        }

        return robotPosition!! to scaffolding
    }

    private fun intersections(scaffolding: Set<Pair<Int, Int>>) =
        scaffolding.filter {
            Direction.neighboursOf(it).fold(true) { acc, neighbour ->
                acc && scaffolding.contains(neighbour)
            }
        }

    private fun alignmentParameter(point: Pair<Int, Int>) = point.first * point.second


    private fun render(robot: Pair<Int, Int>, scaffolding: Set<Pair<Int, Int>>) =
        ((scaffolding.map { it.second }.min()!!)..(scaffolding.map { it.second }.max()!!)).joinToString("\n") { y ->
            ((scaffolding.map { it.first }.min()!!)..(scaffolding.map { it.first }.max()!!)).joinToString("") { x ->
                if (Pair(x, y) == robot) "@" else if (scaffolding.contains(Pair(x, y))) "#" else " "
            }
        }

}