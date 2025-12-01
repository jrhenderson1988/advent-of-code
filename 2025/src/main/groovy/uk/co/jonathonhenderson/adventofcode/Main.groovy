package uk.co.jonathonhenderson.adventofcode

import uk.co.jonathonhenderson.adventofcode.days.Day

import java.time.Duration
import java.time.Instant
import java.util.function.Supplier

import static uk.co.jonathonhenderson.adventofcode.Utils.readPuzzle

class Main {
    static void main(String[] args) {
        def dayNum = args.length >= 1 ? args[0].toInteger() : null
        def partNum = args.length >= 2 ? args[1].toInteger() : null

        def day = getDay(dayNum)
        if (partNum == 1 || partNum == null) {
            def result = time(day::part1)
            println("Part 1: ${result}")
        }
        if (partNum == 2 || partNum == null) {
            def result = time(day::part2)
            println("Part 2: ${result}")
        }
    }


    static Day getDay(int n) {
        try {
            def className = Day.class.packageName + ".Day" + (n < 10 ? "0${n}" : n)
            def clazz = Class.forName(className)
            def constructor = clazz.getDeclaredConstructor(String.class)
            def content = readPuzzle(n)
            def inst = constructor.newInstance(content)
            inst as Day
        } catch (ClassNotFoundException cnfe) {
            println("Day ${n} not implemented")
            System.exit(1)
        }
    }

    static TimedResult time(Supplier<String> fn) {
        def start = Instant.now()
        def result = fn.get()
        def end = Instant.now()
        def time = Duration.between(start, end).abs()
        new TimedResult(time, result)
    }

    static class TimedResult {
        final Duration time
        final String result

        TimedResult(Duration time, String result) {
            this.time = time
            this.result = result
        }

        @Override
        String toString() {
            "$result (${time.toMillis()}ms)"
        }
    }
}