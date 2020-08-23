package com.github.jrhenderson1988.adventofcode2019.day21

import com.github.jrhenderson1988.adventofcode2019.readFileAsString

class Application {
    // NOT A J: If the next tile is open, we MUST jump
    // NOT C T: Invert C and put it in T i.e. if the 3rd tile is empty T becomes true
    // AND D T: If D is closed (true) and C is open (false) then T stays true
    // OR T J: If either T or J is true i.e. A is open OR C is open and D is closed, then jump
    fun part1(args: Array<String>) = SpringDroid(IntCodeComputer.parseProgram(readFileAsString(args.first()))).execute(
        """
        NOT A J
        NOT C T
        AND D T
        OR T J
        WALK
        """
    )

    // D && (!A || !B || !C) && (E || H)
    fun part2(args: Array<String>) = SpringDroid(IntCodeComputer.parseProgram(readFileAsString(args.first()))).execute(
        """
        OR A T
        AND B T
        AND C T
        NOT T T
        OR E J
        OR H J
        AND T J
        AND D J
        RUN
        """
    )
}