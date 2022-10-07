abstract class Expr {
    interface Visitor<R> {
        fun visitAssignExpr(expr: Assign): R
        fun visitBinaryExpr(expr: Binary): R
        fun visitCallExpr(expr: Call): Any?
        fun visitGetExpr(expr: Get): Any?
        fun visitGroupingExpr(expr: Grouping): R
        fun visitLiteralExpr(expr: Literal): R
        fun visitLogicalExpr(expr: Logical): R
        fun visitSetExpr(expr: Set): R
        fun visitSuperExpr(expr: Super): R
        fun visitThisExpr(expr: This): Any?
        fun visitUnaryExpr(expr: Unary): R
        fun visitVariableExpr(expr: Variable): Any?
    }

    class Assign(val name: Token, val value: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitAssignExpr(this)
        }
    }

    class Binary(val left: Expr, val operator: Token, val right: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitBinaryExpr(this)
        }
    }

    class Call(val callee: Expr, val paren: Token, val arguments: List<Expr>) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitCallExpr(this) as R
        }
    }

    class Get(val obj: Expr, val name: Token) : Expr() {
        override fun <R> accept(visitor: Visitor<R>) : R {
            return visitor.visitGetExpr(this) as R
        }
    }

    class Grouping(val expression: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>) : R {
            return visitor.visitGroupingExpr(this)
        }
    }

    class Literal(var value: Any?) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitLiteralExpr(this)
        }
    }

    class Logical(val left: Expr, val operator: Token, val right: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitLogicalExpr(this)
        }
    }

    class Set(val obj: Expr, val name: Token, val value: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitSetExpr(this)
        }
    }

    class Super(val keyword: Token, val method: Token) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitSuperExpr(this)
        }
    }

    class This(val keyword: Token) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitThisExpr(this) as R
        }
    }

    class Unary(val operator: Token, val right: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitUnaryExpr(this)
        }
    }

    class Variable(val name: Token) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitVariableExpr(this) as R
        }
    }

    abstract fun <R> accept(visitor: Visitor<R>): R
}