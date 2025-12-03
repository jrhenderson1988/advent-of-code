package uk.co.jonathonhenderson.adventofcode.days

class Day03 extends Day {
    private final List<BatteryBank> batteryBanks

    Day03(String content) {
        super(content)
        this.batteryBanks = content.trim().readLines().collect { BatteryBank.parse(it) }
    }

    @Override
    String part1() {
        batteryBanks.collect { it.maxJoltageWith2Batteries() }.sum()
    }

    @Override
    String part2() {
        batteryBanks.collect { it.maxJoltageWith12Batteries() }.sum()
    }

    static class BatteryBank {
        final List<Long> batteries

        BatteryBank(List<Long> batteries) {
            this.batteries = batteries
        }

        static BatteryBank parse(String line) {
            new BatteryBank(line.toCharArray().collect { ch -> Long.valueOf("" + ch) })
        }

        long maxJoltageWith2Batteries() {
            long max = 0
            for (int i = 0; i < batteries.size() - 1; i++) {
                for (int j = i + 1; j < batteries.size(); j++) {
                    long val = (batteries.get(i) * 10) + batteries.get(j)
                    if (val > max) {
                        max = val
                    }
                }
            }

            max
        }

        long maxJoltageWith12Batteries() {
            long max = 0

            int startPos = 0
            for (int n = 12; n >= 1; n--) {
                long best = batteries.get(startPos)
                int bestPos = startPos
                for (int i = startPos; i < batteries.size() + 1 - n; i++) {
                    if (batteries.get(i) > best) {
                        best = batteries.get(i)
                        bestPos = i
                    }
                }
                max = (max * 10) + best
                startPos = bestPos + 1
            }

            max
        }

        @Override
        String toString() {
            batteries.toListString()
        }
    }

}
