package com.github.jrhenderson1988.adventofcode2020.day16

import scala.util.control.Breaks.{break, breakable}

case class TicketValidator(rules: Map[String, Rule], yourTicket: List[Int], nearbyTickets: List[List[Int]]) {
  def ticketScanningErrorRate(): Int = {
    nearbyTickets
      .map(ticket => ticketErrorRate(ticket))
      .sum
  }

  def departureValuesProduct(): Long = {
    var result = nearbyTickets
      .filter(ticket => ticketErrorRate(ticket) == 0)
      .foldLeft(yourTicket.indices.map(idx => (idx, rules.keys.toSet)).toMap) { case (ruleMap, ticket) =>
        ruleMap.map { case (index, ruleNames) =>
          (index, ruleNames.filter(ruleName => rules(ruleName).valid(ticket(index))))
        }
      }

    var resolved = Map[String, Int]()
    breakable {
      while (result.nonEmpty) {
        val (res, rem) = result.partition { case (_, rules) => rules.size == 1 }
        for (item <- res) {
          resolved += (item._2.head -> item._1)
        }

        result = rem.map { case (index, rules) => (index, rules.diff(resolved.keySet)) }
      }
    }

    resolved
      .filter { case (key, _) => key.startsWith("departure") }
      .map { case (_, index) => yourTicket(index).toLong }
      .product
  }

  private def ticketErrorRate(ticket: List[Int]): Int = {
    ticket
      .map(value => if (!rules.values.exists(rule => rule.valid(value))) value else 0)
      .sum
  }
}

object TicketValidator {
  def parse(input: String): TicketValidator = {
    val segments = input.trim.split("\\n\\s*\\n").toList

    val rules = segments
      .head
      .split("(\\n|\\r\\n|\\r)")
      .toList
      .map(line => {
        val parts = line.split(":")
        (parts.head.trim, Rule.parse(parts.last.trim))
      })
      .toMap

    val segment2Lines = segments(1).split("(\\n|\\r\\n|\\r)").toList
    if (!segment2Lines.head.trim.toLowerCase().startsWith("your ticket")) {
      throw new RuntimeException
    }
    val yourTicket = segment2Lines.last.split(",").toList.map(_.trim.toInt)

    val segment3Lines = segments(2).split("(\\n|\\r\\n|\\r)").toList
    if (!segment3Lines.head.trim.toLowerCase().startsWith("nearby tickets")) {
      throw new RuntimeException
    }

    val nearbyTickets = segment3Lines.drop(1).map(line => line.split(",").toList.map(_.trim.toInt))

    TicketValidator(rules, yourTicket, nearbyTickets)
  }
}


