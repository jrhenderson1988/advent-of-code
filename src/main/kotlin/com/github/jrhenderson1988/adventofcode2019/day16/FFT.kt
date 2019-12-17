package com.github.jrhenderson1988.adventofcode2019.day16

import kotlin.math.abs

class FFT(private val signal: List<Int>, private val repetitions: Int = 1) {
    fun signalAt(phases: Int, offset: Int, length: Int = 8): List<Int> {
        val repeated = (0 until repetitions).map { signal }.flatten().drop(offset)
        val applied = (0 until phases).fold(repeated) { input, _ -> next(input) }

        return applied.subList(0, length)
    }

    private fun next(input: List<Int>): List<Int> {
        val next = input.toMutableList()

        (next.size - 1 downTo 0).forEach { i -> next[i] = (next[i] + next.getOrElse(i + 1) { 0 }) % 10 }

        return next.toList()
    }


    fun applyPhases(n: Int): List<Int> {
        return (0 until n).fold(signal) { input, _ ->
            (input.indices)
                .map { i ->
                    abs((input.indices).map { j -> input[j] * pattern(i)[j] }.sum() % 10)
                }
        }
    }

    fun applyPhasesAndTrim(phases: Int, trimTo: Int) = applyPhases(phases).subList(0, trimTo)

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

    companion object {
        fun parse(input: String, repetitions: Int = 1) = FFT(input.map { it.toString().toInt() }, repetitions)
    }
}