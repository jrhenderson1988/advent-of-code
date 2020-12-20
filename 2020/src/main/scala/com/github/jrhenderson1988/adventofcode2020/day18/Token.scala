package com.github.jrhenderson1988.adventofcode2020.day18

sealed trait Token

case class Number(value: Long) extends Token

case class Addition() extends Token

case class Multiplication() extends Token

case class OpeningParenthesis() extends Token

case class ClosingParenthesis() extends Token

