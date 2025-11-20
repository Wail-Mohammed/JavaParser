package com.example;

import java.util.*;

import com.example.Ast.Grouping;

public class Interpreter implements Ast.ExprVisitor<Integer>, Ast.StmtVisitor {
    private final Environment env = new Environment();
    private final StringBuilder out;

    public Interpreter(StringBuilder out) {
        this.out = out;
    }

    public void run(List<Ast.Stmt> program) {
        for (Ast.Stmt s : program) {
            s.accept(this);
        }
    }

    // ----- statements -----
    @Override
    public void visitVarDecl(Ast.VarDecl s) {
        int v = (s.init == null) ? 0 : eval(s.init);
        env.declare(s.name, v);
    }

    @Override
    public void visitAssign(Ast.Assign s) {
        env.set(s.name, eval(s.value));
    }

    @Override
    public void visitPrint(Ast.Print s) {
        int v = eval(s.value);
        out.append(v).append('\n');
        // System.out.println("[console] " + v); // optional mirror
    }

    @Override
    public void visitIf(Ast.If s) {
        if (truthy(eval(s.condition))) {
            s.thenBranch.accept(this);
        } else if (s.elseBranch != null) {
            s.elseBranch.accept(this);
        }
    }

    @Override
    public void visitWhile(Ast.While s) {
        while (truthy(eval(s.condition))) {
            s.body.accept(this);
        }
    }

    @Override
    public void visitBlock(Ast.Block s) {
        env.push();
        try {
            for (Ast.Stmt st : s.statements) {
                st.accept(this);
            }
        } finally {
            env.pop();
        }
    }

    // ----- expressions -----
    private int eval(Ast.Expr e) {
        return e.accept(this);
    }

    @Override
    public Integer visitInt(Ast.IntLiteral e) {
        return e.value;
    }

    @Override
    public Integer visitVar(Ast.Var e) {
        return env.get(e.name);
    }

    @Override
    public Integer visitUnary(Ast.Unary e) {
        int r = eval(e.right);
        if ("-".equals(e.op))
            return -r;
        throw new RuntimeException("Unknown unary op " + e.op);
    }

    @Override
    public Integer visitBinary(Ast.Binary e) {
        // short-circuit
        if ("&&".equals(e.op))
            return truthy(eval(e.left)) ? (truthy(eval(e.right)) ? 1 : 0) : 0;
        if ("||".equals(e.op))
            return truthy(eval(e.left)) ? 1 : (truthy(eval(e.right)) ? 1 : 0);

        int a = eval(e.left), b = eval(e.right);
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

    private boolean truthy(int v) {
        return v != 0;
    }

    @Override
    public Integer visitGrouping(Grouping e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitGrouping'");
    }
}
