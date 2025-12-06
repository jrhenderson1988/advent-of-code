package uk.co.jonathonhenderson.adventofcode.days

import groovy.transform.Canonical

class Day06 extends Day {
    private final List<Puzzle> puzzles

    Day06(String content) {
        super(content)
        this.puzzles = parse(content)
    }

    @Override
    String part1() {
        this.puzzles.collect { it.calculate() }.sum()
    }

    @Override
    String part2() {
        this.puzzles.collect { it.columnarCalculate() }.sum()
    }

    static List<Puzzle> parse(String content) {
        def lines = content.stripIndent().trim().readLines()
        int maxLen = lines*.size().max()
        lines = lines.collect { line -> padEnd(line, maxLen) + " " }

        def puzzles = []
        int start = 0
        for (int i = 0; i < maxLen + 1; i++) {
            if (allEmpty(lines*.charAt(i) as List<Character>)) {
                def values = lines.collect { line -> line.substring(start, i) }
                def numbers = (0..(values.size() - 2)).collect { n -> values.get(n) }
                def operator = Operator.from(values.last().trim())

                puzzles.add(new Puzzle(numbers, operator))
                start = i + 1
            }
        }

        puzzles
    }

    static String padEnd(String s, int len) {
        if (s.length() >= len) {
            s
        } else {
            int toPad = len - s.length()
            s + (" " * toPad)
        }
    }

    static boolean allEmpty(List<Character> chars) {
        chars.every { it == ' ' }
    }

    static enum Operator {
        ADD,
        MUL;

        long apply(long a, long b) {
            if (this == MUL) {
                a * b
            } else if (this == ADD) {
                a + b
            } else {
                throw new IllegalStateException("Not a valid operator")
            }
        }

        long apply(List<Long> numbers) {
            numbers.inject { a, b -> apply(a, b) }
        }

        static Operator from(String s) {
            if (s == "+") {
                ADD
            } else if (s == "*") {
                MUL
            } else {
                throw new IllegalArgumentException("Not a valid operator")
            }
        }
    }

    @Canonical
    static class Puzzle {
        final List<String> numbers
        final Operator operator

        long calculate() {
            operator.apply(numbers*.toLong())
        }

        long columnarCalculate() {
            def len = numbers.first().size()
            def nums = (0..(len - 1)).collect { i -> numbers.collect { n -> n.charAt(i) }.join("").toLong() }
            operator.apply(nums)
        }
    }
}