package com.github.jrhenderson1988.adventofcode2019.day14

data class Chemical(val name: String, val quantity: Int) {
    override fun hashCode() = name.hashCode()

    companion object {
        fun parse(input: String): Chemical {
            val (quantity, name) = input.trim().split(' ')
            return Chemical(name.trim(), quantity.trim().toInt())
        }
    }
}