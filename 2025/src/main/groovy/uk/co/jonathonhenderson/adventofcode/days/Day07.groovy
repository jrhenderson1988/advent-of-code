package uk.co.jonathonhenderson.adventofcode.days

import groovy.transform.Canonical
import uk.co.jonathonhenderson.adventofcode.utils.Coord

class Day07 extends Day {
    private final TachyonManifold tachyonManifold

    Day07(String content) {
        super(content.stripIndent().trim())
        this.tachyonManifold = TachyonManifold.parse(this.content)
    }

    @Override
    String part1() {
        tachyonManifold.totalSplittersHit()
    }

    @Override
    String part2() {
        tachyonManifold.totalTimelines()
    }

    @Canonical
    static class TachyonManifold {
        final Coord start
        final Set<Coord> splitters
        final int height
        final int width

        static TachyonManifold parse(String content) {
            def lines = content.stripIndent().trim().readLines()
            def coords = (0..(lines.size() - 1))
                    .collectMany { y -> (0..(lines.get(y).length() - 1)).collect { x -> Coord.of(y, x) } }

            def splitters = coords.findAll { coord -> lines.get(coord.y).charAt(coord.x) == '^' as char }.toSet()
            def start = coords.find { coord -> lines.get(coord.y).charAt(coord.x) == 'S' as char }

            new TachyonManifold(start, splitters, lines.size(), lines.get(0).size())
        }

        int totalSplittersHit() {
            def splittersHit = [].toSet()
            def beamsOnCurrentRow = [start.x].toSet()

            for (int y = start.y; y <= height; y++) {
                def newBeamsOnCurrentRow = [].toSet()
                for (def x in beamsOnCurrentRow) {
                    def coord = Coord.of(y, x)
                    if (coord in splitters) {
                        splittersHit.add(coord)

                        if (Coord.of(y, x - 1) !in splitters) {
                            newBeamsOnCurrentRow.add(x - 1)
                        }

                        if (Coord.of(y, x + 1) !in splitters) {
                            newBeamsOnCurrentRow.add(x + 1)
                        }
                    } else {
                        newBeamsOnCurrentRow.add(x)
                    }
                }
                beamsOnCurrentRow = newBeamsOnCurrentRow
            }

            splittersHit.size()
        }

        long totalTimelines() {
            def firstSplitter = nextSplitterBelow(start)
            if (firstSplitter == null) {
                throw new IllegalStateException("could not find a splitter under start")
            }

            totalTimelinesFromSplitter(firstSplitter, [:])
        }

        long totalTimelinesFromSplitter(Coord coord, Map<Coord, Long> cache) {
            if (cache.containsKey(coord)) {
                return cache.get(coord)
            }

            def left = Coord.of(coord.y, coord.x - 1)
            def right = Coord.of(coord.y, coord.x + 1)
            def total = traverse(left, cache) + traverse(right, cache)

            cache.put(coord, total)

            total
        }

        long traverse(Coord coord, Map<Coord, Long> cache) {
            def below = nextSplitterBelow(coord)
            below == null ? 1 : totalTimelinesFromSplitter(below, cache)
        }

        private Coord nextSplitterBelow(Coord coord) {
            ((coord.y + 1)..(height - 1))
                    .collect { y -> Coord.of(y, coord.x) }
                    .find { c -> c in splitters }
        }
    }
}