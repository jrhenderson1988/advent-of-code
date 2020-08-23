package com.github.jrhenderson1988.adventofcode2019.common

import java.util.*

fun <T>dijkstra(points: Iterable<T>, source: T, target: T, neighbours: (T) -> Iterable<T>): List<T>? {
    val q = points.toMutableSet()
    val dist = q.map { it to Int.MAX_VALUE }.toMap().toMutableMap()
    val prev: MutableMap<T, T?> = q.map { it to null }.toMap().toMutableMap()

    dist[source] = 0
    while (q.isNotEmpty()) {
        val u = dist.filter { q.contains(it.key) }.minBy { it.value }?.key ?: error("Q is empty")
        q.remove(u)

        if (u == target) {
            val s = mutableListOf<T>()
            var n: T? = target
            if (prev[n] != null || n == source) {
                while (n != null) {
                    s.add(n)
                    n = prev[n]
                }
            }

            return s.reversed().toList()
        }

        for (v in neighbours(u).filter { q.contains(it) }) {
            val alt = (dist[u] ?: 0) + 1
            if (alt < dist[v]!!) {
                dist[v] = alt
                prev[v] = u
            }
        }
    }

    return null
}

fun dijkstra(points: Set<Pair<Int, Int>>, source: Pair<Int, Int>, target: Pair<Int, Int>) =
    dijkstra(points, source, target) { point -> Direction.neighboursOf(point) }

fun <T : Any>bfs(source: T, target: T, neighbours: (T) -> Iterable<T>): List<T>? {
    val q = ArrayDeque<T>()
    val discovered = mutableSetOf<T>()
    val prev = mutableMapOf<T, T>()

    discovered.add(source)
    q.add(source)

    while (q.isNotEmpty()) {
        val v = q.poll()
        if (v == target) {
            return generateSequence(target, { prev.getOrDefault(it, null)})
                .toList()
                .reversed()
        }

        neighbours(v).filter { !discovered.contains(it) }
            .forEach {
                discovered.add(it)
                prev[it] = v
                q.add(it)
            }
    }

    return null
}

fun powerOf(num: Long, exponent: Long): Long = if (exponent == 0L) 1L else num * powerOf(num, exponent - 1L)

fun <T> generateCombinations(items: Set<T>): Set<Set<T>> {
    val length = items.size

    val combinations = mutableSetOf<Set<T>>()
    for (i in 0 until (1 shl length)) {
        val set = mutableSetOf<T>()
        for (j in 0 until length) {
            if ((i and (1 shl j)) > 0) {
                set.add(items.elementAt(j))
            }
        }

        combinations.add(set)
    }

    return combinations
}