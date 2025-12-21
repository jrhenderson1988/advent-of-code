package uk.co.jonathonhenderson.adventofcode.days

import groovy.transform.Canonical

import java.time.Instant

class Day10 extends Day {
    private List<MachineInstruction> manual

    Day10(String content) {
        super(content)
        this.manual = this.content.stripIndent().trim().readLines().collect { it -> MachineInstruction.parse(it.trim()) }
    }

    @Override
    String part1() {
        manual.collect { minimumButtonPressesToReachTargetLights(it) }.sum()
    }

    @Override
    String part2() {
        manual.collect { minButtonPressesToReachTarget(it) }.sum()
    }

    private static long minimumButtonPressesToReachTargetLights(MachineInstruction instruction) {
        def start = [false] * instruction.indicatorLights.size()
        def target = instruction.indicatorLights
        def path = bfs(start, { it == target }) { state -> instruction.wiringSchematics.collect { btn -> pressButton(state, btn) } }

        (path.size() - 1) as long
    }

    private static List<Boolean> pressButton(List<Boolean> lights, List<Integer> button) {
        (0..(lights.size() - 1)).collect { i -> button.contains(i) ? !lights.get(i) : lights.get(i) }
    }

    private static long minimumButtonPressesToReachJoltageLevels(MachineInstruction instruction) {
        def start = [0] * instruction.joltageRequirements.size()
        def target = instruction.joltageRequirements
        def joltageIncreases = instruction.wiringSchematics.collect { btn -> schematicToJoltageIncreases(btn, start.size()) }
        def path = bfs(start, { it == target }) { state -> possibleNextStates(target, state, joltageIncreases) }

        println("$instruction -> ${path.size() - 1}")
        (path.size() - 1) as long
    }

    private static long minButtonPressesToReachTarget(MachineInstruction instruction) {
        def target = instruction.joltageRequirements
        def inputs = instruction.wiringSchematics.collect { btn -> schematicToJoltageIncreases(btn, target.size()) }
        def start = [0] * inputs.size() // 0 presses per input

        def q = [start]
        def explored = [start].toSet()

        def lastPrint = System.currentTimeSeconds()
        while (!q.isEmpty()) {
            def v = q.pop()
            if ((System.currentTimeSeconds() - lastPrint) > 10) {
                println("current: $v")
                lastPrint = System.currentTimeSeconds()
            }

            if (target == applyPresses(inputs, v)) {
                def result = v.sum() as long
                println("Found: $result")
                return result
            }

            for (def w in nextPresses(v, target)) {
                if (!explored.contains(w)) {
                    explored.add(w as List<Integer>)
                    q.add(w as List<Integer>)
                }
            }
        }

        return -1
    }

    private static List<Integer> applyPresses(List<List<Integer>> inputs, List<Integer> presses) {
        (0..<inputs.size()).collect { i -> multiply(inputs.get(i), presses.get(i)) }.inject { a, b -> add(a, b) }
    }

    private static List<Integer> add(List<Integer> a, List<Integer> b) {
        (0..<a.size()).collect { i -> a.get(i) + b.get(i) }
    }

    private static List<Integer> multiply(List<Integer> values, int multiplier) {
        values.collect { val -> val * multiplier }
    }

    private static List<Integer> nextPresses(List<Integer> currentPresses, List<Integer> target) {
        // compare if each option would potentially exceed the target, and omit it

        def result = []
        for (def i = 0; i < currentPresses.size(); i++) {
            def newPresses = currentPresses.collect { it }
            newPresses[i] = currentPresses.get(i) + 1
            result.add(newPresses)
        }
        result
    }

    private static <T> List<T> bfs(T start, Closure<Boolean> target, Closure<List<T>> neighbourFn) {
        def q = [start]
        def explored = [start].toSet()
        def prev = [(start): null]

        while (!q.isEmpty()) {
            def v = q.pop()
            if (target.call(v) == true) {
                def path = []
                while (v != null) {
                    path.push(v)
                    v = prev[v]
                }
                return path
            }
            for (def w in neighbourFn.call(v)) {
                if (!explored.contains(w)) {
                    explored.add(w)
                    prev[w] = v
                    q.add(w)
                }
            }
        }

        return null
    }

    private static List<Integer> increaseJoltage(List<Integer> state, List<Integer> button) {
        (0..<state.size()).collect { i -> state.get(i) + button.get(i) }
    }

    private static List<List<Integer>> possibleNextStates(List<Integer> targetState, List<Integer> currentState, List<List<Integer>> joltageIncreases) {
        // as we're applying the next states, if any of them have values greater than the target, omit them from possible next states
        joltageIncreases
                .collect { joltageIncrease -> increaseJoltage(currentState, joltageIncrease) }
                .findAll { possibleNextState -> !(0..<possibleNextState.size()).any { idx -> possibleNextState.get(idx) > targetState.get(idx) } }
    }

    private static List<Integer> schematicToJoltageIncreases(List<Integer> schematic, int size) {
        (0..<size).collect { i -> schematic.contains(i) ? 1 : 0 }
    }

    @Canonical
    static class MachineInstruction {
        private final List<Boolean> indicatorLights
        private final List<List<Integer>> wiringSchematics
        private final List<Integer> joltageRequirements

        MachineInstruction(List<Boolean> indicatorLights, List<List<Integer>> wiringSchematics, List<Integer> joltageRequirements) {
            this.indicatorLights = indicatorLights
            this.wiringSchematics = wiringSchematics
            this.joltageRequirements = joltageRequirements
        }

        static MachineInstruction parse(String line) {
            def indicatorLights = line.substring(line.indexOf("[") + 1, line.indexOf("]"))
                    .toCharArray()
                    .collect { ch -> (ch == ('#' as char)) }
            def wiringSchematics = line.substring(line.indexOf("]") + 1, line.indexOf("{"))
                    .trim()
                    .split(" ")
                    .collect { it.trim() }
                    .collect { it.substring(1, it.length() - 1) }
                    .collect { it.split(",").collect { it.toInteger() } }
            def joltageRequirements = line.substring(line.indexOf("{") + 1, line.indexOf("}"))
                    .split(",")
                    .collect(it -> it.toInteger())

            new MachineInstruction(indicatorLights, wiringSchematics, joltageRequirements)
        }

        @Override
        String toString() {
            "${indicatorLights.collect { it ? "#" : "." }.join("")} && ${joltageRequirements.join(",")}"
        }
    }
}
