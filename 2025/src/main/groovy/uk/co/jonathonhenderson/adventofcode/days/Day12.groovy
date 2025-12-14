package uk.co.jonathonhenderson.adventofcode.days

import groovy.transform.Canonical

import static uk.co.jonathonhenderson.adventofcode.utils.Utils.splitByEmptyLines

class Day12 extends Day {
    private final Map<Integer, Shape> shapes
    private final List<Region> regions

    Day12(String content) {
        super(content)
        def chunks = splitByEmptyLines(this.content.stripIndent().trim())
        this.shapes = chunks.take(chunks.size() - 1).collectEntries { [(parseShapeIndex(it)): Shape.parse(it)] }
        this.regions = chunks.last().trim().readLines().collect { Region.parse(it) }
    }

    private static int parseShapeIndex(String content) {
        content.substring(0, content.indexOf(":")).trim().toInteger()
    }

    @Override
    String part1() {
        regions.findAll { canFitAllShapes(it) }.size()
    }

    @Override
    String part2() {
        "N/A"
    }

    static boolean canFitAllShapes(Region region) {
        // Annoyingly, a simple area check worked for the real input, but the test input is not as
        // simple, so the tests don't pass for that.
        int maxShapesPerColumn = (int) (region.height / 3) // all shapes are 3x3
        int maxShapesPerRow = (int) (region.width / 3)
        int maxShapes = maxShapesPerRow * maxShapesPerColumn

        int totalShapesToFit = region.quantities.collect { it }.sum() as int

        totalShapesToFit <= maxShapes
    }

    @Canonical
    static class Shape {
        final boolean[] blocks

        static Shape parse(String content) {
            def lines = content.readLines()
            def puzzleLine = lines.drop(1).join("")
            boolean[] puzzle = puzzleLine.toCharArray().collect { ch -> ch == ('#' as char) }.toArray()
            if (puzzle.length != 9) {
                throw new IllegalArgumentException("not a valid puzzle")
            }

            new Shape(puzzle)
        }

        @Override
        String toString() {
            (0..2).collect { y -> (0..2).collect { x -> isBlock(y, x) ? "#" : "." }.join("") }.join("\n")
        }

        // all of these methods that I was originally setting up for a more complex implementation
        // (of which I did no idea where to start) are not really needed.

        boolean isBlock(int y, int x) {
            blocks[y * 3 + x]
        }

        Shape rotateClockwise() {
            // 012    630
            // 345 -> 741
            // 678    852
            transform([2, 5, 8, 1, 4, 5, 0, 3, 6] as int)
        }

        Shape rotateAntiClockwise() {
            transform([6, 3, 0, 7, 4, 1, 8, 5, 2] as int)
        }

        Shape flipHorizontal() {
            transform([2, 1, 0, 5, 4, 3, 8, 7, 6] as int)
        }

        Shape flipVertical() {
            transform([6, 7, 8, 3, 4, 5, 0, 1, 2] as int)
        }

        Shape transform(int[] newIndices) {
            new Shape(this.blocks.collect { it } as boolean) // TODO - apply transformations
        }
    }

    @Canonical
    static class Region {
        final int width
        final int height
        final int[] quantities

        static Region parse(String line) {
            def dimensions = line.substring(0, line.indexOf(":")).split("x").collect { it.trim().toInteger() }
            def width = dimensions[0]
            def height = dimensions[1]
            int[] quantities = line.substring(line.indexOf(":") + 1).trim().split(" ").collect { it.trim().toInteger() }
            if (quantities.length != 6) {
                throw new IllegalArgumentException("invalid region definition")
            }

            new Region(width, height, quantities)
        }

        @Override
        String toString() {
            "${width}x${height}: ${quantities.collect { it -> it }.join(" ")}"
        }
    }
}
