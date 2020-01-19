package com.github.jrhenderson1988.adventofcode2019.day18

import com.github.jrhenderson1988.adventofcode2019.readFileAsString

class Application {
    fun part1(args: Array<String>) = Vault.parse(readFileAsString(args.first())).shortestPathToCollectAllKeys()
    fun part2(args: Array<String>) = QuadVault.parse(readFileAsString(args.first())).shortestPathToCollectAllKeys()
}