package com.github.jrhenderson1988.adventofcode2020.day20

import com.github.jrhenderson1988.adventofcode2020.day20.Tile.edgeToNumber

import scala.math.pow

sealed case class Edge(index: Int)

object Edge {

  object TOP extends Edge(0)

  object RIGHT extends Edge(1)

  object BOTTOM extends Edge(2)

  object LEFT extends Edge(3)

}


case class Tile(id: Long, cells: List[List[Boolean]]) {
  private val edgeSequences = List(
    cells.head, // Top row, left to right
    cells.map(_.last), // Right column, top to bottom
    cells.last.reverse, // Bottom row, right to left
    cells.reverse.map(_.head) // Left column, bottom to top
  )
  val edges: List[Int] = edgeSequences.map(edge => edgeToNumber(edge))
  val opposites: List[Int] = edgeSequences.map(edge => edgeToNumber(edge.reverse))
  val allEdges: List[Int] = edges ++ opposites

  def getEdge(edge: Edge): Int = {
    edges(edge.index)
  }

  def getOppositeEdge(edge: Edge): Int = {
    opposites(edge.index)
  }

  def hasMatchingEdgeFor(edge: Int): Boolean = edges.contains(edge) || opposites.contains(edge)

  def rotate(times: Int): Tile = (0 until times).foldLeft(this) { (tile, _) => Tile(tile.id, tile.cells.transpose.map(_.reverse)) }

  def flip(): Tile = Tile(id, cells.map(row => row.reverse))

  def totalMatchingEdges(others: List[Tile]): Int =
    edges.count { edge => others.exists(other => other.hasMatchingEdgeFor(edge)) }

  def withoutBorder(): Tile = {
    Tile(id, cells.slice(1, cells.size - 1).map(row => row.slice(1, row.size - 1)))
  }

  override def hashCode(): Int = id.hashCode()

  override def toString: String = {
    val lines = cells
      .map { line =>
        line
          .map { t =>
            if (t) '#' else '.'
          }
          .mkString("")
      }
      .mkString("\n")
    s"Tile: $id\n$lines"
  }
}

object Tile {
  def parse(input: String): Tile = {
    val lines = input.trim.linesIterator.toList

    Tile(
      lines.head.split(" ").last.trim.stripSuffix(":").toLong,
      lines
        .drop(1)
        .map { line =>
          line
            .trim
            .toCharArray
            .toList
            .map {
              case '#' => true
              case '.' => false
              case _ => throw new RuntimeException
            }
        }
    )
  }

  def edgeToNumber(edge: List[Boolean]): Int =
    edge
      .reverse
      .zipWithIndex
      .map { case (value, index) => if (value) pow(2, index.toDouble).toInt else 0 }
      .sum
}