package com.github.jrhenderson1988.adventofcode2019.day12

import org.junit.Assert.assertEquals
import org.junit.Test

class JupiterTest {
    companion object {
        const val FIRST =
            """<x=-1, y=0, z=2>
              |<x=2, y=-10, z=-7>
              |<x=4, y=-8, z=8>
              |<x=3, y=5, z=-1>"""

        const val SECOND =
            """<x=-8, y=-10, z=0>
              |<x=5, y=5, z=10>
              |<x=2, y=-7, z=3>
              |<x=9, y=-8, z=-3>"""
    }

    @Test
    fun parse() =
        mapOf(
            FIRST to Jupiter(listOf(Moon(-1, 0, 2), Moon(2, -10, -7), Moon(4, -8, 8), Moon(3, 5, -1))),
            SECOND to Jupiter(listOf(Moon(-8, -10, 0), Moon(5, 5, 10), Moon(2, -7, 3), Moon(9, -8, -3)))
        ).forEach { (input, expected) ->
            val parsed = Jupiter.parse(input.trimMargin())

            assertEquals(expected.moons.size, parsed.moons.size)
            (expected.moons.indices).forEach { i ->
                assertEquals(expected.moons[i].x, parsed.moons[i].x)
                assertEquals(expected.moons[i].y, parsed.moons[i].y)
                assertEquals(expected.moons[i].z, parsed.moons[i].z)
                assertEquals(expected.moons[i].vx, parsed.moons[i].vx)
                assertEquals(expected.moons[i].vy, parsed.moons[i].vy)
                assertEquals(expected.moons[i].vz, parsed.moons[i].vz)
            }
        }

    @Test
    fun calculateTotalEnergyAfterSteps() =
        mapOf(Pair(FIRST, 10) to 179, Pair(SECOND, 100) to 1940)
            .forEach { (input, expected) ->
                assertEquals(
                    expected,
                    Jupiter.parse(input.first.trimMargin()).calculateTotalEnergyAfterSteps(input.second)
                )
            }

    @Test
    fun applyGravity() =
        mapOf(
            listOf(Moon(1, 2, 3), Moon(3, 2, 1)) to listOf(Moon(1, 2, 3, 1, 0, -1), Moon(3, 2, 1, -1, 0, 1))
        ).forEach { (input, expected) ->
            val applied = Jupiter.applyGravity(input)
            repeat(expected.size) { i ->
                assertEquals(expected[i].x, applied[i].x)
                assertEquals(expected[i].y, applied[i].y)
                assertEquals(expected[i].z, applied[i].z)
                assertEquals(expected[i].vx, applied[i].vx)
                assertEquals(expected[i].vy, applied[i].vy)
                assertEquals(expected[i].vz, applied[i].vz)
            }
        }

    @Test
    fun applyVelocity() =
        mapOf(
            listOf(Moon(1, 2, 3, 1, 0, -1), Moon(3, 2, 1, -1, 0, 1)) to
                    listOf(Moon(2, 2, 2, 1, 0, -1), Moon(2, 2, 2, -1, 0, 1))
        ).forEach { (input, expected) ->
            val applied = Jupiter.applyVelocity(input)
            repeat(expected.size) { i ->
                assertEquals(expected[i].x, applied[i].x)
                assertEquals(expected[i].y, applied[i].y)
                assertEquals(expected[i].z, applied[i].z)
                assertEquals(expected[i].vx, applied[i].vx)
                assertEquals(expected[i].vy, applied[i].vy)
                assertEquals(expected[i].vz, applied[i].vz)
            }
        }

    @Test
    fun numberOfStepsUntilStateRepeated() =
        mapOf(SECOND to 4_686_774_924)
            .forEach { (input, expected) ->
                assertEquals(
                    expected,
                    Jupiter.parse(input.trimMargin()).numberOfStepsUntilStateRepeated()
                )
            }

}