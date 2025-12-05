package uk.co.jonathonhenderson.adventofcode.days

import uk.co.jonathonhenderson.adventofcode.utils.Utils

class Day05 extends Day {
    private final List<RangeInclusive> freshIngredientRanges
    private final List<Long> ingredients

    Day05(String content) {
        super(content)
        def parts = Utils.splitByEmptyLines(content)
        freshIngredientRanges = parts.get(0)
                .readLines()
                .collect { it.trim() }
                .findAll { !it.isBlank() }
                .collect { parseRange(it) }
        ingredients = parts.get(1)
                .readLines()
                .collect { it.trim() }
                .findAll { !it.isBlank() }
                .collect { it.toLong() }
    }

    @Override
    String part1() {
        ingredients.findAll { isFresh(it) }.size().toString()
    }

    @Override
    String part2() {
        def sorted = sortRanges(this.freshIngredientRanges)
        def merged = mergeRanges(sorted)

        merged.collect { it.totalItems() }.sum().toString()
    }

    static RangeInclusive parseRange(String line) {
        def parts = line.split("-")
        long start = parts[0].toLong()
        long end = parts[1].toLong()

        new RangeInclusive(Math.min(start, end), Math.max(start, end))
    }

    static List<RangeInclusive> sortRanges(List<RangeInclusive> ranges) {
        ranges.sort { it.start }
    }

    static List<RangeInclusive> mergeRanges(List<RangeInclusive> ranges) {
        if (ranges.size() <= 1) {
            return ranges
        }

        def newRanges = []
        def current = ranges.get(0)
        for (int i = 1; i < ranges.size(); i++) {
            def range = ranges.get(i)
            if (current.overlaps(range)) {
                current = current.merge(range)
            } else {
                newRanges.add(current)
                current = range
            }
        }

        newRanges.add(current)

        newRanges
    }

    boolean isFresh(Long ingredient) {
        freshIngredientRanges.any { it.contains(ingredient) }
    }

    static class RangeInclusive {
        final long start
        final long end

        RangeInclusive(long start, long end) {
            this.start = start
            this.end = end
        }

        boolean contains(long n) {
            n >= start && n <= end
        }

        boolean overlaps(RangeInclusive other) {
            other.contains(this.start) || other.contains(this.end) || this.contains(other.start) || this.contains(other.end)
        }

        RangeInclusive merge(RangeInclusive other) {
            if (!overlaps(other)) {
                null
            } else {
                new RangeInclusive(Math.min(other.start, this.start), Math.max(other.end, this.end))
            }
        }

        long totalItems() {
            (end - start) + 1
        }

        @Override
        String toString() {
            "($start..$end)"
        }
    }
}
