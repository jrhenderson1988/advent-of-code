package com.github.jrhenderson1988.adventofcode2019.day1

import org.junit.Assert
import org.junit.Test

class ModuleTest {
    @Test
    fun `Module correctly calculates fuel`() =
        mapOf(12 to 2, 14 to 2, 1969 to 654, 100756 to 33583)
            .forEach { (mass, expectedFuel) ->
                Assert.assertEquals(expectedFuel, Module(mass).calculateFuel())
            }

    @Test
    fun `Module correctly calculates final fuel`() =
        mapOf(14 to 2, 1969 to 966, 100756 to 50346)
            .forEach { (mass, expectedFuel) ->
                Assert.assertEquals(expectedFuel, Module(mass).calculateFinalFuel())
            }
}