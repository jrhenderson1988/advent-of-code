package com.github.jrhenderson1988.adventofcode2019.day17

import com.github.jrhenderson1988.adventofcode2019.common.Direction

// https://gitlab.com/krystian.slesik/advent-of-code-2019/blob/master/src/main/java/pl/kslesik/adventofcode/Day17.java
// L,4,L,4,L,10,R,4,R,4,L,4,L,4,R,8,R,10,L,4,L,4,L,10,R,4,R,4,L,10,R,10,L,4,L,4,L,10,R,4,R,4,L,10,R,10,R,4,L,4,L,4,R,8,R,10,R,4,L,10,R,10,R,4,L,10,R,10,R,4,L,4,L,4,R,8,R,10
//
// A,B,A,C,A,C,B,C,C,B
// A: L,4,L,4,L,10,R,4
// B: R,4,L,4,L,4,R,8,R,10
// C: R,4,L,10,R,10

class ASCII(private val intCodeComputer: IntCodeComputer) {
    fun calculateSumOfAlignmentParameters(): Int {
        val (_, scaffolding) = mapScaffolding()

        return intersections(scaffolding).map { alignmentParameter(it) }.sum()
    }

    fun solve(videoFeed: Boolean): Long? {
        val (mainRoutine, subRoutines) = calculateRoutines()

        val cpu = intCodeComputer
        cpu.instructions[0] = 2

        val input = "$mainRoutine\n${subRoutines.joinToString("\n")}\n${if (videoFeed) "y" else "n"}\n"
        val ascii = toAscii(input).map { it.toLong() }
        var ptr = 0
        cpu.inputReceiver = { ascii[ptr++] }

        while (!cpu.terminated) {
            cpu.execute()
        }

        return cpu.lastOutput
    }

    /**
     * TODO - This was calculated by hand. Should ideally try to calculate programmatically by detecting the directions
     *  required to scout the whole scaffolding and then to try to calculate 3 distinct groups of instructions to cover
     *  all of the them (each group no more than 20 ASCII characters, including commas and treating each digit as
     *  individual ASCII characters). Potential approach using a regex:
     *  - Regex: ^(.{1,21})\1*(.{1,21})(?:\1|\2)*(.{1,21})(?:\1|\2|\3)*$
     *  - Source: https://www.reddit.com/r/adventofcode/comments/ebr7dg/2019_day_17_solutions/fb7ymcw
     */
    private fun calculateRoutines(): Pair<String, List<String>> =
        "A,B,A,C,A,C,B,C,C,B" to listOf("L,4,L,4,L,10,R,4", "R,4,L,4,L,4,R,8,R,10", "R,4,L,10,R,10")

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

    companion object {
        fun toAscii(input: String) = input.map { it.toInt() }
    }

}