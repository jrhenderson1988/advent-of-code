package com.github.jrhenderson1988.adventofcode2019.day20

import org.junit.Test

class DonutMazeTest {
    companion object {
        const val INPUT = """
                     A           
                     A           
              #######.#########  
              #######.........#  
              #######.#######.#  
              #######.#######.#  
              #######.#######.#  
              #####  B    ###.#  
            BC...##  C    ###.#  
              ##.##       ###.#  
              ##...DE  F  ###.#  
              #####    G  ###.#  
              #########.#####.#  
            DE..#######...###.#  
              #.#########.###.#  
            FG..#########.....#  
              ###########.#####  
                         Z       
                         Z       
        """
    }

    @Test
    fun test() {
        println(DonutMaze.parse(INPUT.trimIndent()).render())
    }
}