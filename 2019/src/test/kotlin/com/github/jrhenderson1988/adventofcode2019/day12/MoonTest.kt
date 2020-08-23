package com.github.jrhenderson1988.adventofcode2019.day12

import org.junit.Assert.assertEquals
import org.junit.Test

class MoonTest {
    @Test
    fun parse() =
        mapOf("<x=-1, y=2, z=3>" to Triple(-1, 2, 3))
            .forEach { (input, expected) ->
                val parsed = Moon.parse(input)
                assertEquals(expected, parsed.x)
                assertEquals(expected, parsed.y)
                assertEquals(expected, parsed.z)
            }
}