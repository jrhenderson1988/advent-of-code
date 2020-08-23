package com.github.jrhenderson1988.adventofcode2019.day11

class Robot(program: String) {
    private val intCodeComputer = IntCodeComputer.createFromString(program)

    fun calculateTotalPaintedPanels() = paint(Pair(0, 0), Pair(0, -1), BLACK).size

    fun renderRegistrationNumber() = renderRegistrationNumber(paint(Pair(0, 0), Pair(0, -1), WHITE))

    private fun paint(initLoc: Pair<Int, Int>, initDir: Pair<Int, Int>, initClr: Long): Map<Pair<Int, Int>, Long> {
        val cpu = intCodeComputer
        val painted = mutableMapOf<Pair<Int, Int>, Long>()
        var location = initLoc
        var direction = initDir

        painted[location] = initClr
        while (!cpu.terminated) {
            cpu.addInput(painted[location] ?: BLACK)

            val color = cpu.execute()!!
            painted[location] = color

            val turn = cpu.execute()!!
            direction = nextDirection(direction, turn)
            location = nextLocation(location, direction)
        }

        return painted
    }

    companion object {
        const val BLACK = 0L
        const val WHITE = 1L

        private const val ANTICLOCKWISE = 0L
        private const val CLOCKWISE = 1L

        private fun nextLocation(location: Pair<Int, Int>, direction: Pair<Int, Int>) =
            Pair(location.first + direction.first, location.second + direction.second)

        private fun nextDirection(direction: Pair<Int, Int>, turn: Long): Pair<Int, Int> {
            val directions = listOf(
                Pair(0, -1),    // UP
                Pair(1, 0),     // RIGHT
                Pair(0, 1),     // DOWN
                Pair(-1, 0)     // LEFT
            )

            val current = directions.indexOf(direction)

            return when (turn) {
                CLOCKWISE -> directions[(current + 1) % directions.size]
                ANTICLOCKWISE -> directions[if (current <= 0) directions.size - 1 else current - 1]
                else -> error("Invalid turn [$turn].")
            }
        }

        private fun renderRegistrationNumber(painted: Map<Pair<Int, Int>, Long>): String {
            val allX = painted.keys.map { it.first }
            val allY = painted.keys.map { it.second }

            return (allY.min()!!..allY.max()!!).joinToString("\n") { y ->
                (allX.min()!!..allX.max()!!).joinToString("") { x ->
                    if (painted.containsKey(Pair(x, y)) && painted[Pair(x, y)] == WHITE) "#" else "."
                }
            }
        }
    }
}