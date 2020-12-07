package com.github.jrhenderson1988.adventofcode2020.day07

//case class Bag(name: String, children: Map[String, Int]) {
//  def canHoldAny(targets: Iterable[String]): Boolean = {
//    targets.exists(target => children.contains(target))
//  }
//}
//
//object Bag {
//  def parse(input: String): Bag = {
//    val parts = input.split("bags contain").toList
//    if (parts.size == 1) {
//      throw new RuntimeException
//    }
//
//    val bag = parts.head.trim
//    if (parts(1).trim.startsWith("no other")) {
//      return Bag(bag, Map())
//    }
//
//    Bag(
//      bag,
//      parts(1)
//        .stripSuffix(".")
//        .split(", ")
//        .map(_.trim.split(" ").toList)
//        .map(item => (item.drop(1).dropRight(1).mkString(" ").trim, item.head.toInt))
//        .toMap
//    )
//  }
//}