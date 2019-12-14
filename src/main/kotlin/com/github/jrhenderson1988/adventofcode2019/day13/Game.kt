package com.github.jrhenderson1988.adventofcode2019.day13

class Game(private val intCodeComputer: IntCodeComputer) {
    companion object {
        const val PADDLE_POSITION_NEUTRAL = 0L
        const val PADDLE_POSITION_LEFT = -1L
        const val PADDLE_POSITION_RIGHT = 1L
    }

    fun totalBlocksOnScreen() =
        generateBoard().count { it.value == TileId.BLOCK }

    fun play(): Long {
        val board = mutableMapOf<Pair<Long, Long>, TileId>()
        var score = 0L
        val cpu = intCodeComputer
        var nextInput = 0L

        cpu.instructions[0] = 2

        var ballPosition: Pair<Long, Long>? = null
        var paddlePosition: Pair<Long, Long>? = null
        while (!cpu.terminated) {
            val x = cpu.execute(nextInput)!!
            val y = cpu.execute(nextInput)!!
            val tile = cpu.execute(nextInput)!!

            if (x == -1L && y == 0L) {
                score = tile
            } else {
                val position = Pair(x, y)
                val tileId = TileId.fromValue(tile.toInt())
                if (tileId != null) {
                    board[position] = tileId

                    if (tileId == TileId.BALL) {
                        ballPosition = position
                    } else if (tileId == TileId.PADDLE) {
                        paddlePosition = position
                    }
                }
            }

            nextInput = when {
                paddlePosition == null || ballPosition == null -> PADDLE_POSITION_NEUTRAL
                paddlePosition.first < ballPosition.first -> PADDLE_POSITION_RIGHT
                paddlePosition.first > ballPosition.first -> PADDLE_POSITION_LEFT
                else -> PADDLE_POSITION_NEUTRAL
            }
        }

        return score
    }

    private fun generateBoard(): Map<Pair<Long, Long>, TileId> {
        val cpu = intCodeComputer
        val board = mutableMapOf<Pair<Long, Long>, TileId>()
        while (!cpu.terminated) {
            val x = cpu.execute()!!
            val y = cpu.execute()!!
            val tileId = TileId.fromValue(cpu.execute()!!.toInt())

            board[Pair(x, y)] = tileId ?: error("Invalid tile")
        }

        return board
    }

    private fun renderBoard(board: Map<Pair<Long, Long>, TileId>, score: Long): String {
        val allX = board.keys.map { it.first }
        val allY = board.keys.map { it.first }
        val minX = allX.min()!!
        val minY = allY.min()!!
        val maxX = allX.max()!!
        val maxY = allY.max()!!

        return "Score: $score\n" + (minY..maxY).joinToString("\n") { y ->
            (minX..maxX).joinToString("") { x ->
                when (board[(Pair(x, y))]) {
                    TileId.BLOCK -> "."
                    TileId.BALL -> "o"
                    TileId.PADDLE -> "_"
                    TileId.WALL -> "#"
                    else -> " "
                }
            }
        }
    }
}

