package com.github.jrhenderson1988.adventofcode2019

fun getResourceReader(filename: String) = {}.javaClass.getResourceAsStream(filename)?.bufferedReader()

fun readResourceLines(filename: String) = getResourceReader(filename)?.readLines()?.toList()

fun readResource(filename: String) = getResourceReader(filename)?.readText()