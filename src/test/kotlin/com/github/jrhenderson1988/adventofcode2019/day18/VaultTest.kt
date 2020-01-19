package com.github.jrhenderson1988.adventofcode2019.day18

import org.junit.Assert.assertEquals
import org.junit.Test

class VaultTest {
    companion object {
        const val INPUT_A =
            """
            #########
            #b.A.@.a#
            #########
            """

        const val INPUT_B =
            """
            ########################
            #f.D.E.e.C.b.A.@.a.B.c.#
            ######################.#
            #d.....................#
            ######################## 
            """

        const val INPUT_C =
            """
            ########################
            #...............b.C.D.f#
            #.######################
            #.....@.a.B.c.d.A.e.F.g#
            ######################## 
            """

        const val INPUT_D =
            """
            #################
            #i.G..c...e..H.p#
            ########.########
            #j.A..b...f..D.o#
            ########@########
            #k.E..a...g..B.n#
            ########.########
            #l.F..d...h..C.m#
            #################
            """

        const val INPUT_E =
            """
            ########################
            #@..............ac.GI.b#
            ###d#e#f################
            ###A#B#C################
            ###g#h#i################
            ########################
            """

        const val INPUT_F =
            """
            #######
            #a.#Cd#
            ##...##
            ##.@.##
            ##...##
            #cB#Ab#
            ####### 
            """

        const val INPUT_G =
            """
            ###############
            #d.ABC.#.....a#
            ######...######
            ######.@.######
            ######...######
            #b.....#.....c#
            ###############
            """

        const val INPUT_H =
            """
            #############
            #DcBa.#.GhKl#
            #.###...#I###
            #e#d#.@.#j#k#
            ###C#...###J#
            #fEbA.#.FgHi#
            #############
            """

        const val INPUT_I =
            """
            #############
            #g#f.D#..h#l#
            #F###e#E###.#
            #dCba...BcIJ#
            #####.@.#####
            #nK.L...G...#
            #M###N#H###.#
            #o#m..#i#jk.#
            #############
            """
    }

    @Test
    fun shortestPathToCollectAllKeys() =
        mapOf(
            INPUT_A to 8,
            INPUT_B to 86,
            INPUT_C to 132,
            INPUT_D to 136,
            INPUT_E to 81
        )
            .forEach { (input, expected) ->
                assertEquals(expected, Vault.parse(input.trimIndent()).shortestPathToCollectAllKeys())
            }

    @Test
    fun fewestStepsToCollectAllKeysInAllQuadrants() =
        mapOf(
            INPUT_F to 8
//            INPUT_G to 24
//            INPUT_H to 32
//            INPUT_I to 72
        ).forEach { (input, expected) ->
            assertEquals(expected, QuadVault.parse(input).shortestPathToCollectAllKeys())
        }
}