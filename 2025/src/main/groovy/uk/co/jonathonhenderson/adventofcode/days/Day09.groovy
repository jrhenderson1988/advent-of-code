package uk.co.jonathonhenderson.adventofcode.days

import groovy.transform.Canonical
import groovy.transform.EqualsAndHashCode
import uk.co.jonathonhenderson.adventofcode.utils.Pair

import java.time.Instant

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

    private long largestRectangleAreaWithOnlyRedAndGreenTiles() {
        def lines = (0..(points.size() - 1)).collect { i -> Line.of(points.get(i), points.get((i + 1) % points.size())) }
        def polygon = Polygon.of(lines)

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

//        [
//                Point.of(6, 0),
//                Point.of(6, 1),
//                Point.of(6, 2), // should be out but it's in
//                Point.of(6, 3),
//                Point.of(6, 4),
//                Point.of(6, 5),
//                Point.of(6, 6),
//                Point.of(6, 7),
//                Point.of(6, 8),
//        ].each { pt -> println("$pt -> ${polygon.contains(pt)}") }


        def pointPairsToAreas = points
                .collectMany { a -> points.collect { b -> Pair.of(a, b) } }
                .collect { Pair.of(it, it.first().area(it.second())) }
                .findAll { it.second() > 1 } // exclude any that have area 1 (same point)
                .sort { it.second() } // sort by area
                .reversed() // largest area first

        int i = 0
        for (def item in pointPairsToAreas) {
            if (i % 1000 == 0) {
                println("${Instant.now()} evaluating $i")
            }

            def points = item.first()
            def a = points.first()
            def b = points.second()
            if (polygon.containsAll(otherCornerPoints(a, b))) {
                if (polygonContainsAllLines(polygon, getLines(a, b))) {
                    return item.second()
                }
            }

            i++
        }

        -1

//        println("there are ${pointPairsToAreas.size()} total possible rectangles")
//        def allFourCornersWithinPolygon = pointPairsToAreas
//                .findAll { polygon.containsAll(otherCornerPoints(it.first().first(), it.first().second())) }
//        println("there are ${allFourCornersWithinPolygon.size()} rectangles with 4 corners all within polygon")
//        def allLinesWithinPolygon = allFourCornersWithinPolygon
//                .findAll { polygonContainsAllLines(polygon, getLines(it.first().first(), it.first().second())) }
//        println("there are ${allLinesWithinPolygon.size()} rectangles with all their outer lines in polygon")
//
//        def result = allLinesWithinPolygon.sort { it.second() }.reversed().first().second()
//        // allLinesWithinPolygon.each { println(it) }
//        println("----------------")
//        println("RESULT: $result")

//        println(pointPairsToAreas.size())
//        println(allFourCornersWithinPolygon.size())
//        println(polygon.verticalLines.size())
//        println(polygon.horizontalLines.size())
//        result
    }

    private static List<Point> otherCornerPoints(Point a, Point b) {
        // find the other two corner points of the rectangle
        [Point.of(a.x, b.y), Point.of(b.x, a.y)]
    }

    private static List<Line> getLines(Point a, Point b) {
        def lines = otherCornerPoints(a, b).collectMany { corner -> [a, b].collect { point -> Line.of(point, corner) } }
//        println("$a / $b OTHER CORNERS: $lines")
        lines
    }

    private static boolean polygonContainsAllLines(Polygon polygon, List<Line> lines) {
        for (def line in lines) {
            if (!polygonContainsAllPointsInLine(polygon, line)) {
                return false
            }
        }
        return true
    }

    private static boolean polygonContainsAllPointsInLine(Polygon polygon, Line line) {
        if (line.isVertical) {
            def start = Math.min(line.a.y, line.b.y)
            def end = Math.max(line.a.y, line.b.y)
            for (long y = start; y <= end; y++) {
                def point = Point.of(line.a.x, y)
                if (!polygon.contains(point)) {
                    return false
                }
            }
            return true
        } else {
            def start = Math.min(line.a.x, line.b.x)
            def end = Math.max(line.a.x, line.b.x)
            for (long x = start; x <= end; x++) {
                def point = Point.of(x, line.a.y)
                if (!polygon.contains(point)) {
                    return false
                }
            }
            return true
        }
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

            if (!(a.x == b.x || a.y == b.y)) {
                throw new IllegalStateException("Point $a is not adjacent to point $b")
            }
        }

        static Line of(Point a, Point b) {
            new Line(a, b)
        }

        @Override
        String toString() {
            "${isVertical ? "VRT" : "HRZ"}($a/$b)"
        }
    }

    @Canonical
    static class Polygon {
        final List<Line> horizontalLines
        final List<Line> verticalLines
        final long maxX

        Polygon(List<Line> lines) {
            this.horizontalLines = lines.findAll { line -> !line.isVertical }
            this.verticalLines = lines.findAll { line -> line.isVertical }
            this.maxX = this.horizontalLines.collect { line -> Math.max(line.a.x, line.b.x) }.max()
        }

        static Polygon of(List<Line> lines) {
            new Polygon(lines)
        }

        boolean containsAll(List<Point> points) {
            for (def point in points) {
                if (!contains(point)) {
                    return false
                }
            }

            return true
        }

        boolean contains(Point point) {
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

            // TODO -
            //  currently, the problem is at the edges. It's saying that 4,2 and 4,12 are not inside
            //  the polygon

            // if the point is in any line (vertical or horizontal) return true
            if (verticalLines.any { line -> isOnLine(line, point) }) {
                return true
            } else if (horizontalLines.any { line -> isOnLine(line, point) }) {
                return true
            }

            // otherwise, do a ray trace to the right to count the intersections with vertical lines
            // and work out how many intersections there are
            verticalLines.count { rayIntersects(it, point) } % 2 == 1
        }

        static boolean rayIntersects(Line line, Point point) {
//            if (Math.min(line.a.y, line.b.y) > point.y) {
//                return false // point is above the highest point of the line
//            } else if (Math.max(line.a.y, line.b.y) < point.y) {
//                return false // point is below the lowest point of the line
//            } else {
//
//            }

            def result = line.a.x >= point.x && // line is to the right of the point
                    Math.min(line.a.y, line.b.y) <= point.y && // point is below line's top point
                    Math.max(line.a.y, line.b.y) >= point.y // point is above line's bottom point
//            println("$point intersects line $line ? $result")
            result
        }

        static boolean isOnLine(Line line, Point point) {
            if (line.isVertical) {
                if (point.x != line.a.x) {
                    return false // point does not have the same X as the line
                } else if (point.y < Math.min(line.a.y, line.b.y)) {
                    return false // point is above the top of the line
                } else if (point.y > Math.max(line.a.y, line.b.y)) {
                    return false // point is below the bottom of the line
                } else {
                    return true
                }
            } else { // horizontal
                if (point.y != line.a.y) {
                    return false // point does not have the same Y as the line
                } else if (point.x < Math.min(line.a.x, line.b.x)) {
                    return false // point is to the left of the leftmost point of the line
                } else if (point.x > Math.max(line.a.x, line.b.x)) {
                    return false // point is to the right of the rightmost point of the line
                } else {
                    return true
                }
            }
        }
    }
}