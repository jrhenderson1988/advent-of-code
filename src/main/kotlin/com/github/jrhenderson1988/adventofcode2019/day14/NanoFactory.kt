package com.github.jrhenderson1988.adventofcode2019.day14

class NanoFactory(val reactions: Map<Chemical, List<Chemical>>) {
    fun calculateTotalOresRequiredToMakeFuel(): Int {
        return calculateTotalRequiredBaseChemical(Chemical("A", 1))
    }

    fun calculateTotalRequiredBaseChemical(chemical: Chemical): Int {
        val ingredients = findIngredientsFor(chemical)
        if (ingredients.isEmpty()) {
            return 1
        }

        var total = 0
        for (ingredient in ingredients) {
            total += ingredient.quantity * calculateTotalRequiredBaseChemical(ingredient)
        }

        return total
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