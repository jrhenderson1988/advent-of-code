package com.github.jrhenderson1988.adventofcode2019.day14

import kotlin.math.max

class NanoFactory(private val recipes: Map<String, Pair<Long, List<Chemical>>>) {
    fun calculateTotalOresRequiredToMakeFuel() = requiredOre(Chemical("FUEL", 1), mutableMapOf())

    fun calculateFuelGeneratedByOneTrillionOres(): Long {
        var l = 0L
        var r = TRILLION
        var best = 0L
        while (l <= r) {
            val m = (l + r) / 2
            val result = requiredOre(Chemical("FUEL", m), mutableMapOf())
            when {
                result < TRILLION -> {
                    l = m + 1
                    best = m
                }
                result > TRILLION -> r = m - 1
                else -> return m
            }
        }

        return best
    }

    private fun requiredOre(chemical: Chemical, surplus: MutableMap<String, Long>): Long {
        val (product, quantity) = chemical.name to chemical.quantity
        val (recipeQuantity, ingredients) = recipes[product] ?: error("Could not find recipe for $chemical")
        val existing = surplus[product] ?: 0
        val required = quantity - existing
        val multiplier = (max(required, 0) + recipeQuantity - 1) / recipeQuantity
        val used = recipeQuantity * multiplier
        val extra = used - required

        surplus[product] = extra

        return ingredients.fold(0L) { acc, ingredient ->
            acc + if (ingredient.name == "ORE") {
                multiplier * ingredient.quantity
            } else {
                requiredOre(Chemical(ingredient.name, multiplier * ingredient.quantity), surplus)
            }
        }
    }

    companion object {
        const val TRILLION = 1_000_000_000_000L

        fun parse(input: String) =
            NanoFactory(
                input.lines()
                    .map { line ->
                        val (input, output) = line.split("=>")
                        val target = Chemical.parse(output.trim())
                        val ingredients = input.split(',').map { Chemical.parse(it.trim()) }.toList()

                        target.name to Pair(target.quantity, ingredients)
                    }.toMap()
            )

    }
}