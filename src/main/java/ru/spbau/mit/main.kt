package ru.spbau.mit

import java.io.File
import java.util.*

class Graph private constructor(private val scanner: Scanner) {

    var nrVertices = 0
    val edges: MutableList<MutableList<Int>> = ArrayList()
    private var nrEdges = 0

    private fun read() {
        nrVertices = scanner.nextInt()
        nrEdges = scanner.nextInt()

        for (i in 1..nrVertices) {
            edges.add(ArrayList())
        }

        for (i in 1..nrEdges) {
            val u = scanner.nextInt() - 1
            val v = scanner.nextInt() - 1

            edges[u].add(v)
            edges[v].add(u)
        }
    }

    companion object {
        fun readFrom(scanner: Scanner): Graph {
            val g = Graph(scanner)
            g.read()
            return g
        }
    }

}

class Solver(private val g: Graph) {

    private val was = BooleanArray(g.nrVertices, {false})

    private fun hasCircle(u: Int, p: Int): Boolean {
        if (was[u]) {
            return false
        }
        was[u] = true

        return g.edges[u].any {
            v -> (was[v] && v != p) || hasCircle(v, u)
        }
    }

    private fun solve(u: Int): Int {
        return when {
            was[u] -> 0
            !hasCircle(u, u) -> 1
            else -> 0
        }
    }

    fun solve(): Int {
        return (1..g.nrVertices).sumBy({index -> solve(index - 1)})
    }

}

fun main(args: Array<String>) {
    val inputStream = if (!args.isEmpty()) File(args[0]).inputStream() else System.`in`

    Scanner(inputStream).use {
        val g = Graph.readFrom(it)
        val solver = Solver(g)
        println(solver.solve())
    }
}
