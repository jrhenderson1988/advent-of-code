package com.github.jrhenderson1988.adventofcode2020.day23

import java.lang.Math.min

case class Game(currentIndex: Int, cups: List[Int]) {
  def play(rounds: Int): Int = (0 until rounds).foldLeft(this) { (game, _) => game.round() }.result()

  def round(): Game = {
    val (selection, remaining) = select()

    Game(
      (currentIndex + 1) % cups.size,
      placeSelection(destination(remaining), remaining, selection)
    )
  }

  private def select(): (List[Int], List[Int]) = {
    val size = cups.size
    val indices = List((currentIndex + 1) % size, (currentIndex + 2) % size, (currentIndex + 3) % size)
    (
      indices.map(index => cups(index)),
      cups.zipWithIndex.filter { case (_, idx) => !indices.contains(idx) }.map { case (cup, _) => cup }
    )
  }

  private def destination(remaining: List[Int]): Int = {
    val current = cups(currentIndex)
    remaining.indexOf(
      if (current > remaining.min) {
        (remaining.min until current).reverse.find(cup => remaining.contains(cup)).get
      } else {
        remaining.max
      }
    )
  }

  private def placeSelection(dest: Int, remaining: List[Int], selection: List[Int]): List[Int] = {
    val shift = min(cups.size - 1 - currentIndex, 3)
    var newCups = remaining.slice(0, dest + 1) ++ selection ++ remaining.slice(dest + 1, remaining.size)
    newCups = if (dest < currentIndex) {
//      remaining.slice(shift, dest + 1) ++
//        selection ++
//        remaining.slice(dest + 1, remaining.size) ++
//        remaining.slice(0, shift)
      newCups.drop(shift) ++ newCups.take(shift)
    } else {
      remaining.slice(0, dest + 1) ++ selection ++ remaining.slice(dest + 1, remaining.size)
    }

    newCups
  }

  def result(): Int = {
    val index = cups.indexOf(1)
    (cups.slice(index + 1, cups.size) ++ cups.slice(0, index)).mkString("").toInt
  }

  override def toString: String =
    cups
      .zipWithIndex
      .map { case (cup, i) => if (i == currentIndex) s"($cup)" else s" $cup " }
      .mkString("")
      .trim
}

object Game {
  def parse(input: String): Game = {
    Game(0, input.toCharArray.map(_.toString.toInt).toList)
  }
}
