package uk.co.jonathonhenderson.adventofcode.days

import org.junit.jupiter.api.Test

class Day01Test extends DayTest {
    @Override
    def input() {
        """
        L68
        L30
        R48
        L5
        R60
        L55
        L1
        L99
        R14
        L82
        """.trim()
    }

    @Test
    void part1() {
        expectPart1("3")
    }

    @Test
    void part2() {
        expectPart2("6")
    }
}