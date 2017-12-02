package ru.spbau.mit.parser.interpreter

open class Value(val value: Int, val returned: Boolean = false)
object DefaultValue : Value(0)
