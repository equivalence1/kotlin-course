package ru.spbau.mit.parser

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import org.junit.Test

class TestPrinter {

    @Test
    fun testPrinter() {
        val fplLexer = FplLexer(CharStreams.fromFileName("tests/printer_tests/Test01.fpl"))
        FplParser(BufferedTokenStream(fplLexer)).file().accept(FplPrinter)
    }

}
