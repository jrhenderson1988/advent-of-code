package uk.co.jonathonhenderson.adventofcode.days

import groovy.transform.Canonical

import static uk.co.jonathonhenderson.adventofcode.utils.PathFinding.bfs

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
        "TODO"
    }

    private long minimumButtonPressesToReachTargetLights(MachineInstruction instruction) {
        def start = [false] * instruction.indicatorLights.size()
        def target = instruction.indicatorLights
        def path = bfs(start, { it == target }) { state -> instruction.wiringSchematics.collect { btn -> pressButton(state, btn) } }

        (path.size() - 1) as long
    }


    private List<Boolean> pressButton(List<Boolean> lights, List<Integer> button) {
        (0..(lights.size() - 1)).collect { i -> button.contains(i) ? !lights.get(i) : lights.get(i) }
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
    }
}
