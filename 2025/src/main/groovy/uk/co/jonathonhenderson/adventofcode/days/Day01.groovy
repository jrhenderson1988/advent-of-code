package uk.co.jonathonhenderson.adventofcode.days

import static uk.co.jonathonhenderson.adventofcode.utils.Utils.mathMod

class Day01 extends Day {
    private final List<Instruction> instructions

    Day01(String content) {
        super(content)
        this.instructions = content.lines().map { line -> parseLine(line) }.toList()
    }

    @Override
    String part1() {
        def state = new State(50, 0)
        def zeroCounter = 0
        for (def instruction in instructions) {
            state = instruction.apply(state)
            if (state.current == 0) {
                zeroCounter++
            }
        }

        zeroCounter.toString()
    }

    @Override
    String part2() {
        def state = new State(50, 0)
        for (def instruction in instructions) {
            state = instruction.apply(state)
        }

        state.zeroTicks
    }

    static Instruction parseLine(String line) {
        def trimmed = line.trim()
        new Instruction(parseDirection(trimmed), parseValue(trimmed))
    }

    static int parseValue(String line) {
        line.substring(1).toInteger()
    }

    static enum Direction {
        LEFT,
        RIGHT;

        @Override
        String toString() {
            this.name().substring(0, 1)
        }
    }

    static class Instruction {
        final Direction direction
        final int value

        Instruction(Direction direction, int value) {
            this.direction = direction
            this.value = value
        }

        State apply(State state) {

            if (direction == Direction.LEFT) {
                applyLeft(state)
            } else if (direction == Direction.RIGHT) {
                applyRight(state)
            } else {
                throw new RuntimeException("Invalid direction")
            }
        }

        State applyRight(State state) {
            def adjusted = state.current + value
            def newCurrent = mathMod(adjusted, 100)
            def zeroPasses = adjusted.abs().intdiv(100)
            def newZeroTicks = state.zeroTicks + zeroPasses
            def newState = new State(newCurrent, newZeroTicks)
            newState
        }

        State applyLeft(State state) {
            def adjusted = state.current - value
            def newCurrent = mathMod(adjusted, 100)
            def zeroPasses = 0
            if (adjusted == 0) {
                zeroPasses = 1
            } else if (adjusted < 0) {
                zeroPasses = (adjusted.abs() + 100).intdiv(100) - (state.current == 0 ? 1 : 0)
            } else if (adjusted > 0) {
                zeroPasses = 0
            }
            def newZeroTicks = state.zeroTicks + zeroPasses
            def newState = new State(newCurrent, newZeroTicks)
            newState
        }
    }

    static Direction parseDirection(String line) {
        if (line.toUpperCase().startsWith("L")) {
            Direction.LEFT
        } else if (line.toUpperCase().startsWith("R")) {
            Direction.RIGHT
        } else {
            throw new RuntimeException("unexpected instruction start (expected L or R)")
        }
    }

    static class State {
        final int current
        final int zeroTicks

        State(int current, int zeroTicks) {
            this.current = current
            this.zeroTicks = zeroTicks
        }

        @Override
        String toString() {
            "{$current/$zeroTicks}"
        }
    }
}
