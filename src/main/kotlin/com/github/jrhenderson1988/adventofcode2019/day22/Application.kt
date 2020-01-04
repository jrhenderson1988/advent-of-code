package com.github.jrhenderson1988.adventofcode2019.day22

import com.github.jrhenderson1988.adventofcode2019.readFileAsLines

class Application {
    fun part1(args: Array<String>) = Deck.create(0..10006).execute(readFileAsLines(args.first())).positionOf(2019)
//    fun part2(args: Array<String>) = Deck.create(0..119315717514047).execute(readFileAsLines(args.first())).positionOf(2019)
}