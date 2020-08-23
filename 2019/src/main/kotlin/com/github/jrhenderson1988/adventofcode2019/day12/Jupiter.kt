package com.github.jrhenderson1988.adventofcode2019.day12

import com.github.jrhenderson1988.adventofcode2019.lcm
import kotlin.math.abs


class Jupiter(val moons: List<Moon>) {

    fun calculateTotalEnergyAfterSteps(steps: Int) =
        (0 until steps).fold(moons) { ms, _ -> applyVelocity(applyGravity(ms)) }.sumBy {
            val potential = abs(it.x) + abs(it.y) + abs(it.z)
            val kinetic = abs(it.vx) + abs(it.vy) + abs(it.vz)
            potential * kinetic
        }

    fun numberOfStepsUntilStateRepeated(): Long {
        val x = stepsUntilFirstRepetition({ it.x }, { it.vx })
        val y = stepsUntilFirstRepetition({ it.y }, { it.vy })
        val z = stepsUntilFirstRepetition({ it.z }, { it.vz })

        return lcm(lcm(x.toLong(), y.toLong()), z.toLong())
    }

    private fun stepsUntilFirstRepetition(position: (moon: Moon) -> Int, velocity: (moon: Moon) -> Int): Int {
        var m = moons
        val initialPositions = m.mapIndexed { i, moon -> i to Pair(position(moon), velocity(moon)) }.toMap()
        var step = 0
        while (true) {
            step++
            m = applyVelocity(applyGravity(m))

            val stateMatches = m.foldIndexed(true) { i, prev, moon ->
                prev && Pair(position(moon), velocity(moon)) == initialPositions[i]
            }

            if (stateMatches) {
                return step
            }
        }
    }

    companion object {
        fun parse(input: String) = Jupiter(input.trim().lines().map { Moon.parse(it) })

        fun applyVelocity(moons: List<Moon>) =
            moons.map { moon ->
                Moon(
                    moon.x + moon.vx,
                    moon.y + moon.vy,
                    moon.z + moon.vz,
                    moon.vx,
                    moon.vy,
                    moon.vz
                )
            }

        fun applyGravity(moons: List<Moon>) =
            moons.map { moon ->
                val others = moons.filter { it != moon }
                Moon(
                    moon.x,
                    moon.y,
                    moon.z,
                    moon.vx + others.sumBy { other -> compareVelocity(moon.x, other.x) },
                    moon.vy + others.sumBy { other -> compareVelocity(moon.y, other.y) },
                    moon.vz + others.sumBy { other -> compareVelocity(moon.z, other.z) }
                )
            }

        private fun compareVelocity(a: Int, b: Int) = when {
            a > b -> -1
            a < b -> 1
            else -> 0
        }
    }
}