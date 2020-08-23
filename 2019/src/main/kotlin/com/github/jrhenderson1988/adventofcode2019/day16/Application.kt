package com.github.jrhenderson1988.adventofcode2019.day16

import com.github.jrhenderson1988.adventofcode2019.readFileAsString

class Application {
    fun part1(args: Array<String>) =
        FFT.parse(readFileAsString(args.first()))
            .applyPhasesAndTrim(100, 8)
            .joinToString("")

    fun part2(args: Array<String>): String {
        val input = readFileAsString(args.first())
        val fft = FFT.parse(input, 10000)
        val offset = input.substring(0, 7).toInt()

        return fft
            .signalAt(100, offset, 8)
            .joinToString("")
    }
}