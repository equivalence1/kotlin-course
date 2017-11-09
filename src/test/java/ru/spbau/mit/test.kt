package ru.spbau.mit
import org.junit.Test
import java.io.File
import java.util.*
import kotlin.test.assertEquals

class TestSolution {
    @Test
    fun testSamples() {
        File("Samples").listFiles().filter({file -> file.name.endsWith(".in")}).sorted().forEach {
            println("file: " + it.name)
            val g = Graph.readFrom(Scanner(it))
            val solver = Solver(g)
            val rightAnswer = Scanner(File(it.path.replaceAfter('.', "out"))).nextInt()
            assertEquals(rightAnswer, solver.solve())
        }

    }
}
