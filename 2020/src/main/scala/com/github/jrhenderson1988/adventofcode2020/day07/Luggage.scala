package com.github.jrhenderson1988.adventofcode2020.day07

case class Luggage(bags: Map[String, Map[String, Int]]) {
  def totalBagsWhichCanCarry(target: String): Int = {
    var parents = Set[String]()
    var queue = Set(target)
    while (queue.nonEmpty) {
      val p = parentsOf(queue)
      queue = p
      parents = parents.union(p)
    }

    parents.size
  }

  def totalBagsRequiredInside(bag: String, qty: Int): Int = {
    var total = 0
    for ((requiredBag, requiredQty) <- bags(bag)) {
      total += qty * requiredQty
      total += totalBagsRequiredInside(requiredBag, qty * requiredQty)
    }

    total
  }

  private def parentsOf(items: Set[String]): Set[String] = {
    bags
      .filter {
        case (_, children) =>
          children.exists {
            case (child, _) => items.contains(child)
          }
      }
      .keySet
  }
}

object Luggage {
  def parse(lines: List[String]): Luggage = {
    Luggage(lines.map(parseLine).toMap)
  }

  def parseLine(line: String): (String, Map[String, Int]) = {
    val parts = line.split("bags contain").toList
    if (parts.size == 1) {
      throw new RuntimeException
    }

    val bag = parts.head.trim
    if (parts(1).trim.startsWith("no other")) {
      return (bag, Map())
    }

    (
      bag,
      parts(1)
        .stripSuffix(".")
        .split(", ")
        .map(_.trim.split(" ").toList)
        .map(item => (item.drop(1).dropRight(1).mkString(" ").trim, item.head.toInt))
        .toMap
    )
  }
}
