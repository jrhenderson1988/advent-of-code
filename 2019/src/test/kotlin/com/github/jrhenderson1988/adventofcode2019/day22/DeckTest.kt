package com.github.jrhenderson1988.adventofcode2019.day22

import org.junit.Assert.assertEquals
import org.junit.Test

class DeckTest {
    @Test
    fun cut() =
        mapOf(
            Pair(Deck.create(0..9), 3) to Deck(listOf(3, 4, 5, 6, 7, 8, 9, 0, 1, 2)),
            Pair(Deck.create(0..9), -4) to Deck(listOf(6, 7, 8, 9, 0, 1, 2, 3, 4, 5))
        ).forEach { (input, expected) ->
            assertEquals(expected, input.first.cut(input.second))
        }

    @Test
    fun dealWithIncrement() =
        mapOf(
            Pair(Deck.create(0..9), 3) to Deck(listOf(0, 7, 4, 1, 8, 5, 2, 9, 6, 3))
        ).forEach { (input, expected) ->
            assertEquals(expected, input.first.dealWithIncrement(input.second))
        }

    @Test
    fun dealIntoNewStack() =
        mapOf(
            Deck.create(0..9) to Deck.create(9 downTo 0),
            Deck.create(9 downTo 0) to Deck.create(0..9)
        ).forEach { (input, expected) ->
            assertEquals(expected, input.dealIntoNewStack())
        }

    @Test
    fun execute() =
        mapOf(
            Pair(Deck.create(0..9), "deal into new stack") to Deck.create(9 downTo 0),
            Pair(Deck.create(0..9), "cut 3") to Deck(listOf(3, 4, 5, 6, 7, 8, 9, 0, 1, 2)),
            Pair(Deck.create(0..9), "cut -4") to Deck(listOf(6, 7, 8, 9, 0, 1, 2, 3, 4, 5)),
            Pair(Deck.create(0..9), "deal with increment 3") to Deck(listOf(0, 7, 4, 1, 8, 5, 2, 9, 6, 3))
        ).forEach { (input, expected) ->
            assertEquals(expected, input.first.execute(input.second))
        }

    @Test
    fun executeMany() = mapOf(
        Pair(
            Deck.create(0..9),
            listOf(
                "deal with increment 7",
                "deal into new stack",
                "deal into new stack"
            )
        ) to Deck(listOf(0, 3, 6, 9, 2, 5, 8, 1, 4, 7)),
        Pair(
            Deck.create(0..9),
            listOf(
                "cut 6",
                "deal with increment 7",
                "deal into new stack"
            )
        ) to Deck(listOf(3, 0, 7, 4, 1, 8, 5, 2, 9, 6)),
        Pair(
            Deck.create(0..9),
            listOf(
                "deal with increment 7",
                "deal with increment 9",
                "cut -2"
            )
        ) to Deck(listOf(6, 3, 0, 7, 4, 1, 8, 5, 2, 9)),
        Pair(
            Deck.create(0..9),
            listOf(
                "deal into new stack",
                "cut -2",
                "deal with increment 7",
                "cut 8",
                "cut -4",
                "deal with increment 7",
                "cut 3",
                "deal with increment 9",
                "deal with increment 3",
                "cut -1"
            )
        ) to Deck(listOf(9, 2, 5, 8, 1, 4, 7, 0, 3, 6))
    ).forEach { (input, expected) ->
        assertEquals(expected, input.first.execute(input.second))
    }

    @Test
    fun positionOf() =
        mapOf(
            Pair(Deck.create(0..9), 7) to 7,
            Pair(Deck.create(9 downTo 0), 7) to 2,
            Pair(Deck.create(9 downTo 0), 5) to 4
        ).forEach { (input, expected) ->
            assertEquals(expected, input.first.positionOf(input.second))
        }
}