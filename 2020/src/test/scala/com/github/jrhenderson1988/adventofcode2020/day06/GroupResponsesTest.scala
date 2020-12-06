package com.github.jrhenderson1988.adventofcode2020.day06

import org.scalatest.FunSuite

class GroupResponsesTest extends FunSuite {
  val sample = GroupResponses(
    List(
      List(
        Set('a', 'b', 'c')
      ),
      List(
        Set('a'), Set('b'), Set('c')
      ),

      List(
        Set('a', 'b'), Set('a', 'c')
      ),
      List(
        Set('a'), Set('a'), Set('a'), Set('a'),
      ),
      List(
        Set('b')
      )
    )
  )

  test("GroupResponses.parse") {
    assert(GroupResponses.parse("abc\n\na\nb\nc\n\nab\nac\n\na\na\na\na\n\nb") == sample)
  }

  test("GroupResponses.totalAnyoneAnsweredYes") {
    assert(sample.totalAnyoneAnsweredYes() == 11)
  }

  test("GroupResponses.totalEveryoneAnsweredYes") {
    assert(sample.totalEveryoneAnsweredYes() == 6)
  }
}
