package uk.co.jonathonhenderson.adventofcode.days

import org.junit.jupiter.api.Test

class Day05Test extends DayTest {
    @Override
    def input() {
        """
        3-5
        10-14
        16-20
        12-18
        
        1
        5
        8
        11
        17
        32
        """
    }

    @Test
    void part1() {
        expectPart1("3")
    }

    @Test
    void part2() {
        expectPart2("14")
    }
}