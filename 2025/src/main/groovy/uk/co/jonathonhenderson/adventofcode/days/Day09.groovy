package uk.co.jonathonhenderson.adventofcode.days

import groovy.transform.Canonical
import groovy.transform.EqualsAndHashCode
import uk.co.jonathonhenderson.adventofcode.utils.Pair

class Day09 extends Day {
    private final List<Point> points

    Day09(String content) {
        super(content.stripIndent().trim())
        this.points = this.content.readLines().collect { line -> parseCoord(line) }
    }

    @Override
    String part1() {
        largestRectangleArea().toString()
    }

    @Override
    String part2() {
        "TODO"
    }

    private static Point parseCoord(String line) {
        def parts = line.split(",")
        if (parts.length != 2) {
            throw new IllegalStateException("not a valid coordinate: $line")
        }

        Point.of(parts[1].toLong(), parts[0].toLong())
    }

    private long largestRectangleArea() {
        points.collectMany { a -> this.points.collect { b -> Pair.of(a, b) } }
                .collect { it.first().area(it.second()) }
                .max()
    }


    @Canonical
    @EqualsAndHashCode
    static class Point {
        final long y
        final long x

        Point(long y, long x) {
            this.y = y
            this.x = x
        }

        static Point of(long y, long x) {
            new Point(y, x)
        }

        long area(Point other) {
            (Math.abs(this.y - other.y) + 1) * (Math.abs(this.x - other.x) + 1)
        }
    }
}