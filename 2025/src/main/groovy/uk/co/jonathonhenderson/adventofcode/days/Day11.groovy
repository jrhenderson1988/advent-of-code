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
        totalPathsBetweenWithRequiredNodes("svr", "out", false, false, [:]).toString()
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

    private long totalPathsBetweenWithRequiredNodes(String start, String end, boolean seenDAC, boolean seenFFT, Map<String, Long> cache) {
        def key = "${start}-${end}-${seenDAC}-${seenFFT}"
        if (cache.containsKey(key)) {
            cache.get(key)
        } else if (start == end) {
            seenDAC && seenFFT ? 1 : 0
        } else {
            def paths = 0
            def outputs = devices.containsKey(start) ? devices.get(start).outputs : []
            for (def output in outputs) {
                paths += totalPathsBetweenWithRequiredNodes(output, end, start == "dac" ? true : seenDAC, start == "fft" ? true : seenFFT, cache)
            }

            cache.put(key, paths)
            paths
        }
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
