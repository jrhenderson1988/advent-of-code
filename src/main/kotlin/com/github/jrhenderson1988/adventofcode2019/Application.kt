package com.github.jrhenderson1988.adventofcode2019

import kotlin.system.exitProcess

class Application(private val args: Array<String>) {
    fun run() {
        val (day, part) = parseDayAndPart(args.first())

        try {
            val clazz = Class.forName(this::class.java.`package`.name + ".day$day.Application")
            val method = clazz.getMethod("part$part", Array<String>::class.java)

            println(method.invoke(clazz.getConstructor()?.newInstance(), args.drop(1).toTypedArray()))
        } catch (ex: ClassNotFoundException) {
            println("Day [$day] has not been implemented.")
            exitProcess(-1)
        } catch (ex: NoSuchMethodException) {
            println("Part [$part] of Day [$day] has not been implemented.")
            exitProcess(-1)
        }
    }

    private fun parseDayAndPart(arg: String): Pair<Int, Int> {
        val slashPos = arg.indexOf('/')
        if (slashPos == -1) {
            return Pair(arg.toInt(), 1)
        }

        return Pair(arg.substring(0, slashPos).toInt(), arg.substring(slashPos + 1).toInt())
    }
}

fun main(args: Array<String>) {
    Application(args).run();
}