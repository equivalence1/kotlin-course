package ru.spbau.mit.parser

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import org.junit.Test

class TestParser {

    @Test
    fun testParserOk() {
        val fplLexer = FplLexer(CharStreams.fromFileName("tests/test_parser/ok.fpl"))
        fplLexer.addErrorListener(FplErrorListener)
        val parser = FplParser(BufferedTokenStream(fplLexer))
        parser.addErrorListener(FplErrorListener)
        parser.file().accept(FplPrinter)

    }

    @Test(expected = RuntimeException::class)
    fun testParserFail() {
        val fplLexer = FplLexer(CharStreams.fromFileName("tests/test_parser/fail.fpl"))
        fplLexer.addErrorListener(FplErrorListener)
        val parser = FplParser(BufferedTokenStream(fplLexer))
        parser.addErrorListener(FplErrorListener)
        parser.file().accept(FplPrinter)
    }

}
