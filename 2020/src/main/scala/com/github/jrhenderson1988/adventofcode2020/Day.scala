package com.github.jrhenderson1988.adventofcode2020

import java.io.File

trait Day {
  def run(path: File): Option[Answer]

  def fileToString(file: File): String = {
    val source = scala.io.Source.fromFile(file)
    try {
      source.mkString
    } finally {
      source.close()
    }
  }

  def fileAsLines(file: File): List[String] = {
    val source = scala.io.Source.fromFile(file)
    try {
      source.getLines.toList
    } finally {
      source.close()
    }
  }
}
