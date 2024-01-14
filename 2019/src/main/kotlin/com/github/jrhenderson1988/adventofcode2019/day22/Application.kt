package com.github.jrhenderson1988.adventofcode2019.day22

import com.github.jrhenderson1988.adventofcode2019.readFileAsLines
import java.math.BigInteger

class Application {
    fun part1(args: Array<String>) =
        Deck.create(0..10006).execute(readFileAsLines(args.first())).positionOf(2019)

    fun part2(args: Array<String>) =
        BigDeck(BigInteger.ZERO, BigInteger.ONE, BigInteger("119315717514047")).shuffle(
            readFileAsLines(args.first()),
            BigInteger("101741582076661")
        ).get(BigInteger("2020"))
}