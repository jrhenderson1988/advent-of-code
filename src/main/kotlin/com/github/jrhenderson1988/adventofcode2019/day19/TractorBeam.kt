package com.github.jrhenderson1988.adventofcode2019.day19

class TractorBeam(private val numbers: List<Long>) {
    fun findClosestSquareOfSize(size: Int): Pair<Int, Int> {
        var x = 0
        var y = size / 2
        while (true) {
            while (true) {
                if (inBeam(x, y)) {
                    if (!inBeam(x + (size - 1), y)) {
                        break
                    }
                    var i = 0
                    while (true) {
                        if (!inBeam(x + i + (size - 1), y)) {
                            break
                        }

                        if (inBeam(x + i, y + (size - 1))) {
                            return Pair(x + i, y)
                        }

                        i++
                    }
                }
                x++
            }
            y++
        }
    }

    fun totalPointsAffectedInGrid(width: Int, height: Int) =
        (0 until height).sumBy { y -> (0 until width).sumBy { x -> if (inBeam(x, y)) 1 else 0 } }

    private fun inBeam(x: Int, y: Int): Boolean {
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
}