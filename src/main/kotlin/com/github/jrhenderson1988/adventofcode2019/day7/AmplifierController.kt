package com.github.jrhenderson1988.adventofcode2019.day7

class AmplifierController(private val program: String) {
    fun calculateOutputSignal(phaseSequence: List<Int>, initialSignal: Int) =
        phaseSequence.fold(initialSignal) { signal, input ->
            Amplifier(program, input).execute(signal)
        }

    fun calculateLargestOutputSignal(phaseSequence: List<Int>, initialSignal: Int) =
        generatePermutations(phaseSequence).map { sequence -> calculateOutputSignal(sequence, initialSignal) }.max()


    companion object {
        fun generatePermutations(items: List<Int>) = generatePermutations(items, items.size)

        /**
         * Iterative version of Heap's algorithm to generate permutations:
         * https://en.wikipedia.org/wiki/Heap%27s_algorithm
         */
        private fun generatePermutations(items: List<Int>, n: Int): Set<List<Int>> {
            val elements = items.toMutableList()
            val result = mutableSetOf<List<Int>>()
            val indexes = (0 until n).map { 0 }.toMutableList()

            result.add(elements.toList())

            var i = 0
            while (i < n) {
                if (indexes[i] < i) {
                    val a = if (i % 2 == 0) 0 else indexes[i]
                    val b = i

                    val tmp = elements[a]
                    elements[a] = elements[b]
                    elements[b] = tmp

                    result.add(elements.toList())
                    indexes[i] = indexes[i] + 1
                    i = 0
                } else {
                    indexes[i] = 0
                    i++
                }
            }

            return result
        }
    }
}