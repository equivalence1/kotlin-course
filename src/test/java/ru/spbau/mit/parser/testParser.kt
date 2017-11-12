package ru.spbau.mit.parser

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import org.junit.Test

/**
 * These tests only check that parser does or does not fail to parse
 */
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
    fun testParserIdentFail() {
        val fplLexer = FplLexer(CharStreams.fromFileName("tests/test_parser/ident_fail.fpl"))
        fplLexer.addErrorListener(FplErrorListener)
        val parser = FplParser(BufferedTokenStream(fplLexer))
        parser.addErrorListener(FplErrorListener)
        parser.file().accept(FplPrinter)
    }

    @Test(expected = RuntimeException::class)
    fun testParserNumberFail() {
        val fplLexer = FplLexer(CharStreams.fromFileName("tests/test_parser/number_fail.fpl"))
        fplLexer.addErrorListener(FplErrorListener)
        val parser = FplParser(BufferedTokenStream(fplLexer))
        parser.addErrorListener(FplErrorListener)
        parser.file().accept(FplPrinter)
    }

    @Test(expected = RuntimeException::class)
    fun testParserExpressionFail() {
        val fplLexer = FplLexer(CharStreams.fromFileName("tests/test_parser/expression_fail.fpl"))
        fplLexer.addErrorListener(FplErrorListener)
        val parser = FplParser(BufferedTokenStream(fplLexer))
        parser.addErrorListener(FplErrorListener)
        parser.file().accept(FplPrinter)
    }

}
