package com.github.jrhenderson1988.adventofcode2019

import java.io.File

fun readFileAsString(name: String): String {
    return File(name).readText()
}

fun readFileAsLines(name: String): List<String> {
    return File(name).readLines()
}