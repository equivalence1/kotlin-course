package ru.spbau.mit.parser.interpreter

open class Value(val value: Int, val returned: Boolean)
object DefaultValue : Value(0, false)
