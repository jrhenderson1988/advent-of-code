package uk.co.jonathonhenderson.adventofcode.days

import groovy.transform.Canonical
import uk.co.jonathonhenderson.adventofcode.utils.Pair

class Day08 extends Day {
    private final List<Coord> positions
    private static final int CYCLES = 10
    // TODO - this should be 1000 for the real input but 10 for the test input

    Day08(String content) {
        super(content.stripIndent().trim())
        this.positions = this.content.readLines().collect { line -> Coord.parse(line.trim()) }
    }

    @Override
    String part1() {
        def closestPairs = computeDistances()
                .collect { k, v -> Pair.of(k, v) }
                .sort { pair -> pair.second() }
                .take(CYCLES)
                .collect { it.first() }

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
        "TODO"
    }

    private Map<Pair<Coord, Coord>, Double> computeDistances() {
        def distances = [:]

        for (int i = 0; i < this.positions.size(); i++) {
            for (int j = i + 1; j < this.positions.size(); j++) {
                def a = this.positions.get(i)
                def b = this.positions.get(j)
                def key = Pair.of(a, b)
                distances.put(key, a.straightLineDistanceTo(b))
            }
        }

        distances
    }

    private List<Set<Coord>> buildCircuits(List<Pair<Coord, Coord>> connections) {
        def circuits = connections.collect { pair -> Set.of(pair.first(), pair.second()) }
        def processed = circuits.collect { false }

        def newCircuits = []
        for (int i = 0; i < circuits.size(); i++) {
            if (processed[i]) {
                continue
            }

            def circuit = circuits.get(i)
            def merged = new HashSet<>(circuit)
            processed[i] = true

            while (true) {
                def changes = false
                for (int j = i + 1; j < circuits.size(); j++) {
                    def current = circuits.get(j)
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

            newCircuits.add(merged)
        }

        newCircuits
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