package com.github.jrhenderson1988.adventofcode2019.day25

data class State(val location: String, val doors: List<Direction>, val items: List<String>) {
    var input: String? = null

    companion object {
        private const val LOCATION_START = "=="
        private const val LOCATION_END = "=="
        private const val DOORS_HERE_LEAD_START = "Doors here lead:"
        private const val DOORS_HERE_LEAD_END = "\n\n"
        private const val ITEMS_HERE_START = "Items here:"
        private const val ITEMS_HERE_END = "\n\n"

        fun parse(input: String): List<State> {
            val states = mutableListOf<State>()
            var from = 0

//            println(input)
            while (true) {
                val locationStart = input.indexOf(LOCATION_START, from, true)
                if (locationStart == -1) {
                    break
                }

                val locationEnd = input.indexOf(LOCATION_END, locationStart + 1, true)
                val location = input.substring(locationStart + LOCATION_START.length, locationEnd)
                    .trim()
                    .toLowerCase()
                from = locationEnd + 1

                val doorsStart = input.indexOf(DOORS_HERE_LEAD_START, from, true)
                val doors = if (doorsStart > -1) {
                    val doorsEnd = input.indexOf(DOORS_HERE_LEAD_END, doorsStart + 1, true)
                    from = doorsEnd + 1
                    input.substring(doorsStart + DOORS_HERE_LEAD_START.length, doorsEnd)
                        .trim()
                        .lines()
                        .map { it.trimStart('-', ' ').trim() }
                        .map { Direction.parse(it) }
                } else {
                    listOf()
                }

                val itemsStart = input.indexOf(ITEMS_HERE_START, from, true)
                val items = if (itemsStart > -1) {
                    val itemsEnd = input.indexOf(ITEMS_HERE_END, itemsStart + 1, true)
                    from = itemsEnd + 1
                    input.substring(itemsStart + ITEMS_HERE_START.length, itemsEnd)
                        .trim()
                        .lines()
                        .map { it.trimStart('-', ' ').trim() }
                } else {
                    listOf()
                }

                val state = State(location, doors, items)
                state.input = input
                states.add(state)
            }

            if (states.isEmpty()) {
                error("Invalid state input: No locations")
            } else if (states.size > 2) {
                error("Invalid state input: More than 2 locations")
            }

            return states.toList()
        }
    }
}