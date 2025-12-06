package uk.co.jonathonhenderson.adventofcode.days

import org.junit.jupiter.api.Test

class Day06Test extends DayTest {
    @Override
    def input() {
        """
        123 328  51 64 
         45 64  387 23 
          6 98  215 314
        *   +   *   +  
        """
    }

    @Test
    void part1() {
        expectPart1("4277556")
    }

    @Test
    void part2() {
        expectPart2("3263827")
    }
}