package ru.spbau.mit.parser

import org.antlr.v4.runtime.BaseErrorListener
import org.antlr.v4.runtime.RecognitionException
import org.antlr.v4.runtime.Recognizer

object FplErrorListener : BaseErrorListener() {
    override fun syntaxError(recognizer: Recognizer<*, *>?, offendingSymbol: Any?, line: Int, charPositionInLine: Int, msg: String?, e: RecognitionException?) {
        super.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e)
        throw RuntimeException("Syntax Error on line $line:$charPositionInLine offendingSymbol is '$offendingSymbol', msg is $msg")
    }
}