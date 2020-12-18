package com.github.jrhenderson1988.adventofcode2020.day17

case class PocketDimension(points: Map[List[Int], Boolean], dimensions: Int) {
  val neighbours: Set[List[Int]] = buildNeighbours(dimensions).diff(Set(List.fill(dimensions)(0)))

  def totalActiveAfterCycles(n: Int): Int = {
    cycles(n).totalActive()
  }

  def cycles(n: Int): PocketDimension = {
    (0 until n).foldLeft(this) { case (pd, _) => pd.cycle() }
  }

  def cycle(): PocketDimension = {
    var newPoints = points.map(p => p)

    points.foreach { case (point, _) =>
      neighbours.foreach { delta =>
        val neighbour = applyDelta(point, delta)
        points.get(neighbour) match {
          case Some(_) => // do nothing
          case None => newPoints += (neighbour -> false)
        }
      }
    }

    newPoints = newPoints.map { case (point, active) =>
      val totalActiveNeighbours = neighbours.count { delta =>
        val neighbour = applyDelta(point, delta)
        newPoints.get(neighbour) match {
          case Some(value) => value
          case None => false
        }
      }

      (point, if (active) {
        totalActiveNeighbours == 2 || totalActiveNeighbours == 3
      } else {
        totalActiveNeighbours == 3
      })
    }

    PocketDimension(newPoints, dimensions)
  }

  def totalActive(): Int = {
    points.count { case (_, active) => active }
  }

  private def applyDelta(point: List[Int], delta: List[Int]): List[Int] = {
    point.zipWithIndex.map { case (value, index) => value + delta(index) }
  }

  private def buildNeighbours(dimension: Int): Set[List[Int]] = {
    if (dimension < 1) {
      throw new RuntimeException
    }

    if (dimension == 1) {
      Set(List(-1), List(0), List(1))
    } else {
      var n = Set[List[Int]]()
      for (a <- Set(List(-1), List(0), List(1))) {
        for (b <- buildNeighbours(dimension - 1)) {
          n += a ++ b
        }
      }

      n
    }
  }
}

object PocketDimension {
  def parse(lines: List[String], dimensions: Int): PocketDimension = {
    PocketDimension(
      lines
        .zipWithIndex
        .flatMap { case (line, y) =>
          line
            .trim
            .toCharArray
            .zipWithIndex
            .map { case (ch, x) => (List(x, y) ++ List.fill(dimensions - 2)(0), ch == '#') }
            .toMap
        }
        .toMap,
      dimensions
    )
  }
}
