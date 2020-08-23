package com.github.jrhenderson1988.adventofcode2019.day1

import org.junit.Assert
import org.junit.Test

class FuelCounterUpperTest {
    @Test
    fun `FuelCounterUpper correctly calculates total required fuel`() =
        mapOf(listOf(12, 14, 1969, 100756) to 34241)
            .forEach { (modules, expectedFuel) ->
                Assert.assertEquals(
                    expectedFuel,
                    FuelCounterUpper(modules.map { Module(it) }).calculateFuelRequirement()
                )
            }

    @Test
    fun `FuelCounterUpper correctly calculates final total required`() =
        mapOf(listOf(14, 1969, 100756) to 51314)
            .forEach { (modules, expectedFinalFuel) ->
                Assert.assertEquals(
                    expectedFinalFuel,
                    FuelCounterUpper(modules.map { Module(it) }).calculateFinalFuelRequirement()
                )
            }
}