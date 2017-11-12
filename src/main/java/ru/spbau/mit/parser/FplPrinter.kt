package ru.spbau.mit.parser

import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.TerminalNode

/**
 * This object created only for testing purposes, following this issue: https://github.com/java-course-au/kotlin-course/issues/35
 * ("либо сделал стабильный принтер дерева и в качестве эталона сравнивал со строкой")
 */
object FplPrinter : FplBaseVisitor<Unit>() {

    private var indent: Int = 0

    override fun visitFile(ctx: FplParser.FileContext) {
        ctx.block().accept(this)
        println()
    }

    override fun visitBlock(ctx: FplParser.BlockContext) {
        ctx.statements.forEachIndexed { index, st ->
            printIndent()
            st.accept(this)
            if (index != ctx.statements.size - 1)
                println()
        }
    }

    override fun visitBlockWithBraces(ctx: FplParser.BlockWithBracesContext) {
        println("{")

        indent++
        ctx.block().accept(this)
        println()
        indent--

        printIndent()
        print("}")
    }

    // statements {{{

    override fun visitStatement(ctx: FplParser.StatementContext) {
        // TODO something better-looking?
        ctx.function()?.accept(this)
        ctx.variable()?.accept(this)
        ctx.expression()?.accept(this)
        ctx.whileStatement()?.accept(this)
        ctx.ifStatement()?.accept(this)
        ctx.assignment()?.accept(this)
        ctx.returnStatement()?.accept(this)
    }

    override fun visitFunction(ctx: FplParser.FunctionContext) {
        print("fun ${ctx.name.text}(")
        ctx.parameterList()?.accept(this)
        print(") ")

        ctx.blockWithBraces().accept(this)
    }

    override fun visitVariable(ctx: FplParser.VariableContext) {
        print("var ${ctx.name.text}")
        if (ctx.expression() != null) {
            print(" = ")
            ctx.expression().accept(this)
        }
    }

    override fun visitWhileStatement(ctx: FplParser.WhileStatementContext) {
        print("while (")
        ctx.expression().accept(this)
        print(") ")

        ctx.blockWithBraces().accept(this)
    }

    override fun visitIfStatement(ctx: FplParser.IfStatementContext) {
        print("if (")
        ctx.expression().accept(this)
        print(") ")

        ctx.thenBlock.accept(this)

        if (ctx.elseBlock != null) {
            print(" else ")
            ctx.elseBlock.accept(this)
        }
    }

    override fun visitAssignment(ctx: FplParser.AssignmentContext) {
        print("${ctx.varName.text} = ")
        ctx.expression().accept(this)
    }

    override fun visitReturnStatement(ctx: FplParser.ReturnStatementContext) {
        print("return ")
        ctx.expression().accept(this)
    }

    // }}} statements

    // expression {{{

    override fun visitFcallExpr(ctx: FplParser.FcallExprContext) {
        print("${ctx.name.text}(")
        ctx.argumentsList()?.accept(this)
        print(")")
    }

    override fun visitAddExpr(ctx: FplParser.AddExprContext) {
        ctx.lhs.accept(this)
        print(" ${ctx.op.text} ")
        ctx.rhs.accept(this)
    }

    override fun visitMulExpr(ctx: FplParser.MulExprContext) {
        ctx.lhs.accept(this)
        print(" ${ctx.op.text} ")
        ctx.rhs.accept(this)
    }

    override fun visitCompareExpr(ctx: FplParser.CompareExprContext) {
        ctx.lhs.accept(this)
        print(" ${ctx.op.text} ")
        ctx.rhs.accept(this)
    }

    override fun visitLogicalExpr(ctx: FplParser.LogicalExprContext) {
        ctx.lhs.accept(this)
        print(" ${ctx.op.text} ")
        ctx.rhs.accept(this)
    }

    override fun visitUnaryExpr(ctx: FplParser.UnaryExprContext) {
        print(ctx.op.text)
        ctx.expression().accept(this)
    }

    override fun visitIdentifierExpr(ctx: FplParser.IdentifierExprContext) {
        ctx.Identifier().accept(this)
    }

    override fun visitIntExpr(ctx: FplParser.IntExprContext) {
        ctx.Int().accept(this)
    }

    override fun visitDoubleExpr(ctx: FplParser.DoubleExprContext) {
        ctx.Double().accept(this)
    }

    override fun visitExprInExpr(ctx: FplParser.ExprInExprContext) {
        print("(")
        ctx.expression().accept(this)
        print(")")
    }

    // }}} expression

    override fun visitParameterList(ctx: FplParser.ParameterListContext) {
        ctx.names.forEachIndexed { index, name ->
            print(name.text)
            if (index != ctx.names.size - 1)
                print(", ")
        }
    }

    override fun visitArgumentsList(ctx: FplParser.ArgumentsListContext) {
        ctx.arguments.forEachIndexed { index, arg ->
            arg.accept(this)
            if (index != ctx.arguments.size - 1)
                print(", ")
        }
    }

    override fun visitErrorNode(node: ErrorNode) {
        println("Error: (" + node.text + " | " + node.symbol + ")")
    }

    override fun visitTerminal(node: TerminalNode) {
        print(node.text)
    }

    private fun printIndent() {
        print(" ".repeat(indent * 4))
    }

}