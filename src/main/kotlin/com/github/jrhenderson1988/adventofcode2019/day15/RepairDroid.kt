package com.github.jrhenderson1988.adventofcode2019.day15

class RepairDroid(private val intCodeComputer: IntCodeComputer) {
    private val maze = mutableMapOf(Pair(0, 0) to Cell.PATH)
    private var position = Pair(0, 0)
    private var facing: Direction = Direction.NORTH

    fun findShortestPathToOxygenSystem(): Int {
        render()
        var i = 0
        while (!intCodeComputer.terminated) {
            val direction = nextDirection()
            val response = intCodeComputer.execute(direction.code) ?: break
            val cell = Cell.fromCode(response)

            val nextPosition = Pair(position.first + direction.delta.first, position.second + direction.delta.second)
            when (cell) {
                Cell.WALL -> maze[nextPosition] = Cell.WALL
                Cell.OXYGEN_SYSTEM -> {
                    maze[nextPosition] = Cell.OXYGEN_SYSTEM
                    position = nextPosition
                }
                Cell.PATH -> {
                    maze[nextPosition] = Cell.PATH
                    position = nextPosition
                }
            }

            render()
            if (i++ > 10000) {
                break
            }
        }

        return 0
    }

    // - Prefer moving the direction we're facing if next point is undiscovered
    // - If next point is a wall, we have to turn - use priority (N > E > S > W) (possibly should alternate between E/W
    //   and N/S to avoid getting stuck in circles)
    // - If next point is a path/oxygen (it has been discovered) prefer turning to an undiscovered path. If all paths
    //   have been discovered, prefer to continue moving in direction we're facing
    //
    fun nextDirection() =
        when (maze[facing.nextPoint(position)]) {
            null -> facing
            Cell.WALL -> facing.nextDirection()
            Cell.PATH, Cell.OXYGEN_SYSTEM -> {
                val a = facing.nextDirection()
                val b = a.nextDirection().nextDirection()
                when {
                    maze[a.nextPoint(position)] == null -> a
                    maze[b.nextPoint(position)] == null -> b
                    else -> facing
                }
            }
        }

    private fun render() {
        val ys = maze.keys.map { it.second }
        val maxY = ys.max() ?: 0
        val minY = ys.min() ?: 0
        val xs = maze.keys.map { it.first }
        val maxX = xs.max() ?: 0
        val minX = xs.min() ?: 0

//        println("($minX, $minY) -> ($maxX, $maxY)")
//        println(maze)
//        System.out.flush()
        println("\u001Bc")
        println("\n\n\n\n" + (minY..maxY).joinToString("\n") { y ->
            (minX..maxX).joinToString("") { x ->
                when {
                    Pair(x, y) == position -> "@"
                    maze[Pair(x, y)] == null -> " "
                    else -> maze[Pair(x, y)]!!.display
                }
            }
        })
    }

    enum class Cell(val code: Long, val display: String) {
        WALL(0L, "#"),
        PATH(1L, "."),
        OXYGEN_SYSTEM(2L, "*");

        companion object {
            fun fromCode(code: Long) = values().find { c -> c.code == code } ?: error("Invalid code")
        }
    }

    enum class Direction(val code: Long, val delta: Pair<Int, Int>) {
        NORTH(1L, Pair(0, -1)),
        SOUTH(2L, Pair(0, 1)),
        WEST(3L, Pair(-1, 0)),
        EAST(4L, Pair(1, 0));

        fun nextPoint(current: Pair<Int, Int>) = Pair(current.first + delta.first, current.second - delta.second)
        fun nextDirection() = when (this) {
            NORTH -> EAST
            EAST -> SOUTH
            SOUTH -> WEST
            WEST -> NORTH
        }
    }
}