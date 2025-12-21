package uk.co.jonathonhenderson.adventofcode.days

import groovy.transform.Canonical

import static uk.co.jonathonhenderson.adventofcode.utils.PathFinding.bfs

// Part 2 was HARD. I had it working fine using search algorithms, like BFS, for the sample input.
// But for the real puzzle inputs, there was just too many branches. Looking online revealed that
// most used Z3 (which I'd never heard of until now). I didn't want to use external libraries so I
// hunted around for approaches to the problem and didn't get very far. The few solutions others had
// also went over my head. This is a pretty maths heavy puzzle and it's not my area of expertise.
// Ultimately, I found this on Reddit which lead me to the solution.
// https://www.reddit.com/r/adventofcode/comments/1pk87hl/2025_day_10_part_2_bifurcate_your_way_to_victory/
// Truth be told, I did have to look at other solutions for inspiration on how to apply it as this
// was quite difficult for me to comprehend too. Not happy with this one but at least it's done now.
// I really don't enjoy maths heavy puzzles that require specialised knowledge ¯\_(ツ)_/¯
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
        manual.collect { minimumPressesToReachJoltageLevels(it) }.sum()
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

    private static long minimumPressesToReachJoltageLevels(MachineInstruction instruction) {
        def buttons = instruction.wiringSchematics
        def demands = instruction.joltageRequirements

        def options = [:].withDefault { [] }
        def output = [:]
        for (def pressed in buildPowerset((0..<buttons.size()).toList() as List<Long>)) {
            def supply = (0..<demands.size()).collect { j -> pressed.findAll { b -> j in buttons[b] }.size() }
            def parity = supply.collect { j -> j % 2 as long }

            options[parity] << pressed
            output[pressed] = supply
        }

        return solve(demands, options, output, [:])
    }

    private static long solve(List<Long> joltages, Map<List<Long>, List<List<Long>>> options, Map<List<Long>, List<Long>> output, Map<String, Long> cache) {
        def key = joltages.toString()
        if (key in cache) {
            cache[key]
        } else if (joltages.min() < 0) {
            Long.MAX_VALUE
        } else if (joltages.sum() == 0) {
            0
        } else {
            def answer = Long.MAX_VALUE
            def parity = joltages.collect { v -> v % 2 as long }

            for (def pressed in options.getOrDefault(parity, [])) {
                def remaining = (0..<joltages.size()).collect { i -> Math.floorDiv(joltages[i] - output[pressed][i], 2) }
                long sub = solve(remaining, options, output, cache)
                if (sub == Long.MAX_VALUE) {
                    // avoid overflowing when Long.MAX_VALUE is returned (see above) and we end up
                    // doubling it (below).
                    continue
                }
                answer = Math.min(answer, pressed.size() + 2 * sub)
            }

            cache[key] = answer
            answer
        }
    }

    private static List<List<Long>> buildPowerset(List<Long> items) {
        (0..items.size()).collectMany { r -> combinations(items, r) }
    }

    private static List<List<Long>> combinations(List<Long> items, int r) {
        if (r == 0) {
            [[]]
        } else if (items.size() < r) {
            []
        } else {
            (0..<items.size())
                    .collectMany { i ->
                        combinations(items[(i + 1)..<items.size()], r - 1)
                                .collect { combo -> [items[i]] + combo }
                    }
        }
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
