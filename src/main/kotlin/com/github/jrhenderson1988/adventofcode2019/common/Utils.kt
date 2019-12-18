package com.github.jrhenderson1988.adventofcode2019.common

fun dijkstra(points: Set<Pair<Int, Int>>, source: Pair<Int, Int>, target: Pair<Int, Int>): List<Pair<Int, Int>>? {
    val q = points.toMutableSet()
    val dist = q.map { it to Int.MAX_VALUE }.toMap().toMutableMap()
    val prev: MutableMap<Pair<Int, Int>, Pair<Int, Int>?> = q.map { it to null }.toMap().toMutableMap()

    dist[source] = 0
    while (q.isNotEmpty()) {
        val u = dist.filter { q.contains(it.key) }.minBy { it.value }?.key ?: error("Q is empty")
        q.remove(u)

        if (u == target) {
            val s = mutableListOf<Pair<Int, Int>>()
            var n: Pair<Int, Int>? = target
            if (prev[n] != null || n == source) {
                while (n != null) {
                    s.add(n)
                    n = prev[n]
                }
            }

            return s.reversed().toList()
        }

        val neighbours = Direction.neighboursOf(u).filter { q.contains(it) }
        for (v in neighbours) {
            val alt = (dist[u] ?: 0) + 1
            if (alt < dist[v]!!) {
                dist[v] = alt
                prev[v] = u
            }
        }
    }

    return null
}