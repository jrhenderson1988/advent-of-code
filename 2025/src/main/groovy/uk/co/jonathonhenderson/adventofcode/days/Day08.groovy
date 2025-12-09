package uk.co.jonathonhenderson.adventofcode.days

import groovy.transform.Canonical
import uk.co.jonathonhenderson.adventofcode.utils.Pair

class Day08 extends Day {
    private final List<Coord> positions
    private final List<Pair<Pair<Coord, Coord>, Double>> closestDistances

    Day08(String content) {
        super(content.stripIndent().trim())
        this.positions = this.content.readLines().collect { line -> Coord.parse(line.trim()) }
        this.closestDistances = computeClosesDistances(this.positions)
    }

    @Override
    String part1() {
        def closestPairs = this.closestDistances.take(this.isTest ? 10 : 1000).collect { it.first() }

        def circuits = buildCircuits(closestPairs)

        circuits.collect { it.size() as long }
                .sort()
                .reverse()
                .take(3)
                .inject { a, b -> a * b }
                .toString()
    }

    @Override
    String part2() {
        def finalConnection = findFinalConnection(this.closestDistances.collect { it.first() })
        (finalConnection.first().x * finalConnection.second().x).toString()

    }

    private static List<Pair<Pair<Coord, Coord>, Double>> computeClosesDistances(List<Coord> positions) {
        def distances = [:]

        for (int i = 0; i < positions.size(); i++) {
            for (int j = i + 1; j < positions.size(); j++) {
                def a = positions.get(i)
                def b = positions.get(j)
                def key = Pair.of(a, b)
                distances.put(key, a.straightLineDistanceTo(b))
            }
        }

        distances.collect { k, v -> Pair.of(k, v) }.sort { pair -> pair.second() } as List<Pair<Pair<Coord, Coord>, Double>>
    }

    private List<Set<Coord>> buildCircuits(List<Pair<Coord, Coord>> connections) {
        mergeSets(connections.collect { pair -> Set.of(pair.first(), pair.second()) })
    }

    private static List<Set<Coord>> mergeSets(List<Set<Coord>> sets) {
        def processed = sets.collect { false }

        def newSets = []
        for (int i = 0; i < sets.size(); i++) {
            if (processed[i]) {
                continue
            }

            def merged = new HashSet<>(sets.get(i))
            processed[i] = true

            while (true) {
                def changes = false
                for (int j = i + 1; j < sets.size(); j++) {
                    def current = sets.get(j)
                    if (!processed[j] && !merged.disjoint(current)) {
                        merged = merged.union(current)
                        processed[j] = true
                        changes = true
                    }
                }

                if (!changes) {
                    break
                }
            }

            newSets.add(merged)
        }

        newSets
    }

//    private Pair<Coord, Coord> findFinalConnection(List<Pair<Coord, Coord>> connections) {
//        //println("Total connections: ${connections.size()}")
//        def targetSize = this.positions.size()
//        def circuits = []
//        int i = 0
//        for (def connection in connections) {
//            //if (i % 100 == 0) {
//            //    println("$i...")
//            //}
//            def set = [connection.first(), connection.second()].toSet()
//            circuits.add(set)
//            circuits = mergeSets(circuits)
//            if (circuits.size() == 1 && circuits.get(0).size() == targetSize) {
//                return connection
//            }
//            i++
//        }
//
//        null
//    }

    private Pair<Coord, Coord> findFinalConnection(List<Pair<Coord, Coord>> connections) {
        def targetSize = this.positions.size()

        def sets = connections.collect { [it.first(), it.second()].toSet() }
        def circuits = [sets.get(0)]
        for (int i = 1; i < sets.size(); i++) {
            def item = sets.get(i)
            def indices = circuits.findIndexValues { circuit -> intersects(circuit, item) }
            if (indices.size() == 0) {
                circuits.push(item)
            } else if (indices.size() == 1) {
                circuits.get(indices[0] as int).addAll(item)
            } else if (indices.size() > 1) {
                Set<Coord> union = indices.collect { idx -> circuits[idx] }.inject(item) { a, b -> a.union(b) }
                for (def idx in indices.reversed()) {
                    circuits.remove(idx as int)
                }
                circuits.add(union)
            }

            if (circuits.size() == 1 && circuits[0].size() == targetSize) {
                return connections.get(i)
            }
        }

        null
    }

    private static boolean intersects(Set<Coord> a, Set<Coord> b) {
        return !a.disjoint(b)
    }

    @Canonical
    static class Coord {
        final long x
        final long y
        final long z

        static Coord of(long x, long y, long z) {
            new Coord(x, y, z)
        }

        static Coord parse(String line) {
            def parts = line.split(",")
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid 3D coordinate: $line")
            }

            of(parts[0].toLong(), parts[1].toLong(), parts[2].toLong())
        }

        double straightLineDistanceTo(Coord other) {
            Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2) + Math.pow(this.z - other.z, 2))
        }

        @Override
        String toString() {
            "{$x,$y,$z}"
        }
    }

}