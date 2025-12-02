package uk.co.jonathonhenderson.adventofcode.days

class Day02 extends Day {
    private final List<Range> ranges

    Day02(String content) {
        super(content)
        this.ranges = content.trim().split(",").collect { chunk -> Range.parse(chunk) }
    }

    @Override
    String part1() {
        ranges.collectMany { range -> range.invalidIdsMadeOfTwoRepeatedNumbers() }.sum().toString()
    }

    @Override
    String part2() {
        ranges.collectMany { range -> range.invalidIdsMadeOfNRepeatedNumbers() }.sum().toString()
    }

    static class Range {
        final long start
        final long end

        Range(long start, long end) {
            this.start = start
            this.end = end
        }

        @Override
        String toString() {
            "$start-$end"
        }

        List<Long> invalidIdsMadeOfTwoRepeatedNumbers() {
            (start..end)
                    .collect { n -> n.toString() }
                    .findAll { n -> isMadeOfTwoRepeatedNumbers(n) }
                    .collect { n -> n.toLong() }
        }

        List<Long> invalidIdsMadeOfNRepeatedNumbers() {
            (start..end).collect { n -> n.toString() }
                    .findAll { n -> isMadeOfNRepeatedNumbers(n) }
                    .collect { n -> n.toLong() }
        }

        static boolean isMadeOfTwoRepeatedNumbers(String s) {
            s.length() % 2 == 0 && s.substring(0, s.length() / 2 as int) == s.substring(s.length() / 2 as int)
        }

        static boolean isMadeOfNRepeatedNumbers(String s) {
            if (s.length() == 1) {
                return false
            }

            (1..(((s.length() + 1) / 2) as int))
                    .findAll { s.length() % it == 0 }
                    .collect { s.substring(0, it) * ((s.length() / it) as int) }
                    .any { it == s }
        }

        static Range parse(String input) {
            def parts = input.trim().split("-")
            if (parts.length != 2) {
                throw new IllegalStateException("Not a valid range")
            }

            new Range(parts[0].trim().toLong(), parts[1].trim().toLong())
        }
    }

}
