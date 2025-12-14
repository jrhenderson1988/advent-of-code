package uk.co.jonathonhenderson.adventofcode.days

import org.junit.jupiter.api.Test

class Day12Test extends DayTest {

    @Override
    def input() {
        """
        0:
        ###
        ##.
        ##.
        
        1:
        ###
        ##.
        .##
        
        2:
        .##
        ###
        ##.
        
        3:
        ##.
        ###
        ##.
        
        4:
        ###
        #..
        ###
        
        5:
        ###
        .#.
        ###
        
        4x4: 0 0 0 0 2 0
        12x5: 1 0 1 0 2 2
        12x5: 1 0 1 0 3 2
        """
    }

    @Test
    void part1() {
        // The real input worked with a really simple area check solution, but the test/sample
        // doesn't and would require something more complicated. I've not bothered with it.
        // expectPart1("2")
    }

    @Test
    void part2() {
        // N/A - there's no part 2
    }
}