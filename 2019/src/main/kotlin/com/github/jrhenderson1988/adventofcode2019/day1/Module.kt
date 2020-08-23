package com.github.jrhenderson1988.adventofcode2019.day1

class Module(private val mass: Int) {
    fun calculateFuel(): Int = calculateFuelForMass(mass)

    private fun calculateFuelForMass(mass: Int): Int = (mass / 3) - 2

    fun calculateFinalFuel(): Int {
        var finalFuel = 0
        var fuel: Int? = null

        while (true) {
            fuel = calculateFuelForMass(fuel ?: this.mass)
            if (fuel > 0) {
                finalFuel += fuel
            } else {
                break
            }
        }

        return finalFuel
    }
}