package com.github.jrhenderson1988.adventofcode2020.day18

import org.scalatest.FunSuite

class ExpressionTest extends FunSuite {
  test("parse") {
    assert(Expression.parse("2 * 3 + (4 * 5)") == Expression(
      List[Token](
        Number(2),
        Multiplication(),
        Number(3),
        Addition(),
        OpeningParenthesis(),
        Number(4),
        Multiplication(),
        Number(5),
        ClosingParenthesis()
      )
    ))
  }

  test("evaluateLeftToRight") {
    assert(Expression.parse("1 + 2").evaluateLeftToRight().contains(3))
    assert(Expression.parse("2 * 2").evaluateLeftToRight().contains(4))
    assert(Expression.parse("2 * 2 * 2").evaluateLeftToRight().contains(8))
    assert(Expression.parse("2 * 2 * 2 + 3").evaluateLeftToRight().contains(11))
    assert(Expression.parse("2 * 2 + 10 + 3").evaluateLeftToRight().contains(17))
    assert(Expression.parse("2 * 3 + (4 * 5)").evaluateLeftToRight().contains(26))
    assert(Expression.parse("5 + (8 * 3 + 9 + 3 * 4 * 3)").evaluateLeftToRight().contains(437))
    assert(Expression.parse("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))").evaluateLeftToRight().contains(12240))
    assert(Expression.parse("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2").evaluateLeftToRight().contains(13632))
  }

  test("evaluateWithPrecedence") {
    assert(Expression.parse("1 + 2 * 3 + 4 * 5 + 6").evaluateWithPrecedence().contains(231))
    assert(Expression.parse("2 * 3 + (4 * 5)").evaluateWithPrecedence().contains(46))
    assert(Expression.parse("5 + (8 * 3 + 9 + 3 * 4 * 3)").evaluateWithPrecedence().contains(1445))
    assert(Expression.parse("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4)").evaluateWithPrecedence().contains(669060))
    assert(Expression.parse("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2").evaluateWithPrecedence().contains(23340))
  }
}
