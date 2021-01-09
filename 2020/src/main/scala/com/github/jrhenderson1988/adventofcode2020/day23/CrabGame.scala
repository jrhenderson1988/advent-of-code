package com.github.jrhenderson1988.adventofcode2020.day23

import scala.math.Numeric.IntIsIntegral

class CrabGame(input: List[Int]) {
  val inputAsVector: Vector[Int] = input.toVector
  val size: Int = inputAsVector.size
  val max: Int = inputAsVector.max
  val min: Int = inputAsVector.min
  var current: Int = inputAsVector.head
  var cups: Map[Int, Int] = (0 until size).map(i => inputAsVector(i) -> inputAsVector((i + 1) % size)).toMap

  def arrangementAfterRounds(rounds: Int): String = {
    play(rounds)

    var l = List[Int]()
    var c = 1
    while (cups(c) != 1) {
      l = l ++ List(cups(c))
      c = cups(c)
    }

    l.mkString("")
  }

  def productOfStarHidingPlaces(rounds: Int): Long = {
    play(rounds)

    cups(1).toLong * cups(cups(1)).toLong
  }

  private def play(rounds: Int): Unit = {
    (0 until rounds).foreach(r => {
      round()
    })
  }

  private def round(): Unit = {
    val selection = List(cups(current), cups(cups(current)), cups(cups(cups(current))))
    val dest = destination(selection)

    cups += current -> cups(selection(2))

    cups += (selection.last -> cups(dest))
    cups += (dest -> selection.head)

    current = cups(current)
  }

  def destination(selection: List[Int]): Int = {
    for (i <- (min until current).reverse) {
      if (!selection.contains(i)) {
        return i
      }
    }

    for (i <- (current + 1 to max).reverse) {
      if (!selection.contains(i)) {
        return i
      }
    }

    throw new RuntimeException
  }
}

object CrabGame {
  def parse(input: String): CrabGame = {
    parse(input.trim, input.trim.length)
  }

  def parse(input: String, padToSize: Int): CrabGame = {
    val numbers = input.toCharArray.map(_.toString.toInt).toList
    val values = numbers ++ (numbers.max + 1 until (padToSize - numbers.size) + numbers.max + 1)

    new CrabGame(values)
  }
}