package uk.co.jonathonhenderson.adventofcode.days

import org.junit.jupiter.api.Test

class Day11Test extends DayTest {

    @Test
    void part1() {
        def input =
            """
            aaa: you hhh
            you: bbb ccc
            bbb: ddd eee
            ccc: ddd eee fff
            ddd: ggg
            eee: out
            fff: out
            ggg: out
            hhh: ccc fff iii
            iii: out
            """
        expectPart1(input, "5")
    }

    @Test
    void part2() {
        def input =
            """
            svr: aaa bbb
            aaa: fft
            fft: ccc
            bbb: tty
            tty: ccc
            ccc: ddd eee
            ddd: hub
            hub: fff
            eee: dac
            dac: fff
            fff: ggg hhh
            ggg: out
            hhh: out
            """
        expectPart2(input, "2")
    }
}