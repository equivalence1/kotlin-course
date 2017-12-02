package ru.spbau.mit.parser.interpreter

import ru.spbau.mit.parser.FplBaseVisitor
import ru.spbau.mit.parser.FplParser

class FplInterpreter : FplBaseVisitor<Value>() {

    private val scope = Scope()

    fun evaluate(ctx: FplParser.FileContext): Int {
        return ctx.accept(this).value
    }

    override fun visitFile(ctx: FplParser.FileContext): Value {
        scope.enterFrame()

        scope.addFunction("println") { args ->
            println(args.joinToString(", "))
            DefaultValue
        }

        return ctx.block().accept(this)
    }

    override fun visitBlock(ctx: FplParser.BlockContext): Value {
        // I really don't know how to implement it with streams
        // Can be done with foldl, but it would be ugly
        for (st in ctx.statements) {
            val res = st.accept(this)
            if (res.returned)
                return res
        }
        return DefaultValue
    }

    override fun visitBlockWithBraces(ctx: FplParser.BlockWithBracesContext): Value {
        scope.enterFrame()
        val res = ctx.block().accept(this)
        scope.leaveFrame()
        return res
    }

    // statements {{{

    override fun visitFunction(ctx: FplParser.FunctionContext): Value {
        with(ctx) {
            if (scope.currentFrame().functions.containsKey(name.text)) {
                throw RuntimeException("line ${ctx.start.line}: trying to overload function ${name.text}")
            }

            scope.addFunction(name.text) { args ->
                val params = parameterList().names

                if (params.size != args.size) {
                    throw RuntimeException("function ${name.text} has exactly ${params.size} parameters, ${args.size} were given")
                }

                scope.enterFrame()
                params.forEachIndexed { index, token ->
                    scope.addVariable(token.text)
                    scope.setVariable(token.text, args[index])
                }
                val result = blockWithBraces().accept(this@FplInterpreter)
                scope.leaveFrame()

                result
            }
        }

        // here we only register function in scope, so just return @DefaultValue
        return DefaultValue
    }

    override fun visitVariable(ctx: FplParser.VariableContext): Value {
        if (scope.currentFrame().variables.containsKey(ctx.name.text)) {
            throw RuntimeException("line ${ctx.start.line}: trying to overload variable ${ctx.name.text} ")
        }
        val varName = ctx.name.text
        scope.addVariable(varName)
        ctx.expression()?.let {
            val result = it.accept(this)
            scope.setVariable(varName, result.value)
        }
        return Value(scope.getVariable(varName)!!, false)
    }

    override fun visitWhileStatement(ctx: FplParser.WhileStatementContext): Value {
        while (true) {
            val condition = ctx.expression().accept(this)
            if (condition.value == 0) {
                break
            }

            val result = ctx.blockWithBraces().accept(this)
            if (result.returned) {
                return result
            }
        }
        return DefaultValue
    }

    override fun visitIfStatement(ctx: FplParser.IfStatementContext): Value {
        val condition = ctx.expression().accept(this)

        if (condition.value != 0) {
            return ctx.thenBlock.accept(this)
        } else {
            if (ctx.elseBlock != null) {
                return ctx.elseBlock.accept(this)
            }
        }

        return DefaultValue
    }

    override fun visitAssignment(ctx: FplParser.AssignmentContext): Value {
        val value = ctx.expression().accept(this)
        val varName = ctx.varName.text
        if (!scope.setVariable(varName, value.value)) {
            throw RuntimeException("line ${ctx.start.line}: variable $varName is not defined")
        }
        return DefaultValue
    }

    override fun visitReturnStatement(ctx: FplParser.ReturnStatementContext): Value {
        val value = ctx.expression().accept(this).value
        return Value(value, true)
    }

    // }}} statements

    // expression {{{

    override fun visitFcallExpr(ctx: FplParser.FcallExprContext): Value {
        val arguments: List<Int> = ctx.argumentsList().arguments.map { arg ->
            arg.accept(this).value
        }
        val result = scope.getFunction(ctx.name.text)
        if (result === null) {
            throw RuntimeException("line ${ctx.start.line}: function ${ctx.name.text} not found")
        }
        try {
            return Value(result.invoke(arguments).value, false)
        } catch (e: RuntimeException) {
            throw RuntimeException("function call at line ${ctx.start.line} produced an exception:\n" + e.message)
        }
    }

    override fun visitAddExpr(ctx: FplParser.AddExprContext): Value {
        val lhs = ctx.lhs.accept(this).value
        val rhs = ctx.rhs.accept(this).value

        val res = if (ctx.op.text == "+") lhs + rhs else lhs - rhs
        return Value(res, false)
    }

    override fun visitMulExpr(ctx: FplParser.MulExprContext): Value {
        val lhs = ctx.lhs.accept(this).value
        val rhs = ctx.rhs.accept(this).value
        return when (ctx.op.text) {
            "*" -> Value(lhs * rhs, false)
            "/" -> Value(lhs / rhs, false)
            "%" -> Value(lhs % rhs, false)
            else -> throw IllegalArgumentException("bad mul expr")
        }
    }

    override fun visitCompareExpr(ctx: FplParser.CompareExprContext): Value {
        val lhs = ctx.lhs.accept(this).value
        val rhs = ctx.rhs.accept(this).value
        return when (ctx.op.text) {
            "<"  -> Value(if (lhs <  rhs) 1 else 0, false)
            "<=" -> Value(if (lhs <= rhs) 1 else 0, false)
            ">"  -> Value(if (lhs >  rhs) 1 else 0, false)
            ">=" -> Value(if (lhs >= rhs) 1 else 0, false)
            "==" -> Value(if (lhs == rhs) 1 else 0, false)
            "!=" -> Value(if (lhs != rhs) 1 else 0, false)
            else -> throw IllegalArgumentException("bad cmp expr")
        }
    }

    override fun visitLogicalExpr(ctx: FplParser.LogicalExprContext): Value {
        val lhs = ctx.lhs.accept(this).value
        val rhs = ctx.rhs.accept(this).value

        return when (ctx.op.text) {
            "||" -> Value(if ((lhs != 0) || (rhs != 0)) 1 else 0, false)
            "&&" -> Value(if ((lhs != 0) && (rhs != 0)) 1 else 0, false)
            else -> throw IllegalArgumentException("bad logical expr")
        }
    }

    override fun visitUnaryExpr(ctx: FplParser.UnaryExprContext): Value {
        val value = ctx.expression().accept(this).value
        return when (ctx.op.text) {
            "+" -> Value(value, false)
            "-" -> Value(-value, false)
            else -> throw IllegalArgumentException("bad unary expr")
        }
    }

    override fun visitIdentifierExpr(ctx: FplParser.IdentifierExprContext): Value {
        val value = scope.getVariable(ctx.Identifier().text)
        if (value === null) {
            throw RuntimeException("line ${ctx.start.line}: variable ${ctx.Identifier().text} is not defined")
        }
        return Value(value, false)
    }

    override fun visitIntExpr(ctx: FplParser.IntExprContext): Value {
        return Value(ctx.Int().text.toInt(), false)
    }

    override fun visitExprInExpr(ctx: FplParser.ExprInExprContext): Value {
        return ctx.expression().accept(this)
    }

    // }}} expression

    override fun visitParameterList(ctx: FplParser.ParameterListContext): Value {
        throw IllegalStateException("visitParameterList should never be called in interpreter")
    }

    override fun visitArgumentsList(ctx: FplParser.ArgumentsListContext): Value {
        throw IllegalStateException("visitArgumentList should never be called in interpreter")
    }

}
