package ru.spbau.mit.parser.interpreter

import java.util.*

typealias FunctionType = (List<Int>) -> Value

class Scope {

    private val namespaces = Stack<Namespace>()

    fun currentFrame(): Namespace {
        return namespaces.peek()
    }

    fun enterFrame() {
        namespaces.push(Namespace())
    }

    fun leaveFrame() {
        namespaces.pop()
    }

    fun addFunction(name: String, block: FunctionType) {
        namespaces.peek().addFunction(name, block)
    }

    private fun getVarNamespace(name: String): Namespace? {
        return namespaces.findLast { ns ->
            ns.variables.containsKey(name)
        }
    }

    fun addVariable(name: String) {
        namespaces.peek().setVariable(name, 0)
    }

    fun setVariable(name: String, value: Int): Boolean {
        val ns = getVarNamespace(name)
        return when (ns != null) {
            true -> {
                ns!!.setVariable(name, value); true
            }
            false -> false
        }
    }

    fun getVariable(name: String): Int? {
        return getVarNamespace(name)?.variables?.get(name)
    }

    fun getFunction(name: String): FunctionType? {
        val ns = namespaces.find { ns ->
            ns.functions.containsKey(name)
        }
        return ns?.functions?.get(name)
    }

}

class Namespace {

    val functions = HashMap<String, FunctionType>()
    val variables = HashMap<String, Int>()

    fun addFunction(name: String, block: FunctionType) {
        functions[name] = block
    }

    fun setVariable(name: String, value: Int) {
        variables[name] = value
    }

}
