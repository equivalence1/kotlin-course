package ru.spbau.mit.parser.interpreter

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import org.junit.Test
import ru.spbau.mit.parser.FplLexer
import ru.spbau.mit.parser.FplParser
import ru.spbau.mit.parser.Interpreter.FplInterpreter
import ru.spbau.mit.parser.readFile
import ru.spbau.mit.parser.redirectOut
import kotlin.test.assertEquals

/**
 * These tests only check that parser does or does not fail to parse
 */
class TestInterpreter {

    @Test
    fun testIntOk() {
        redirectOut("tests/output")

        val fplLexer = FplLexer(CharStreams.fromFileName("tests/interpreter_tests/Test01.fpl"))
        val parser = FplParser(BufferedTokenStream(fplLexer))
        val int = FplInterpreter(parser)
        int.evaluate()
        System.out.flush()

        val expected = readFile("tests/interpreter_tests/Test01.expected")
        val actual = readFile("tests/output")

        assertEquals(expected, actual)
    }

    @Test(expected = RuntimeException::class)
    fun testParserIdentFail() {
        val fplLexer = FplLexer(CharStreams.fromFileName("tests/interpreter_tests/Test02.fpl"))
        val parser = FplParser(BufferedTokenStream(fplLexer))
        val int = FplInterpreter(parser)
        int.evaluate()
    }

    @Test(expected = RuntimeException::class)
    fun testParserNumberFail() {
        val fplLexer = FplLexer(CharStreams.fromFileName("tests/interpreter_tests/Test03.fpl"))
        val parser = FplParser(BufferedTokenStream(fplLexer))
        val int = FplInterpreter(parser)
        int.evaluate()
    }

}
