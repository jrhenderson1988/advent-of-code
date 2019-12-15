package com.github.jrhenderson1988.adventofcode2019.day14

class NanoFactory(val reactions: Map<Chemical, List<Chemical>>) {
    fun calculateTotalOresRequiredToMakeFuel(): Int {
        calculateTotalBaseElement(Chemical("FUEL", 1))
        return 0
    }

    fun calculateTotalBaseElement(chemical: Chemical) {
        val ingredients = findIngredientsFor(chemical)
        println("ingredients for $chemical: $ingredients")

        if (ingredients.isEmpty()) {
            println("Base ingredient: $chemical")
        } else {
            for (ingredient in ingredients) {
                calculateTotalBaseElement(ingredient)
            }
        }

    }

    private fun findIngredientsFor(chemical: Chemical) =
        reactions.entries.find { it.key.name == chemical.name }?.value ?: emptyList()


    companion object {
        fun parse(input: String) =
            NanoFactory(
                input.lines()
                    .map { line ->
                        val (input, output) = line.split("=>")
                        Chemical.parse(output.trim()) to input.split(',').map { Chemical.parse(it) }.toList()
                    }.toMap()
            )

    }
}