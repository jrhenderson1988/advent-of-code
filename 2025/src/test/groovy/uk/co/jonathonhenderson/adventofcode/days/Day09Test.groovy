package uk.co.jonathonhenderson.adventofcode.days

import org.junit.jupiter.api.Test

class Day09Test extends DayTest {
    @Override
    def input() {
        """
        7,1
        11,1
        11,7
        9,7
        9,5
        2,5
        2,3
        7,3
        """
    }

    @Test
    void part1() {
        expectPart1("50")
    }

    @Test
    void part2() {
        expectPart2("24")
    }
}