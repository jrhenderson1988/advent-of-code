package com.github.jrhenderson1988.adventofcode2019.day22

class Deck(val cards: List<Int>) {
    fun cut(c: Int) =
        Deck(
            listOf(
                cards.subList(c + if (c >= 0) 0 else cards.size, cards.size),
                cards.subList(0, c + if (c >= 0) 0 else cards.size)
            ).flatten()
        )

    fun dealWithIncrement(increment: Int): Deck {
        val size = cards.size
        val newCards = cards.indices.map { -1 }.toMutableList()
        cards.forEachIndexed { index, card ->
            val position = (index * increment) % size
            if (newCards[position] != -1) {
                error("Overlapping position $position")
            }

            newCards[position] = card
        }

        return Deck(newCards)
    }

    fun dealIntoNewStack() = Deck(cards.reversed())

    fun positionOf(card: Int) = cards.indexOf(card)

    fun execute(instruction: String) =
        when {
            instruction.startsWith(DEAL_WITH_INCREMENT) -> dealWithIncrement(parseParam(instruction))
            instruction.startsWith(CUT) -> cut(parseParam(instruction))
            instruction.startsWith(DEAL_INTO_NEW_STACK) -> dealIntoNewStack()
            else -> error("Invalid instruction")
        }

    fun execute(instructions: List<String>) =
        instructions.fold(this) { deck, instruction -> deck.execute(instruction) }

    override fun equals(other: Any?) =
        if (other is Deck) {
            other.cards == cards
        } else {
            super.equals(other)
        }

    override fun toString() = "Deck { $cards }"

    override fun hashCode(): Int {
        return cards.hashCode()
    }

    operator fun get(i: Int) = cards[i]

    companion object {
        fun create(range: IntProgression) = Deck(range.map { it })

        fun parseParam(input: String) = input.trim().split(' ').last().toInt()

        const val DEAL_WITH_INCREMENT = "deal with increment"
        const val CUT = "cut"
        const val DEAL_INTO_NEW_STACK = "deal into new stack"
    }
}