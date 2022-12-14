/*
* THIS IS INCORRECT, IF WE ARE GOING TO USE IT WE NEED TO MODIFY IT TO GENERATE
* KOTLIN CODE INSTEAD OF JAVA
* */

@file:Suppress("SameParameterValue")

package tool

import java.io.File
import java.io.IOException
import kotlin.system.exitProcess


object GenerateAst {
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        if (args.size != 1) {
            System.err.println("Usage: generate_ast <output directory>")
            exitProcess(64)
        }

        val outputDir = args[0]

        defineAst(outputDir, "klox.Expr", listOf(
            "Assign   : name: klox.Token, value: klox.Expr",
            "Binary   : left: klox.Expr, operator: klox.Token, right: klox.Expr",
            "Grouping : expression: klox.Expr",
            "Literal  : value: Object",
            "Unary    : operator: klox.Token, right: klox.Expr",
            "Variable : name: klox.Token"
            )
        )

        defineAst(outputDir, "klox.Stmt", listOf(
            "Block      : statements: List<klox.Stmt>",
            "Expression : expression: klox.Expr",
            "Print      : expression: klox.Expr",
            "Var        : name: klox.Token, initializer: klox.Expr"
        ))
    }

    @Throws(IOException::class)
    private fun defineAst(outputDir: String, baseName: String, types: List<String>) {
        val path = "$outputDir/$baseName.kt"
        val writer = File(path).printWriter()

        writer.println("package com.craftinginterpreters.lox;")
        writer.println()
        writer.println("import java.util.List;")
        writer.println()
        writer.println("abstract class $baseName {")

        defineVisitor(path, baseName, types)

        for (type in types) {
            val className = type.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].trim { it <= ' ' }
            val fields = type.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].trim { it <= ' ' }
            defineType(path, baseName, className, fields)
        }

        writer.println()
        writer.println("  abstract <R> R accept(Visitor<R> visitor);")

        writer.println("}")
        writer.close()
    }

    private fun defineType(path: String, baseName: String, className: String, fieldList: String) {
        val writer = File(path).printWriter()

        writer.println("  static class $className extends $baseName {")
        writer.println("    $className($fieldList) {")

        // Store parameters in fields.
        val fields: List<String> = fieldList.split(", ")
        for (field in fields) {
            val name = field.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            writer.println("      this.$name = $name;")
        }
        writer.println("    }")

        writer.println()
        writer.println("    @Override")
        writer.println("    <R> R accept(Visitor<R> visitor) {")
        writer.println("      return visitor.visit$className$baseName(this);")
        writer.println("    }")

        // Fields.
        writer.println()
        for (field in fields) {
            writer.println("    final $field;")
        }
        writer.println("  }")

    }

    private fun defineVisitor(path: String, baseName: String, types: List<String>) {
        val writer = File(path).printWriter()

        writer.println("  interface Visitor<R> {")
        for (type: String in types) {
            val typeName = type.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].trim { it <= ' ' }
            writer.println("    R visit$typeName$baseName($typeName ${baseName.lowercase()});")
            writer.println("  }")
        }
    }

}