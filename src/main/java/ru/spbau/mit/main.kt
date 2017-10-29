package ru.spbau.mit

import java.io.File
import java.util.*

class Graph(scanner: Scanner) {

    val n = scanner.nextInt()
    private val m = scanner.nextInt()
    private val g: ArrayList<ArrayList<Int>> = ArrayList()

    init {
        for (i in 1..n) {
            g.add(ArrayList())
        }

        for (i in 1..m) {
            val u = scanner.nextInt() - 1
            val v = scanner.nextInt() - 1

            g[u].add(v)
            g[v].add(u)
        }
    }

    fun <T> foldIndexed(initValue: T, body: (T, Int) -> T): T {
        return g.foldIndexed(initValue, {index, value, _ -> body(value, index)})
    }

    fun <T> adjustentFold(u: Int, initValue: T, body: (T, Int) -> T): T {
        return g[u].fold(initValue, body)
    }

}

class Solver(private val g: Graph) {

    private val was = BooleanArray(g.n, {false})

    private fun hasCircle(u: Int, p: Int): Boolean {
        if (was[u])
            return false
        was[u] = true

        return g.adjustentFold(u, false) {
            prevValue, v -> prevValue || (was[v] && v != p) || hasCircle(v, u)
        }
    }

    private fun solve(u: Int): Int {
        if (was[u])
            return 0
        if (!hasCircle(u, u))
            return 1
        return 0
    }

    fun solve(): Int {
        return g.foldIndexed(0) {
            value, index -> value + solve(index)
        }
    }
}

fun main(args: Array<String>) {
    val inputStream = if (!args.isEmpty()) File(args[0]).inputStream() else System.`in`

    Scanner(inputStream).use {
        val g = Graph(it)
        val solver = Solver(g)
        println(solver.solve())
    }
}
