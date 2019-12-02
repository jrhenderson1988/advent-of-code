package com.github.jrhenderson1988.adventofcode2019

import com.github.jrhenderson1988.adventofcode2019.day1.FuelCounterUpper
import com.github.jrhenderson1988.adventofcode2019.day1.Module
import org.junit.Assert.assertEquals
import org.junit.Test

class Day1Test {
    @Test
    fun `Module correctly calculates fuel`() =
        mapOf(12 to 2, 14 to 2, 1969 to 654, 100756 to 33583)
            .forEach { (mass, expectedFuel) ->
                assertEquals(expectedFuel, Module(mass).calculateFuel())
            }

    @Test
    fun `Module correctly calculates final fuel`() =
        mapOf(14 to 2, 1969 to 966, 100756 to 50346)
            .forEach { (mass, expectedFuel) ->
                assertEquals(expectedFuel, Module(mass).calculateFinalFuel())
            }

    @Test
    fun `FuelCounterUpper correctly calculates total required fuel`() =
        mapOf(listOf(12, 14, 1969, 100756) to 34241)
            .forEach { (modules, expectedFuel) ->
                assertEquals(
                    expectedFuel,
                    FuelCounterUpper(modules.map { Module(it) }).calculateFuelRequirement()
                )
            }

    @Test
    fun `FuelCounterUpper correctly calculates final total required`() =
        mapOf(listOf(14, 1969, 100756) to 51314)
            .forEach { (modules, expectedFinalFuel) ->
                assertEquals(
                    expectedFinalFuel,
                    FuelCounterUpper(modules.map { Module(it) }).calculateFinalFuelRequirement()
                )
            }

    @Test
    fun `calculate total fuel for day 1`() {
        val lines = this::class.java.classLoader.getResourceAsStream("day1.txt").bufferedReader().readLines()
        val modules = lines.map { Module(it.toInt()) }
        println(FuelCounterUpper(modules).calculateFuelRequirement())
    }

    @Test
    fun `calculate total fuel for day 1 part 2`() {
        val lines = this::class.java.classLoader.getResourceAsStream("day1.txt").bufferedReader().readLines()
        val modules = lines.map { Module(it.toInt()) }
        println(FuelCounterUpper(modules).calculateFinalFuelRequirement())
    }
}