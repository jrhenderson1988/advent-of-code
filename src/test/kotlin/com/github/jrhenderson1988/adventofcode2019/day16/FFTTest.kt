package com.github.jrhenderson1988.adventofcode2019.day16

import org.junit.Assert.assertEquals
import org.junit.Test

class FFTTest {
    @Test
    fun applyPhases() =
        mapOf(Pair("12345678", 4) to "01029498")
            .forEach { (input, expected) ->
                val (signal, phases) = input
                assertEquals(expected.map { it.toString().toInt() }, FFT.parse(signal).applyPhases(phases))
            }

    @Test
    fun applyPhasesAndTrim() {
        mapOf(
            Triple("80871224585914546619083218645595", 100, 8) to "24176176",
            Triple("19617804207202209144916044189917", 100, 8) to "73745418",
            Triple("69317163492948606335995924319873", 100, 8) to "52432133"
        ).forEach { (input, expected) ->
            val (signal, phases, trimTo) = input

            assertEquals(
                expected.map { it.toString().toInt() },
                FFT.parse(signal).applyPhasesAndTrim(phases, trimTo)
            )
        }
    }

    @Test
    fun signalAt() =
        mapOf(
            Pair(Pair("03036732577212944063491565474664", 10000), Triple(100, 303673, 8)) to "84462026",
            Pair(Pair("02935109699940807407585447034323", 10000), Triple(100, 293510, 8)) to "78725270",
            Pair(Pair("03081770884921959731165446850517", 10000), Triple(100, 308177, 8)) to "53553731"
        ).forEach { (input, expected) ->
            val (constructorArgs, methodArgs) = input
            val (signal, repetitions) = constructorArgs
            val (phases, offset, length) = methodArgs

            assertEquals(
                expected.map { it.toString().toInt() },
                FFT.parse(signal, repetitions).signalAt(phases, offset, length)
            )
        }
}