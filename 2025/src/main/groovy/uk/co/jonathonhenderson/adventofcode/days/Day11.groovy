package uk.co.jonathonhenderson.adventofcode.days

import groovy.transform.Canonical

class Day11 extends Day {
    private Map<String, Device> devices

    Day11(String content) {
        super(content)
        this.devices = content.trim()
                .stripIndent()
                .readLines()
                .collect { Device.parse(it.trim()) }
                .collectEntries { [(it.name): it] }
    }

    @Override
    String part1() {
        totalPathsBetween("you", "out").toString()
    }

    @Override
    String part2() {
        totalPathsBetweenWithRequiredNodes("svr", "out", false, false)
    }

    private int totalPathsBetween(String start, String end) {
        if (start == end) {
            return 1
        }

        def paths = 0
        for (def output in devices.get(start).outputs) {
            paths += totalPathsBetween(output, end)
        }

        paths
    }

    private int totalPathsBetweenWithRequiredNodes(String start, String end, boolean seenDAC, boolean seenFFT) {
        seenDAC = start == "dac" ? true : seenDAC
        seenFFT = start == "fft" ? true : seenFFT

        if (start == end) {
            if (seenDAC && seenFFT) {
                return 1
            } else {
                return 0
            }
        }

        def paths = 0
        def outputs = devices.containsKey(start) ? devices.get(start).outputs : []
        for (def output in outputs) {
            paths += totalPathsBetweenWithRequiredNodes(output, end, seenDAC, seenFFT)
        }

        paths
    }

    @Canonical
    static class Device {
        final String name
        final List<String> outputs

        static Device parse(String line) {
            def colonPos = line.indexOf(":")
            def label = line.substring(0, colonPos).trim()
            def outputs = line.substring(colonPos + 1).trim().split(" ").collect { it.trim() }

            new Device(label, outputs)
        }

        @Override
        String toString() {
            "$name: [${outputs.join(", ")}]"
        }
    }
}
