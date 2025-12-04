package com.example;

import java.util.ArrayList;
import java.util.List;

import com.example.Ast.*;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Stmt> parseProgram() {
        List<Stmt> stmts = new ArrayList<>();
        while (!isAtEnd()) {
            stmts.add(statement());
        }
        return stmts;
    }

    private Stmt statement() {
        if (match(TokenType.KW_INT))
            return varDecl();
        if (match(TokenType.KW_PRINT))
            return printStmt();
        if (match(TokenType.KW_IF))
            return ifStmt();
        if (match(TokenType.KW_WHILE))
            return whileStmt();
        if (match(TokenType.LBRACE))
            return blockStmt();

        // assignment: IDENT '=' expr ';'
        if (check(TokenType.IDENT) && checkNext(TokenType.EQUAL)) {
            Token name = advance();
            consume(TokenType.EQUAL, "Expected '=' after identifier");
            Expr value = expression();
            consume(TokenType.SEMICOLON, "Expected ';' after assignment");
            return new Assign(name.lexeme, value);
        }

        throw error(peek(), "Unsupported statement (expected int/print/if/while/block/assignment)");
    }

    private Stmt varDecl() {
        Token name = consume(TokenType.IDENT, "Expected variable name after 'int'");
        Expr init = null;
        if (match(TokenType.EQUAL)) {
            init = expression();
        }
        consume(TokenType.SEMICOLON, "Expected ';' after declaration");
        return new VarDecl(name.lexeme, init);
    }

    private Stmt printStmt() {
        consume(TokenType.LPAREN, "Expected '(' after print");
        Expr value = expression();
        consume(TokenType.RPAREN, "Expected ')' after value");
        consume(TokenType.SEMICOLON, "Expected ';' after print");
        return new Print(value);
    }

    private Stmt ifStmt() {
        consume(TokenType.LPAREN, "Expected '(' after if");
        Expr cond = expression();
        consume(TokenType.RPAREN, "Expected ')' after condition");
        Stmt thenBranch = statement();
        Stmt elseBranch = null;
        if (match(TokenType.KW_ELSE)) {
            elseBranch = statement();
        }
        return new If(cond, thenBranch, elseBranch);
    }

    private Stmt whileStmt() {
        consume(TokenType.LPAREN, "Expected '(' after while");
        Expr cond = expression();
        consume(TokenType.RPAREN, "Expected ')' after condition");
        Stmt body = statement();
        return new While(cond, body);
    }

    private Stmt blockStmt() {
        List<Stmt> stmts = new ArrayList<>();
        while (!check(TokenType.RBRACE) && !isAtEnd()) {
            stmts.add(statement());
        }
        consume(TokenType.RBRACE, "Expected '}' after block");
        return new Block(stmts);
    }

    // ===== expressions =====

    private Expr expression() {
        return equality();
    }

    private Expr equality() {
        Expr expr = comparison();
        while (match(TokenType.EQUAL_EQUAL, TokenType.BANG_EQUAL)) {
            Token op = previous();
            Expr right = comparison();
            expr = new Binary(expr, op.lexeme, right);
        }
        return expr;
    }

    private Expr comparison() {
        Expr expr = addition();
        while (match(TokenType.LESS, TokenType.LESS_EQUAL,
                TokenType.GREATER, TokenType.GREATER_EQUAL)) {
            Token op = previous();
            Expr right = addition();
            expr = new Binary(expr, op.lexeme, right);
        }
        return expr;
    }

    private Expr addition() {
        Expr expr = multiplication();
        while (match(TokenType.PLUS, TokenType.MINUS)) {
            Token op = previous();
            Expr right = multiplication();
            expr = new Binary(expr, op.lexeme, right);
        }
        return expr;
    }

    private Expr multiplication() {
        Expr expr = unary();
        while (match(TokenType.STAR, TokenType.SLASH)) {
            Token op = previous();
            Expr right = unary();
            expr = new Binary(expr, op.lexeme, right);
        }
        return expr;
    }

    private Expr unary() {
        if (match(TokenType.MINUS)) {
            Token op = previous();
            Expr right = unary();
            return new Unary(op.lexeme, right);
        }
        return primary();
    }

    private Expr primary() {
        if (match(TokenType.INT_LITERAL))
            return new IntLiteral(Integer.parseInt(previous().lexeme));
        if (match(TokenType.STRING_LITERAL))
            return new StringLiteral(previous().lexeme);
        if (match(TokenType.IDENT))
            return new Var(previous().lexeme);
        if (match(TokenType.LPAREN)) {
            Expr expr = expression();
            consume(TokenType.RPAREN, "Expected ')' after expression");
            return new Grouping(expr);
        }
        throw error(peek(), "Expected expression");
    }

    // ===== helpers =====

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd())
            return false;
        return peek().type == type;
    }

    private boolean checkNext(TokenType type) {
        if (current + 1 >= tokens.size())
            return false;
        return tokens.get(current + 1).type == type;
    }

    private Token advance() {
        if (!isAtEnd())
            current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private Token consume(TokenType type, String message) {
        if (check(type))
            return advance();
        throw error(peek(), message);
    }

    private RuntimeException error(Token token, String message) {
        return new RuntimeException("Parser error at " + token.line + ":" + token.column +
                " near '" + token.lexeme + "': " + message);
    }
}
