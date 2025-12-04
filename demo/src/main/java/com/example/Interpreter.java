package com.example;

import java.util.List;

import com.example.Ast.*;

public class Interpreter implements Ast.ExprVisitor<Integer>, Ast.StmtVisitor {

    private final Environment env = new Environment();
    private final StringBuilder out;

    public Interpreter(StringBuilder out) {
        this.out = out;
    }

    public void run(List<Stmt> program) {
        for (Stmt s : program)
            s.accept(this);
    }

    // ===== Statement visitors =====

    @Override
    public void visitVarDecl(VarDecl s) {
        int v = (s.init == null) ? 0 : eval(s.init);
        env.declare(s.name, v);
    }

    @Override
    public void visitAssign(Assign s) {
        env.set(s.name, eval(s.value));
    }

    @Override
    public void visitPrint(Print s) {
        if (hasString(s.value)) {
            out.append(stringify(s.value)).append('\n');
        } else {
            out.append(eval(s.value)).append('\n');
        }
    }

    @Override
    public void visitIf(If s) {
        if (truthy(eval(s.condition)))
            s.thenBranch.accept(this);
        else if (s.elseBranch != null)
            s.elseBranch.accept(this);
    }

    @Override
    public void visitWhile(While s) {
        while (truthy(eval(s.condition)))
            s.body.accept(this);
    }

    @Override
    public void visitBlock(Block s) {
        env.push();
        try {
            for (Stmt st : s.statements)
                st.accept(this);
        } finally {
            env.pop();
        }
    }

    // ===== Expression visitors (numeric) =====

    private int eval(Expr e) {
        return e.accept(this);
    }

    @Override
    public Integer visitInt(IntLiteral e) {
        return e.value;
    }

    @Override
    public Integer visitString(StringLiteral e) {
        // not used in numeric context; but needed to implement interface
        return 0;
    }

    @Override
    public Integer visitVar(Var e) {
        return env.get(e.name);
    }

    @Override
    public Integer visitUnary(Unary e) {
        int r = eval(e.right);
        if ("-".equals(e.op))
            return -r;
        throw new RuntimeException("Unknown unary op " + e.op);
    }

    @Override
    public Integer visitBinary(Binary e) {
        int a = eval(e.left);
        int b = eval(e.right);
        switch (e.op) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                if (b == 0)
                    throw new RuntimeException("Runtime error: division by zero");
                return a / b;
            case "<":
                return a < b ? 1 : 0;
            case "<=":
                return a <= b ? 1 : 0;
            case ">":
                return a > b ? 1 : 0;
            case ">=":
                return a >= b ? 1 : 0;
            case "==":
                return a == b ? 1 : 0;
            case "!=":
                return a != b ? 1 : 0;
            default:
                throw new RuntimeException("Unknown binary op " + e.op);
        }
    }

    // 🔹 This is the missing method the compiler is asking for
    @Override
    public Integer visitGrouping(Grouping e) {
        return eval(e.inner);
    }

    private boolean truthy(int v) {
        return v != 0;
    }

    // ===== String helpers for print =====

    private boolean hasString(Expr e) {
        if (e instanceof StringLiteral)
            return true;
        if (e instanceof Grouping g)
            return hasString(g.inner);
        if (e instanceof Binary b && "+".equals(b.op)) {
            return hasString(b.left) || hasString(b.right);
        }
        return false;
    }

    private String stringify(Expr e) {
        if (e instanceof StringLiteral s)
            return s.value;
        if (e instanceof IntLiteral i)
            return Integer.toString(i.value);
        if (e instanceof Var v)
            return Integer.toString(env.get(v.name));
        if (e instanceof Grouping g)
            return stringify(g.inner);
        if (e instanceof Binary b && "+".equals(b.op)) {
            return stringify(b.left) + stringify(b.right);
        }
        // fallback: numeric evaluation
        return Integer.toString(eval(e));
    }
}
