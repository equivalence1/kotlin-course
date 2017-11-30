package ru.spbau.mit

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import ru.spbau.mit.parser.FplErrorListener
import ru.spbau.mit.parser.FplLexer
import ru.spbau.mit.parser.FplParser
import ru.spbau.mit.parser.Interpreter.FplInterpreter


fun main(args: Array<String>) {
    if (args.size != 1) {
        println("Should provide name of a file")
        return
    }

    val fplLexer = FplLexer(CharStreams.fromFileName(args[0]))
    fplLexer.addErrorListener(FplErrorListener)
    val parser = FplParser(BufferedTokenStream(fplLexer))
    parser.addErrorListener(FplErrorListener)

    val interpreter = FplInterpreter()
    try {
        interpreter.evaluate(parser.file())
    } catch (e: Exception) {
        println("Interpreter failed with error:\n" + e.message)
    }
}
