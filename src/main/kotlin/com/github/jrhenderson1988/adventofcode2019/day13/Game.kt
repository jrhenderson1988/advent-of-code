package com.github.jrhenderson1988.adventofcode2019.day13

class Game(private val intCodeComputer: IntCodeComputer) {
    fun totalBlocksOnScreen(): Int {
        val cpu = intCodeComputer
        return generateSequence { Pair(cpu.execute()!!, cpu.execute()!!) to (Tile.fromValue(cpu.execute()!!)) }
            .takeWhile { !cpu.terminated }
            .toMap()
            .count { it.value == Tile.BLOCK }
    }

    fun play(): Long {
        val cpu = intCodeComputer
        cpu.instructions[0] = 2

        return generateSequence(State()) {
            it.update(
                cpu.execute(it.input())!!,
                cpu.execute(it.input())!!,
                cpu.execute(it.input())!!
            )
        }
            .takeWhile { !cpu.terminated }
            .last()
            .score
    }

    data class State(
        val board: Map<Pair<Long, Long>, Tile> = mapOf(),
        val score: Long = 0L,
        val ballPosition: Pair<Long, Long>? = null,
        val paddlePosition: Pair<Long, Long>? = null
    ) {
        companion object {
            const val PADDLE_POSITION_NEUTRAL = 0L
            const val PADDLE_POSITION_LEFT = -1L
            const val PADDLE_POSITION_RIGHT = 1L
        }

        fun input() = when {
            paddlePosition == null || ballPosition == null -> PADDLE_POSITION_NEUTRAL
            paddlePosition.first < ballPosition.first -> PADDLE_POSITION_RIGHT
            paddlePosition.first > ballPosition.first -> PADDLE_POSITION_LEFT
            else -> PADDLE_POSITION_NEUTRAL
        }

        fun update(x: Long, y: Long, t: Long) =
            when {
                x == -1L && y == 0L -> State(board, t, ballPosition, paddlePosition)
                Tile.fromValue(t) == null -> this
                t == Tile.BALL.id -> State(board.plus(Pair(x, y) to Tile.BALL), score, Pair(x, y), paddlePosition)
                t == Tile.PADDLE.id -> State(board.plus(Pair(x, y) to Tile.PADDLE), score, ballPosition, Pair(x, y))
                else -> State(board.plus(Pair(x, y) to Tile.fromValue(t)!!), score, ballPosition, paddlePosition)
            }
    }
}

