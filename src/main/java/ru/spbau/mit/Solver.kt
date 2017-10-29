package ru.spbau.mit

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