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
        largestRectangleAreaWithOnlyRedAndGreenTiles().toString()
    }

    private static Point parseCoord(String line) {
        def parts = line.split(",")
        if (parts.length != 2) {
            throw new IllegalStateException("not a valid coordinate: $line")
        }

        Point.of(parts[0].toLong(), parts[1].toLong())
    }

    private long largestRectangleArea() {
        points.collectMany { a -> this.points.collect { b -> Pair.of(a, b) } }
                .collect { it.first().area(it.second()) }
                .max()
    }

    //            1111
    //  01234567890123
    // 0..............
    // 1.......#OOO#..
    // 2.......OOOOO..
    // 3..#OOOO#OOOO..
    // 4..OOOOOOOOOO..
    // 5..#OOOOOO#OO..
    // 6.........OOO..
    // 7.........#O#..
    // 8..............

    private long largestRectangleAreaWithOnlyRedAndGreenTiles() {
        def lines = buildLines()
        def possibleRectangles = computeAndSortPossibleRectangles()

        for (def possibleRectangle in possibleRectangles) {
            def rectangleLine = possibleRectangle.first()
            def area = possibleRectangle.second()
            def otherDiagonal = otherCornerPoints(rectangleLine)

            if (pointOutsidePolygon(lines, otherDiagonal.a)) {
                continue
            }

            if (pointOutsidePolygon(lines, otherDiagonal.b)) {
                continue
            }

            if (polygonIntersectsRectangle(lines, rectangleLine, otherDiagonal)) {
                continue
            }

            return area
        }

        -1
    }

    private List<Line> buildLines() {
        (0..<points.size()).collect { i -> Line.of(points.get(i), points.get((i + 1) % points.size())) }
    }

    private List<Pair<Line, Long>> computeAndSortPossibleRectangles() {
        points.collectMany { a -> points.collect { b -> Line.of(Point.min(a, b), Point.max(a, b)) } }
                .toSet()
                .collect { line -> Pair.of(line, line.a.area(line.b)) }
                .findAll { it.second() > 1 } // exclude any that have area 1 (same point)
                .sort { it.second() } // sort by area
                .reversed() // largest area first
    }

    private static Line otherCornerPoints(Line line) {
        def a = line.a
        def b = line.b
        def otherA = Point.of(a.x, b.y)
        def otherB = Point.of(b.x, a.y)
        Line.of(Point.min(otherA, otherB), Point.max(otherA, otherB))
    }

    private static boolean pointOutsidePolygon(List<Line> polygon, Point point) {
        // returns true if all vertical lines that cross the point's Y coordinate are either
        // completely to the left or completely to the right of the point.
        def linesInScope = polygon.findAll { it.isVertical && point.y >= min(it.a.y, it.b.y) && point.y <= max(it.a.y, it.b.y) }
        linesInScope.every { point.x < min(it.a.x, it.b.x) } || linesInScope.every { point.x > max(it.a.x, it.b.x) }
    }

    private static boolean polygonIntersectsRectangle(List<Line> polygon, Line a, Line b) {
        def minX = [a.a.x, a.b.x, b.a.x, b.b.x].min()
        def maxX = [a.a.x, a.b.x, b.a.x, b.b.x].max()
        def minY = [a.a.y, a.b.y, b.a.y, b.b.y].min()
        def maxY = [a.a.y, a.b.y, b.a.y, b.b.y].max()

        for (def line in polygon) {
            if (line.isVertical) {
                def lineX = line.a.x
                def topPointOfLine = min(line.a.y, line.b.y)
                def bottomPointOfLine = max(line.a.y, line.b.y)

                // the x coord lines up so that the vertical line MAY intersect
                if (lineX > minX && lineX < maxX) {
                    if (bottomPointOfLine > minY && topPointOfLine < minY) {
                        return true // intersects with top
                    } else if (bottomPointOfLine > maxY && topPointOfLine < maxY) {
                        return true // intersects with bottom
                    }
                }
            } else {
                def lineY = line.a.y
                def leftPointOfLine = min(line.a.x, line.b.x)
                def rightPointOfline = max(line.a.x, line.b.x)

                // the y coord lines up so that the horizontal line MAY intersect
                if (lineY > minY && lineY < maxY) {
                    if (rightPointOfline > minX && leftPointOfLine < minX) {
                        return true // intersects with left
                    } else if (rightPointOfline > maxX && leftPointOfLine < maxX) {
                        return true // intersects with right
                    }
                }
            }
        }

        false
    }

    private static long min(long a, long b) {
        Math.min(a, b)
    }

    private static long max(long a, long b) {
        Math.max(a, b)
    }

    @Canonical
    @EqualsAndHashCode
    static class Point {
        final long y
        final long x

        Point(long x, long y) {
            this.x = x
            this.y = y
        }

        static Point of(long x, long y) {
            new Point(x, y)
        }

        long area(Point other) {
            (Math.abs(this.y - other.y) + 1) * (Math.abs(this.x - other.x) + 1)
        }

        static Point min(Point a, Point b) {
            if (a.x < b.x) {
                a
            } else if (a.x > b.x) {
                b
            } else if (a.y < b.y) {
                a
            } else {
                b
            }
        }

        static Point max(Point a, Point b) {
            if (a.x > b.x) {
                a
            } else if (a.x < b.x) {
                b
            } else if (a.y > b.y) {
                a
            } else {
                b
            }
        }

        @Override
        String toString() {
            "{x=$x, y=$y}"
        }
    }

    @Canonical
    static class Line {
        final Point a
        final Point b
        final boolean isVertical

        Line(Point a, Point b) {
            this.a = a
            this.b = b
            this.isVertical = a.x == b.x
        }

        static Line of(Point a, Point b) {
            new Line(a, b)
        }

        @Override
        String toString() {
            "($a / $b)"
        }
    }
}