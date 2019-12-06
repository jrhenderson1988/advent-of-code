package com.github.jrhenderson1988.adventofcode2019.day6

import org.junit.Assert.assertEquals
import org.junit.Test

class OrbitMapTest {
    @Test
    fun calculateChecksum() {
        mapOf(listOf("COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J", "J)K", "K)L") to 42)
            .forEach { (input, expected) ->
                assertEquals(expected, OrbitMap(input).calculateChecksum())
            }
    }

    @Test
    fun calculateMinimumOrbitalTransfersBetween() {
        mapOf(
            listOf("COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J", "J)K", "K)L", "K)YOU", "I)SAN") to 4
        )
            .forEach { (input, expected) ->
                assertEquals(expected, OrbitMap(input).calculateMinimumOrbitalTransfersBetween("YOU", "SAN"))
            }
    }
}
