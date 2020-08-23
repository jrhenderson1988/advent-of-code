package com.github.jrhenderson1988.adventofcode2019.day7

import org.junit.Assert.assertEquals
import org.junit.Test

class AmplifierControllerTest {
    @Test
    fun calculateOutputSignal() =
        mapOf(
            Pair("3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0", listOf(4, 3, 2, 1, 0)) to 43210,
            Pair(
                "3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0",
                listOf(0, 1, 2, 3, 4)
            ) to 54321,
            Pair(
                "3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0",
                listOf(1, 0, 4, 3, 2)
            ) to 65210
        )
            .forEach { (input, expected) ->
                assertEquals(expected, AmplifierController(input.first).calculateOutputSignal(input.second, 0))
            }

    @Test
    fun calculateLargestOutputSignal() =
        mapOf(
            "3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0" to 43210,
            "3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0" to 54321,
            "3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0" to 65210
        ).forEach { (input, expected) ->
            assertEquals(expected, AmplifierController(input).calculateLargestOutputSignal((0..4).toList(), 0))
        }

    @Test
    fun generatePermutations() =
        mapOf(listOf(0, 1, 2, 3, 4) to 120, listOf(0, 1, 2, 3) to 24, listOf(0, 1, 2) to 6, listOf(0, 1) to 2)
            .forEach { (input, expected) ->
                assertEquals(expected, AmplifierController.generatePermutations(input).size)
            }

    @Test
    fun calculateMaxThrusterSignalFromFeedbackLoop() =
        mapOf(
            "3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5" to 139629729,
            "3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,-5,54,1105,1,12,1,53,54," +
                    "53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10" to 18216
        ).forEach { (input, expected) ->
            assertEquals(
                expected,
                AmplifierController(input).calculateMaxThrusterSignalFromFeedbackLoop((5..9).toList(), 0)
            )
        }
}