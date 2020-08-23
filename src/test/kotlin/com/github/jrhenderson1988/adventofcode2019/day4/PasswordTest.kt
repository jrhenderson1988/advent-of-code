package com.github.jrhenderson1988.adventofcode2019.day4

import org.junit.Assert.assertEquals
import org.junit.Test

class PasswordTest {
    @Test
    fun extractDigits() =
        mapOf(123456 to listOf(1, 2, 3, 4, 5, 6), 182 to listOf(1, 8, 2))
            .forEach { (input, expected) ->
                assertEquals(expected, Password.extractDigits(input))
            }

    @Test
    fun isSixDigits() =
        mapOf(123456 to true, 567890 to true, 12345 to false, 1234567 to false)
            .forEach { (input, expected) ->
                assertEquals(expected, Password(input).isSixDigits())
            }

    @Test
    fun containsAdjacentDigits() =
        mapOf(112345 to true, 123443 to true, 1233 to true, 123456 to false, 1 to false)
            .forEach { (input, expected) ->
                assertEquals(expected, Password(input).containsAdjacentDigits())
            }

    @Test
    fun containsNoDescendingDigits() =
        mapOf(123456 to true, 123 to true, 1 to true, 123454 to false, 21 to false)
            .forEach { (input, expected) ->
                assertEquals(expected, Password(input).containsNoDescendingDigits())
            }

    @Test
    fun containsPair() =
        mapOf(
            112233 to true,
            123444 to false,
            111122 to true,
            124444 to false,
            122222 to false,
            112 to true,
            11 to true,
            111144 to true,
            111222 to false,
            111112 to false
        )
            .forEach { (input, expected) ->
                assertEquals(
                    "Expected $input to be ${if (expected) "true" else "false"}",
                    expected,
                    Password(input).containsPair()
                )
            }
}