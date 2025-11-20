package com.example;

import java.util.*;

public class Ast {
    // === Expressions ===
    public interface Expr {
        <R> R accept(ExprVisitor<R> v);
    }

    public interface ExprVisitor<R> {
        R visitInt(IntLiteral e);

        R visitVar(Var e);

        R visitUnary(Unary e);

        R visitBinary(Binary e);

        R visitGrouping(Grouping e);
    }

    public static class IntLiteral implements Expr {
        public final int value;

        public IntLiteral(int value) {
            this.value = value;
        }

        public <R> R accept(ExprVisitor<R> v) {
            return v.visitInt(this);
        }
    }

    public static class Var implements Expr {
        public final String name;

        public Var(String name) {
            this.name = name;
        }

        public <R> R accept(ExprVisitor<R> v) {
            return v.visitVar(this);
        }
    }

    public static class Unary implements Expr {
        public final String op;
        public final Expr right;

        public Unary(String op, Expr right) {
            this.op = op;
            this.right = right;
        }

        public <R> R accept(ExprVisitor<R> v) {
            return v.visitUnary(this);
        }
    }

    public static class Binary implements Expr {
        public final Expr left;
        public final String op;
        public final Expr right;

        public Binary(Expr left, String op, Expr right) {
            this.left = left;
            this.op = op;
            this.right = right;
        }

        public <R> R accept(ExprVisitor<R> v) {
            return v.visitBinary(this);
        }
    }

    public static class Grouping implements Expr {
        public final Expr inner;

        public Grouping(Expr e) {
            this.inner = e;
        }

        public <R> R accept(ExprVisitor<R> v) {
            return v.visitGrouping(this);
        }
    }

    // === Statements ===
    public interface Stmt {
        void accept(StmtVisitor v);
    }

    public interface StmtVisitor {
        void visitVarDecl(VarDecl s);

        void visitAssign(Assign s);

        void visitPrint(Print s);

        void visitIf(If s);

        void visitWhile(While s);

        void visitBlock(Block s);
    }

    public static class VarDecl implements Stmt {
        public final String name;
        public final Expr init; // init may be null

        public VarDecl(String name, Expr init) {
            this.name = name;
            this.init = init;
        }

        public void accept(StmtVisitor v) {
            v.visitVarDecl(this);
        }
    }

    public static class Assign implements Stmt {
        public final String name;
        public final Expr value;

        public Assign(String name, Expr value) {
            this.name = name;
            this.value = value;
        }

        public void accept(StmtVisitor v) {
            v.visitAssign(this);
        }
    }

    public static class Print implements Stmt {
        public final Expr value;

        public Print(Expr value) {
            this.value = value;
        }

        public void accept(StmtVisitor v) {
            v.visitPrint(this);
        }
    }

    public static class If implements Stmt {
        public final Expr condition;
        public final Stmt thenBranch;
        public final Stmt elseBranch; // else may be null

        public If(Expr c, Stmt t, Stmt e) {
            this.condition = c;
            this.thenBranch = t;
            this.elseBranch = e;
        }

        public void accept(StmtVisitor v) {
            v.visitIf(this);
        }
    }

    public static class While implements Stmt {
        public final Expr condition;
        public final Stmt body;

        public While(Expr c, Stmt b) {
            this.condition = c;
            this.body = b;
        }

        public void accept(StmtVisitor v) {
            v.visitWhile(this);
        }
    }

    public static class Block implements Stmt {
        public final List<Stmt> statements;

        public Block(List<Stmt> statements) {
            this.statements = statements;
        }

        public void accept(StmtVisitor v) {
            v.visitBlock(this);
        }
    }
}
