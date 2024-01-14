package com.github.jrhenderson1988.adventofcode2019.day22

import java.math.BigInteger

/**
 * Solution came primarily from this insight:
 * https://www.reddit.com/r/adventofcode/comments/ee0rqi/comment/fbnkaju/
 */
class BigDeck(
    private val offset: BigInteger,
    private val increment: BigInteger,
    private val cards: BigInteger
) {
    private fun dealIntoNewStack() =
        BigDeck(
            this.offset.add(this.increment.multiply(BigInteger("-1")).mod(this.cards))
                .add(this.cards).mod(this.cards),
            this.increment.multiply(BigInteger("-1")).mod(this.cards),
            this.cards
        )

    private fun cut(n: BigInteger) =
        BigDeck(this.offset.add(this.increment.multiply(n)).mod(cards), this.increment, this.cards)

    private fun dealWithIncrement(n: BigInteger) =
        BigDeck(offset, inv(n).multiply(this.increment).mod(this.cards), cards)

    private fun inv(n: BigInteger) = n.modPow((this.cards.subtract(BigInteger.TWO)), this.cards)

    fun execute(instruction: String) = when {
        instruction.startsWith(DEAL_WITH_INCREMENT) -> dealWithIncrement(parseParam(instruction))
        instruction.startsWith(CUT) -> cut(parseParam(instruction))
        instruction.startsWith(DEAL_INTO_NEW_STACK) -> dealIntoNewStack()
        else -> error("Invalid instruction")
    }

    fun execute(instructions: List<String>) =
        instructions.fold(this) { deck, instruction -> deck.execute(instruction) }

    fun shuffle(instructions: List<String>, times: BigInteger): BigDeck {
        val nd = this.execute(instructions)
        val increment = nd.increment.modPow(times, nd.cards)
        val offset = nd.offset.multiply(BigInteger.ONE.subtract(increment))
            .multiply(inv(BigInteger.ONE.subtract(nd.increment).mod(nd.cards)))
        val modOffset = offset.mod(cards)
        return BigDeck(modOffset, increment, nd.cards)
    }

    fun get(n: BigInteger): BigInteger = this.offset.add(n.multiply(this.increment)).mod(this.cards)

    override fun toString() = "($offset, $increment, $cards)"

    companion object {
        const val DEAL_WITH_INCREMENT = "deal with increment"
        const val CUT = "cut"
        const val DEAL_INTO_NEW_STACK = "deal into new stack"

        fun parseParam(input: String) = input.trim().split(' ').last().toBigInteger()
    }
}