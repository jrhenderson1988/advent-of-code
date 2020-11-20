package com.github.jrhenderson1988.adventofcode2020

import java.io.File

trait Day {
  def run(path: File): Option[Answer]
}
