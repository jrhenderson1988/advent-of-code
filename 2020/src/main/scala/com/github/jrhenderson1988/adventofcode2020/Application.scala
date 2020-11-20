package com.github.jrhenderson1988.adventofcode2020

import java.io.File

import scala.util.Try

class Application(private val args: Array[String]) {
  def run(): Unit = {
    println(
      """    _      _             _    ___   __  ___         _       ___ __ ___ __
        |   /_\  __| |_ _____ _ _| |_ / _ \ / _|/ __|___  __| |___  |_  )  \_  )  \
        |  / _ \/ _` \ V / -_) ' \  _| (_) |  _| (__/ _ \/ _` / -_)  / / () / / () |
        | /_/ \_\__,_|\_/\___|_||_\__|\___/|_|  \___\___/\__,_\___| /___\__/___\__/
        | """.stripMargin
    )

    if (args.length < 2) {
      throw new IllegalArgumentException("expected 2 arguments: <day> <input-file>")
    }

    val dayArg = Try(args(0).toInt).toOption
    if (dayArg.isEmpty) {
      throw new IllegalArgumentException("Day argument must be an integer")
    }
    val day = dayArg.get

    val file = new File(args(1))
    if (!file.canRead) {
      throw new IllegalArgumentException(s"Input file is not readable: ${file.getCanonicalPath}")
    }

    println(s"===== Day $day =====\n")

    val runnerClass = Class.forName(s"${this.getClass.getPackageName}.day${if (day < 10) s"0$day" else day}.Runner")
    val runner = runnerClass.getDeclaredConstructor().newInstance()
    if (!runner.isInstanceOf[Day]) {
      throw new RuntimeException
    }

    val start = System.currentTimeMillis()
    val result = runner.asInstanceOf[Day].run(file)
    val end = System.currentTimeMillis()

    if (result.isEmpty) {
      println("Not yet implemented")
    } else {
      println(s"Part 1: ${result.get.part1}")
      println(s"Part 2: ${result.get.part1}")
    }

    println("\n-----------------")
    println(s"Runtime: ${end - start}ms")
  }
}

object Application {
  def main(args: Array[String]): Unit = {
    new Application(args).run()
  }
}
