package ru.spbau.mit.parser.Interpreter

open class Value(val value: Int, val returned: Boolean)
object DefaultValue: Value(0, false)
