package com.github.jrhenderson1988.adventofcode2020.day18

import scala.collection.mutable.ListBuffer

case class Expression(tokens: List[Token]) {

  def evaluateLeftToRight(): Option[Long] = {
    var result: Option[Long] = None
    var i = 0
    while (i < tokens.size) {
      val token = tokens(i)
      token match {
        case Number(value) => result = calculateResult(result, i, value)
        case OpeningParenthesis() =>
          val idx = findMatchingClosingParenthesis(i)
          val value = Expression(tokens.slice(i + 1, idx)).evaluateLeftToRight().get
          result = calculateResult(result, i, value)
          i = idx
        case _ => // Skip
      }

      i += 1
    }

    result
  }

  def evaluateWithPrecedence(): Option[Long] = {
    Some(
      applyMultiplications(
        applyAdditions(
          evaluateParentheses(tokens)
        )
      ).head.asInstanceOf[Number].value
    )
  }

  private def applyOperation(current: Long, operator: Token, value: Long): Option[Long] = {
    operator match {
      case Addition() => Some(current + value.toLong)
      case Multiplication() => Some(current * value.toLong)
      case _ => throw new RuntimeException("Previous token expected to be an operator")
    }
  }

  private def findMatchingClosingParenthesis(i: Int): Int = {
    var index = i + 1
    var stack = 0
    while (index < tokens.size) {
      val t = tokens(index)
      t match {
        case OpeningParenthesis() => stack += 1
        case ClosingParenthesis() =>
          if (stack == 0) {
            return index
          } else {
            stack -= 1
          }
        case _ =>
      }
      index += 1
    }

    index
  }

  private def calculateResult(current: Option[Long], index: Int, value: Long): Option[Long] = {
    if (current.isEmpty) {
      Some(value)
    } else {
      applyOperation(current.get, tokens(index - 1), value)
    }
  }

  private def evaluateParentheses(tokens: List[Token]): List[Token] = {
    var result = List[Token]()
    var i = 0
    while (i < tokens.size) {
      val token = tokens(i)
      token match {
        case OpeningParenthesis() =>
          val index = findMatchingClosingParenthesis(i)
          val value = Expression(tokens.slice(i + 1, index)).evaluateWithPrecedence().get
          result = result ++ List(Number(value))
          i = index
        case _ => result = result ++ List(token)
      }
      i += 1
    }

    result
  }

  private def applyAdditions(tokens: List[Token]): List[Token] = {
    applyBinaryOperation(tokens, _ == Addition(), (a, b) => a + b)
  }

  private def applyMultiplications(tokens: List[Token]): List[Token] = {
    applyBinaryOperation(tokens, token => token == Multiplication(), (a, b) => a * b)
  }

  private def applyBinaryOperation(tokens: List[Token], cond: Token => Boolean, fn: (Long, Long) => Long): List[Token] = {
    if (tokens.size < 2) {
      return tokens
    }

    var result = List[Token](tokens.head.asInstanceOf[Number])
    var i = 1
    while (i < tokens.size - 1) {
      result = if (cond(tokens(i))) {
        val evaluated = Number(fn(result.last.asInstanceOf[Number].value, tokens(i + 1).asInstanceOf[Number].value))
        result.dropRight(1) ++ List(evaluated)
      } else {
        result ++ List(tokens(i), tokens(i + 1))
      }
      i += 2
    }

    result
  }
}

object Expression {
  def parse(line: String): Expression = {
    var i = 0
    var tokens = ListBuffer[Token]()
    while (i < line.length) {
      val ch = line(i)
      if (ch == '(') {
        tokens += OpeningParenthesis()
      } else if (ch == ')') {
        tokens += ClosingParenthesis()
      } else if (ch == '+') {
        tokens += Addition()
      } else if (ch == '*') {
        tokens += Multiplication()
      } else if (ch.isDigit) {
        var j = i + 1
        while (j < line.length && line(j).isDigit) {
          j += 1
        }
        tokens += Number(line.substring(i, j).toLong)
        i = j - 1
      }
      i += 1
    }

    Expression(tokens.toList)
  }
}
