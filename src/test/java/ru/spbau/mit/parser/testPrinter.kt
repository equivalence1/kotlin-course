package ru.spbau.mit.parser

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import org.junit.Test
import kotlin.test.assertEquals

class TestPrinter {

    @Test
    fun testPrinter() {
        redirectOut("tests/output")

        val fplLexer = FplLexer(CharStreams.fromFileName("tests/printer_tests/Test01.fpl"))
        FplParser(BufferedTokenStream(fplLexer)).file().accept(FplPrinter)
        System.out.flush()

        val expected = readFile("tests/printer_tests/Test01.fpl").filterNot { it.trim().startsWith("//") }
                .flatMap { it.split(' ') }
                .joinToString("") { s -> s.trim() }
        val real = readFile("tests/output")
                .flatMap { it.split(' ') }
                .joinToString("") { s -> s.trim() }

        assertEquals(expected, real)
    }

}
