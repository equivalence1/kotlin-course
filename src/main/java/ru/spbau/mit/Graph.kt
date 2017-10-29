package ru.spbau.mit

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