package com.example;

import java.util.*;

public class Lexer {
    private final String src;
    private int i = 0, line = 1, col = 1;
    private final List<Token> tokens = new ArrayList<>();

    public Lexer(String src) {
        this.src = src;
    }

    public List<Token> lex() {
        while (!eof()) {
            char c = peek();
            if (Character.isWhitespace(c)) {
                consumeWhitespace();
                continue;
            }
            int startCol = col;

            // identifiers / keywords
            if (Character.isLetter(c) || c == '_') {
                String id = readWhile(ch -> Character.isLetterOrDigit(ch) || ch == '_');
                tokens.add(new Token(keywordOrIdent(id), id, line, startCol));
                continue;
            }

            // numbers
            if (Character.isDigit(c)) {
                String num = readWhile(Character::isDigit);
                tokens.add(new Token(TokenType.INT_LITERAL, num, line, startCol));
                continue;
            }

            // operators / punct
            switch (c) {
                case '(':
                    add(TokenType.L_PAREN, "(");
                    break;
                case ')':
                    add(TokenType.R_PAREN, ")");
                    break;
                case '{':
                    add(TokenType.L_BRACE, "{");
                    break;
                case '}':
                    add(TokenType.R_BRACE, "}");
                    break;
                case ';':
                    add(TokenType.SEMI, ";");
                    break;
                case ',':
                    add(TokenType.COMMA, ",");
                    break;
                case '+':
                    add(TokenType.PLUS, "+");
                    break;
                case '-':
                    add(TokenType.MINUS, "-");
                    break;
                case '*':
                    add(TokenType.STAR, "*");
                    break;
                case '/':
                    // support // line comments and /* */ block comments
                    if (peek2() == '/') {
                        advance();
                        advance();
                        readLineComment();
                        continue;
                    }
                    if (peek2() == '*') {
                        advance();
                        advance();
                        readBlockComment();
                        continue;
                    }
                    add(TokenType.SLASH, "/");
                    break;
                case '=':
                    if (peek2() == '=') {
                        add2(TokenType.EQ, "==");
                    } else {
                        add(TokenType.ASSIGN, "=");
                    }
                    break;
                case '!':
                    if (peek2() == '=') {
                        add2(TokenType.NE, "!=");
                    } else
                        error("Unexpected '!'");
                    break;
                case '<':
                    if (peek2() == '=') {
                        add2(TokenType.LE, "<=");
                    } else {
                        add(TokenType.LT, "<");
                    }
                    break;
                case '>':
                    if (peek2() == '=') {
                        add2(TokenType.GE, ">=");
                    } else {
                        add(TokenType.GT, ">");
                    }
                    break;
                case '&':
                    if (peek2() == '&') {
                        add2(TokenType.AND_AND, "&&");
                    } else
                        error("Use '&&'");
                    break;
                case '|':
                    if (peek2() == '|') {
                        add2(TokenType.OR_OR, "||");
                    } else
                        error("Use '||'");
                    break;
                default:
                    error("Illegal character: '" + c + "'");
            }
        }
        tokens.add(new Token(TokenType.EOF, "", line, col));
        return tokens;
    }

    private TokenType keywordOrIdent(String s) {
        return switch (s) {
            case "int" -> TokenType.KW_INT;
            case "if" -> TokenType.KW_IF;
            case "else" -> TokenType.KW_ELSE;
            case "while" -> TokenType.KW_WHILE;
            case "print" -> TokenType.KW_PRINT;
            default -> TokenType.IDENT;
        };
    }

    private void readLineComment() {
        while (!eof() && peek() != '\n')
            advance();
    }

    private void readBlockComment() {
        while (!eof()) {
            if (peek() == '*' && peek2() == '/') {
                advance();
                advance();
                return;
            }
            advance();
        }
        error("Unterminated block comment");
    }

    // helpers
    private boolean eof() {
        return i >= src.length();
    }

    private char peek() {
        return src.charAt(i);
    }

    private char peek2() {
        return (i + 1 < src.length()) ? src.charAt(i + 1) : '\0';
    }

    private void advance() {
        if (eof())
            return;
        char c = src.charAt(i++);
        if (c == '\n') {
            line++;
            col = 1;
        } else {
            col++;
        }
    }

    private String readWhile(java.util.function.Predicate<Character> p) {
        int start = i, startCol = col;
        while (!eof() && p.test(peek()))
            advance();
        return src.substring(start, i);
    }

    private void consumeWhitespace() {
        while (!eof() && Character.isWhitespace(peek()))
            advance();
    }

    private void add(TokenType t, String lex) {
        tokens.add(new Token(t, lex, line, col));
        advance();
    }

    private void add2(TokenType t, String lex2) {
        tokens.add(new Token(t, lex2, line, col));
        advance();
        advance();
    }

    private void error(String msg) {
        throw new RuntimeException("Lex error at " + line + ":" + col + " - " + msg);
    }
}
