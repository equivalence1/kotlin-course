package ru.spbau.mit

import java.io.File
import java.util.*

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        throw IllegalArgumentException("should specify input file")
    }

    Scanner(File(args[0]).inputStream()).use {
        val g = Graph(it)
        val solver = Solver(g)
        println(solver.solve())
    }
}
