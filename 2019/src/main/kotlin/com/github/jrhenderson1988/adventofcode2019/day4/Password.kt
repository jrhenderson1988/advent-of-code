package com.github.jrhenderson1988.adventofcode2019.day4

class Password(password: Int) {
    private val digits = extractDigits(password)

    fun isSixDigits() = digits.size == 6

    fun containsPair(): Boolean {
        var i = 0
        while (i < digits.size - 1) {
            if (digits[i] == digits[i+1] && (i+2 >= digits.size || digits[i] != digits[i+2])) {

            }
            i++
        }


        val adjacents = mutableListOf<Int>()
        var prev = digits[0]
        var count = 1
        for (i in 1 until digits.size) {
            if (prev != digits[i]) {
                adjacents.add(count)
                count = 0
            }
            count++
            prev = digits[i]
        }
        adjacents.add(count)

        return adjacents.contains(2)
    }

    fun containsAdjacentDigits() = (0 until digits.size - 1).any { digits[it] == digits[it + 1] }

    fun containsNoDescendingDigits() = (0 until digits.size - 1).none { digits[it] > digits[it + 1] }

    companion object {
        fun extractDigits(value: Int) = value.toString().map { it.toString().toInt() }
    }
}