package com.github.jrhenderson1988.adventofcode2019.day3

import org.junit.Assert.assertEquals
import org.junit.Test

class PanelTest {
    @Test
    fun `findIntersections returns a set containing all intersecting points`() {
        val a = Wire(listOf(Path(Direction.R, 8), Path(Direction.U, 5), Path(Direction.L, 5), Path(Direction.D, 3)))
        val b = Wire(listOf(Path(Direction.U, 7), Path(Direction.R, 6), Path(Direction.D, 4), Path(Direction.L, 4)))
        assertEquals(setOf(Pair(6, 5), Pair(3, 3)), Panel.findIntersections(a.calculatePoints(), b.calculatePoints()))
    }

    @Test
    fun `calculateDistanceOfClosestIntersection gets distance of closest intersection to (0, 0)`() {
        assertEquals(
            159, Panel(
                Wire(
                    listOf(
                        Path(Direction.R, 75),
                        Path(Direction.D, 30),
                        Path(Direction.R, 83),
                        Path(Direction.U, 83),
                        Path(Direction.L, 12),
                        Path(Direction.D, 49),
                        Path(Direction.R, 71),
                        Path(Direction.U, 7),
                        Path(Direction.L, 72)
                    )
                ),
                Wire(
                    listOf(
                        Path(Direction.U, 62),
                        Path(Direction.R, 66),
                        Path(Direction.U, 55),
                        Path(Direction.R, 34),
                        Path(Direction.D, 71),
                        Path(Direction.R, 55),
                        Path(Direction.D, 58),
                        Path(Direction.R, 83)
                    )
                )
            ).calculateDistanceOfClosestIntersection()
        )

        assertEquals(
            135, Panel(
                Wire(
                    listOf(
                        Path(Direction.R, 98),
                        Path(Direction.U, 47),
                        Path(Direction.R, 26),
                        Path(Direction.D, 63),
                        Path(Direction.R, 33),
                        Path(Direction.U, 87),
                        Path(Direction.L, 62),
                        Path(Direction.D, 20),
                        Path(Direction.R, 33),
                        Path(Direction.U, 53),
                        Path(Direction.R, 51)
                    )
                ),
                Wire(
                    listOf(
                        Path(Direction.U, 98),
                        Path(Direction.R, 91),
                        Path(Direction.D, 20),
                        Path(Direction.R, 16),
                        Path(Direction.D, 67),
                        Path(Direction.R, 40),
                        Path(Direction.U, 7),
                        Path(Direction.R, 15),
                        Path(Direction.U, 6),
                        Path(Direction.R, 7)
                    )
                )
            ).calculateDistanceOfClosestIntersection()
        )
    }

    @Test
    fun calculateCombinedFewestRequiredToReachNearestIntersection() {
        assertEquals(
            610, Panel(
                Wire(
                    listOf(
                        Path(Direction.R, 75),
                        Path(Direction.D, 30),
                        Path(Direction.R, 83),
                        Path(Direction.U, 83),
                        Path(Direction.L, 12),
                        Path(Direction.D, 49),
                        Path(Direction.R, 71),
                        Path(Direction.U, 7),
                        Path(Direction.L, 72)
                    )
                ),
                Wire(
                    listOf(
                        Path(Direction.U, 62),
                        Path(Direction.R, 66),
                        Path(Direction.U, 55),
                        Path(Direction.R, 34),
                        Path(Direction.D, 71),
                        Path(Direction.R, 55),
                        Path(Direction.D, 58),
                        Path(Direction.R, 83)
                    )
                )
            ).calculateCombinedFewestRequiredToReachNearestIntersection()
        )

        assertEquals(
            410, Panel(
                Wire(
                    listOf(
                        Path(Direction.R, 98),
                        Path(Direction.U, 47),
                        Path(Direction.R, 26),
                        Path(Direction.D, 63),
                        Path(Direction.R, 33),
                        Path(Direction.U, 87),
                        Path(Direction.L, 62),
                        Path(Direction.D, 20),
                        Path(Direction.R, 33),
                        Path(Direction.U, 53),
                        Path(Direction.R, 51)
                    )
                ),
                Wire(
                    listOf(
                        Path(Direction.U, 98),
                        Path(Direction.R, 91),
                        Path(Direction.D, 20),
                        Path(Direction.R, 16),
                        Path(Direction.D, 67),
                        Path(Direction.R, 40),
                        Path(Direction.U, 7),
                        Path(Direction.R, 15),
                        Path(Direction.U, 6),
                        Path(Direction.R, 7)
                    )
                )
            ).calculateCombinedFewestRequiredToReachNearestIntersection()
        )
    }
}