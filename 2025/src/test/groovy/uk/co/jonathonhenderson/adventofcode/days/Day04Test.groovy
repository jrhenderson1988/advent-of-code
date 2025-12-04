package uk.co.jonathonhenderson.adventofcode.days

import org.junit.jupiter.api.Test

class Day04Test extends DayTest {
    @Override
    def input() {
        """
        ..@@.@@@@.
        @@@.@.@.@@
        @@@@@.@.@@
        @.@@@@..@.
        @@.@@@@.@@
        .@@@@@@@.@
        .@.@.@.@@@
        @.@@@.@@@@
        .@@@@@@@@.
        @.@.@@@.@.
        """
    }

    @Test
    void part1() {
        expectPart1("13")
    }

    @Test
    void part2() {
        expectPart2("43")
    }
}