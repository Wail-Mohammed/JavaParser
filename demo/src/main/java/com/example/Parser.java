package com.example;

import java.util.*;
import static com.example.TokenType.*;
import com.example.Ast.*;

public class Parser {
    private final List<Token> ts;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.ts = tokens;
    }

    public List<Stmt> parseProgram() {
        List<Stmt> out = new ArrayList<>();
        while (!match(EOF)) {
            out.add(statement());
        }
        return out;
    }

    private Stmt statement() {
        if (match(KW_INT))
            return varDecl();
        if (match(KW_PRINT))
            return printStmt();
        if (match(KW_IF))
            return ifStmt();
        if (match(KW_WHILE))
            return whileStmt();
        if (match(L_BRACE))
            return block();
        // assignment
        Token id = consume(IDENT, "Expected identifier");
        consume(ASSIGN, "Expected '=' after identifier");
        Expr value = expression();
        consume(SEMI, "Expected ';' after assignment");
        return new Assign(id.lexeme, value);
    }

    private Stmt varDecl() {
        Token name = consume(IDENT, "Expected variable name after 'int'");
        Expr init = null;
        if (match(ASSIGN))
            init = expression();
        consume(SEMI, "Expected ';' after declaration");
        return new VarDecl(name.lexeme, init);
    }

    private Stmt printStmt() {
        consume(L_PAREN, "Expected '(' after print");
        Expr v = expression();
        consume(R_PAREN, "Expected ')' after print expression");
        consume(SEMI, "Expected ';' after print");
        return new Print(v);
    }

    private Stmt ifStmt() {
        consume(L_PAREN, "Expected '(' after if");
        Expr cond = expression();
        consume(R_PAREN, "Expected ')' after if condition");
        Stmt thenB = statement();
        Stmt elseB = null;
        if (match(KW_ELSE))
            elseB = statement();
        return new If(cond, thenB, elseB);
    }

    private Stmt whileStmt() {
        consume(L_PAREN, "Expected '(' after while");
        Expr cond = expression();
        consume(R_PAREN, "Expected ')' after while condition");
        Stmt body = statement();
        return new While(cond, body);
    }

    private Stmt block() {
        List<Stmt> ss = new ArrayList<>();
        while (!check(R_BRACE) && !check(EOF)) {
            ss.add(statement());
        }
        consume(R_BRACE, "Expected '}' to close block");
        return new Block(ss);
    }

    // ---- Expressions (precedence & associativity) ----
    private Expr expression() {
        return or();
    }

    private Expr or() {
        Expr e = and();
        while (match(OR_OR))
            e = new Binary(e, "||", and());
        return e;
    }

    private Expr and() {
        Expr e = equality();
        while (match(AND_AND))
            e = new Binary(e, "&&", equality());
        return e;
    }

    private Expr equality() {
        Expr e = comparison();
        while (match(EQ, NE)) {
            Token op = previous();
            e = new Binary(e, op.lexeme, comparison());
        }
        return e;
    }

    private Expr comparison() {
        Expr e = term();
        while (match(LT, LE, GT, GE)) {
            Token op = previous();
            e = new Binary(e, op.lexeme, term());
        }
        return e;
    }

    private Expr term() {
        Expr e = factor();
        while (match(PLUS, MINUS)) {
            Token op = previous();
            e = new Binary(e, op.lexeme, factor());
        }
        return e;
    }

    private Expr factor() {
        Expr e = unary();
        while (match(STAR, SLASH)) {
            Token op = previous();
            e = new Binary(e, op.lexeme, unary());
        }
        return e;
    }

    private Expr unary() {
        if (match(MINUS))
            return new Unary("-", unary());
        return primary();
    }

    private Expr primary() {
        if (match(INT_LITERAL))
            return new IntLiteral(Integer.parseInt(previous().lexeme));
        if (match(IDENT))
            return new Var(previous().lexeme);
        if (match(L_PAREN)) {
            Expr e = expression();
            consume(R_PAREN, "Expected ')'");
            return new Grouping(e);
        }
        error(peek(), "Expected expression");
        return null; // unreachable
    }

    // ---- helpers ----
    private boolean match(TokenType... types) {
        for (TokenType t : types) {
            if (check(t)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType t) {
        return peek().type == t;
    }

    private Token advance() {
        if (!check(EOF))
            pos++;
        return previous();
    }

    private Token peek() {
        return ts.get(pos);
    }

    private Token previous() {
        return ts.get(pos - 1);
    }

    private Token consume(TokenType t, String msg) {
        if (check(t))
            return advance();
        error(peek(), msg);
        return null;
    }

    private void error(Token at, String msg) {
        throw new RuntimeException(
                "Parse error at " + at.line + ":" + at.col + " - " + msg + " (found " + at.type + ")");
    }
}
