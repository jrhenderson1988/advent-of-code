package com.github.jrhenderson1988.adventofcode2019.day8

import org.junit.Assert.assertEquals
import org.junit.Test

class ImageTest {
    @Test
    fun buildLayers() {
        val layers = Image.buildLayers("123456789012", 3, 2)
        assertEquals(2, layers.size)

        val first = layers.first()
        assertEquals(listOf(1, 2, 3, 4, 5, 6), first.pixels)
        assertEquals(3, first.width)
        assertEquals(2, first.height)

        val second = layers.last()
        assertEquals(listOf(7, 8, 9, 0, 1, 2), second.pixels)
        assertEquals(3, second.width)
        assertEquals(2, second.height)
    }

    @Test
    fun calculateChecksum() =
        mapOf(Pair("112112002112010101", Pair(3, 3)) to 12)
            .forEach { (input, expected) ->
                assertEquals(expected, Image(input.first, input.second.first, input.second.second).calculateChecksum())
            }
}