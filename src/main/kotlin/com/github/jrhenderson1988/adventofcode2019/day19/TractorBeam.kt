package com.github.jrhenderson1988.adventofcode2019.day19

class TractorBeam(private val numbers: List<Long>) {
    fun findClosestSquareOfSize(size: Int): Pair<Int, Int> {
        val affected = mutableMapOf<Pair<Int, Int>, Boolean>()
        (0 until 50).forEach { _y ->
            (0 until 50).forEach { _x ->
                affected[Pair(_x, _y)] = isPointAffected(_x, _y)
            }
        }
        println(render(affected))



        return 0 to 0
    }

    fun totalPointsAffectedInGrid(width: Int, height: Int) =
        (0 until height).sumBy { y -> (0 until width).sumBy { x -> if (isPointAffected(x, y)) 1 else 0 } }

    private fun isPointAffected(x: Int, y: Int): Boolean {
        val cpu = IntCodeComputer(numbers)
        cpu.queueInput(x.toLong())
        cpu.queueInput(y.toLong())
        while (!cpu.terminated) {
            cpu.execute()
        }

        return when (cpu.lastOutput) {
            1L -> true
            0L -> false
            else -> error("Unexpected output")
        }
    }

    companion object {
        fun render(grid: Map<Pair<Int, Int>, Boolean>) =
            ((grid.keys.map { it.second }.min()!!)..(grid.keys.map { it.second }.max()!!)).joinToString("\n") { y ->
                ((grid.keys.map { it.first }.min()!!)..(grid.keys.map { it.first }.max()!!)).joinToString("") { x ->
                    if (grid[Pair(x, y)] == true) "#" else "."
                }
            }
    }
}