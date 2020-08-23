package com.github.jrhenderson1988.adventofcode2019.day1

class FuelCounterUpper(private val modules: List<Module>) {
    fun calculateFuelRequirement() = modules.map { it.calculateFuel() }.sum()

    fun calculateFinalFuelRequirement() = modules.map { it.calculateFinalFuel() }.sum()
}