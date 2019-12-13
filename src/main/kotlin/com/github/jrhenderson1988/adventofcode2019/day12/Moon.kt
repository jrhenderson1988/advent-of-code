package com.github.jrhenderson1988.adventofcode2019.day12

data class Moon(val x: Int, val y: Int, val z: Int, val vx: Int = 0, val vy: Int = 0, val vz: Int = 0) {
    companion object {
        fun parse(input: String): Moon {
            val map = input
                .trim('<', '>')
                .split(',')
                .map { it.trim().split('=') }
                .map { it[0] to it[1].toInt() }
                .toMap()

            return Moon(
                map["x"] ?: error("X is missing"),
                map["y"] ?: error("Y is missing"),
                map["z"] ?: error("Z is missing")
            )
        }
    }
}