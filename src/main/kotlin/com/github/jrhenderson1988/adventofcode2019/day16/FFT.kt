package com.github.jrhenderson1988.adventofcode2019.day16

class FFT(val signal: List<Int>) {
    fun applyPhases(n: Int): List<Int> {
        println(signal)
        println(pattern(1))
        return listOf()
    }

    private fun pattern(i: Int): List<Int> {
        val base = listOf(0, 1, 0, -1)
        val repeated = base.map { item -> (0..i).map { item } }.flatten()
        val multiplier = (signal.size + repeated.size) / repeated.size
        return if (multiplier > 1) {
            (0 until multiplier).map { repeated }.flatten()
        } else {
            repeated
        }.drop(1).subList(0, signal.size)
    }
}