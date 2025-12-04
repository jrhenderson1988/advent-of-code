package uk.co.jonathonhenderson.adventofcode.days


import uk.co.jonathonhenderson.adventofcode.utils.Coord

class Day04 extends Day {
    private final List<List<Cell>> grid

    Day04(String content) {
        super(content)
        this.grid = content.trim()
                .readLines()
                .collect { it.trim() }
                .collect { line -> line.chars().collect { Cell.from(it as char) } }
    }

    @Override
    String part1() {
        totalRollsForkliftAccessible(this.grid)
    }

    @Override
    String part2() {
        def originalTotalRollsOfPaper = coordsOfRollsOfPaper(this.grid).size()
        def cleanedGrid = cleanGrid(grid)
        def remainingTotalRollsOfPaper = coordsOfRollsOfPaper(cleanedGrid).size()

        originalTotalRollsOfPaper - remainingTotalRollsOfPaper
    }

    static List<List<Cell>> cleanGrid(List<List<Cell>> grid) {
        def newGrid = clearCells(grid, Set.of())
        while (true) {
            def cellsToBeRemoved = cellsThatCanBeRemoved(newGrid).toSet()
            if (cellsToBeRemoved.size() == 0) {
                break
            }
            newGrid = clearCells(newGrid, cellsToBeRemoved)
        }

        newGrid
    }

    static List<List<Cell>> clearCells(List<List<Cell>> grid, Set<Coord> toBeRemoved) {
        (0..(grid.size() - 1)).collect { y ->
            (0..(grid.get(y).size() - 1)).collect { x ->
                toBeRemoved.contains(Coord.of(y, x)) ? Cell.EMPTY : getCellAt(grid, Coord.of(y, x))
            }
        }
    }

    static int totalRollsForkliftAccessible(List<List<Cell>> grid) {
        cellsThatCanBeRemoved(grid).size()
    }

    static List<Coord> cellsThatCanBeRemoved(List<List<Cell>> grid) {
        coordsOfRollsOfPaper(grid).findAll { canBeRemoved(grid, it) }
    }

    static List<Coord> coordsOfRollsOfPaper(List<List<Cell>> grid) {
        coordsFromGrid(grid).findAll { getCellAt(grid, it) == Cell.ROLL_OF_PAPER }
    }

    static List<Coord> coordsFromGrid(List<List<Cell>> grid) {
        (0..(grid.size() - 1))
                .collectMany { y -> (0..(grid.get(y).size() - 1)).collect { x -> Coord.of(y, x) } }
    }

    static int totalAdjacentRollsOfPaper(List<List<Cell>> grid, Coord coord) {
        coord.adjacent()
                .findAll { adj -> getCellAt(grid, adj) == Cell.ROLL_OF_PAPER }
                .size()
    }

    static Cell getCellAt(List<List<Cell>> grid, Coord coord) {
        if (coord.y < 0 || coord.y >= grid.size()) {
            null
        } else if (coord.x < 0 || coord.x >= grid.get(coord.y).size()) {
            null
        } else {
            grid.get(coord.y).get(coord.x)
        }
    }

    static boolean canBeRemoved(List<List<Cell>> grid, Coord coord) {
        totalAdjacentRollsOfPaper(grid, coord) < 4
    }

    static enum Cell {
        ROLL_OF_PAPER,
        EMPTY;

        static Cell from(char ch) {
            if (ch == ('@' as char)) {
                ROLL_OF_PAPER
            } else if (ch == ('.' as char)) {
                EMPTY
            } else {
                throw new IllegalArgumentException("Unexpected character '$ch'")
            }
        }
    }
}
