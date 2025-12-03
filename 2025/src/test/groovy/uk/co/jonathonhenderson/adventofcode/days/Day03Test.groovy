package uk.co.jonathonhenderson.adventofcode.days

import org.junit.jupiter.api.Test

class Day03Test extends DayTest {
    @Override
    def input() {
        "987654321111111\n" +
                "811111111111119\n" +
                "234234234234278\n" +
                "818181911112111"
    }

    @Test
    void part1() {
        expectPart1("357")
    }

    @Test
    void part2() {
        expectPart2("3121910778619")
    }
}