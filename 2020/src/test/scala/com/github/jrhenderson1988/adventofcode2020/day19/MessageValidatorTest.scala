package com.github.jrhenderson1988.adventofcode2020.day19

import org.scalatest.FunSuite

class MessageValidatorTest extends FunSuite {
  val inputA: String = "0: 1 2\n1: \"a\"\n2: 1 3 | 3 1\n3: \"b\"\n\naab\naba\naaa"
  val inputB: String = "0: 4 1 5\n1: 2 3 | 3 2\n2: 4 4 | 5 5\n3: 4 5 | 5 4\n4: \"a\"\n5: \"b\"\n\nababbb\nbababa\nabbbab\naaabbb\naaaabbb"
  val mvA: MessageValidator = MessageValidator.parse(inputA)
  val mvB: MessageValidator = MessageValidator.parse(inputB)

  test("matches") {
    assert(mvA.matches("aab"))
    assert(mvA.matches("aba"))
    assert(!mvA.matches("aaa"))
  }

  test("totalMessagesThatMatchRule0") {
    assert(mvA.totalMatches() == 2)
    assert(mvB.totalMatches() == 2)
  }
}
