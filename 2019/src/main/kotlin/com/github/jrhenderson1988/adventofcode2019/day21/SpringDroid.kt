package com.github.jrhenderson1988.adventofcode2019.day21

class SpringDroid(private val program: List<Long>) {
    fun execute(instructions: String): Long {
        val cpu = IntCodeComputer(program)

        instructions.trimIndent().trim().forEach { ch -> cpu.queueInput(ch.toLong()) }
        cpu.queueInput('\n'.toLong())

        val response = cpu.execute()!!

        return if (response < 128L) {
            println(cpu.outputs.map { it.toChar() }.joinToString(""))
            -1
        } else {
            response
        }

//        return if (cpu.outputs.size == 1) {
//            println(cpu.outputs)
//            cpu.outputs.first()
//        } else {
//        println(cpu.outputs.size)
//        println(cpu.outputs.last())
//        println(cpu.outputs.map { it.toChar() }.joinToString(""))
//            -1
//        }
    }
}