package uk.co.jonathonhenderson.adventofcode.days

import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals

class Day01Test {
    private static String INPUT =
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

    @Test
    void part1() {
        def day = new Day01(INPUT)
        def result = day.part1()
        assertEquals("3", result)
    }

    @Test
    void part2() {
        def day = new Day01(INPUT)
        def result = day.part2()
        assertEquals("6", result)
    }
}