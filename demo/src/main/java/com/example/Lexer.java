package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {
    private final String source;
    private final int length;
    private int pos = 0;
    private int line = 1;
    private int col = 1;

    private static final Map<String, TokenType> KEYWORDS = new HashMap<>();
    static {
        KEYWORDS.put("int", TokenType.KW_INT);
        KEYWORDS.put("if", TokenType.KW_IF);
        KEYWORDS.put("else", TokenType.KW_ELSE);
        KEYWORDS.put("while", TokenType.KW_WHILE);
        KEYWORDS.put("print", TokenType.KW_PRINT);
    }

    public Lexer(String source) {
        this.source = source;
        this.length = source.length();
    }

    public List<Token> scanTokens() {
        List<Token> tokens = new ArrayList<>();
        while (!isAtEnd()) {
            skipWhitespace();
            if (isAtEnd())
                break;
            int startLine = line;
            int startCol = col;
            char c = advance();

            switch (c) {
                case '+':
                    tokens.add(new Token(TokenType.PLUS, "+", startLine, startCol));
                    break;
                case '-':
                    tokens.add(new Token(TokenType.MINUS, "-", startLine, startCol));
                    break;
                case '*':
                    tokens.add(new Token(TokenType.STAR, "*", startLine, startCol));
                    break;
                case '/':
                    tokens.add(new Token(TokenType.SLASH, "/", startLine, startCol));
                    break;
                case '(':
                    tokens.add(new Token(TokenType.LPAREN, "(", startLine, startCol));
                    break;
                case ')':
                    tokens.add(new Token(TokenType.RPAREN, ")", startLine, startCol));
                    break;
                case '{':
                    tokens.add(new Token(TokenType.LBRACE, "{", startLine, startCol));
                    break;
                case '}':
                    tokens.add(new Token(TokenType.RBRACE, "}", startLine, startCol));
                    break;
                case ';':
                    tokens.add(new Token(TokenType.SEMICOLON, ";", startLine, startCol));
                    break;
                case '=':
                    if (match('='))
                        tokens.add(new Token(TokenType.EQUAL_EQUAL, "==", startLine, startCol));
                    else
                        tokens.add(new Token(TokenType.EQUAL, "=", startLine, startCol));
                    break;
                case '<':
                    if (match('='))
                        tokens.add(new Token(TokenType.LESS_EQUAL, "<=", startLine, startCol));
                    else
                        tokens.add(new Token(TokenType.LESS, "<", startLine, startCol));
                    break;
                case '>':
                    if (match('='))
                        tokens.add(new Token(TokenType.GREATER_EQUAL, ">=", startLine, startCol));
                    else
                        tokens.add(new Token(TokenType.GREATER, ">", startLine, startCol));
                    break;
                case '!':
                    if (match('='))
                        tokens.add(new Token(TokenType.BANG_EQUAL, "!=", startLine, startCol));
                    else
                        throw error("Unexpected '!'");
                    break;
                case '"':
                    tokens.add(readString(startLine, startCol));
                    break;
                default:
                    if (isDigit(c)) {
                        tokens.add(readNumber(startLine, startCol, c));
                    } else if (isAlpha(c)) {
                        tokens.add(readIdentOrKeyword(startLine, startCol, c));
                    } else {
                        throw error("Unexpected character: '" + c + "'");
                    }
                    break;
            }
        }
        tokens.add(new Token(TokenType.EOF, "", line, col));
        return tokens;
    }

    private boolean isAtEnd() {
        return pos >= length;
    }

    private char advance() {
        char c = source.charAt(pos++);
        if (c == '\n') {
            line++;
            col = 1;
        } else
            col++;
        return c;
    }

    private char peek() {
        if (isAtEnd())
            return '\0';
        return source.charAt(pos);
    }

    private boolean match(char expected) {
        if (isAtEnd())
            return false;
        if (source.charAt(pos) != expected)
            return false;
        pos++;
        col++;
        return true;
    }

    private void skipWhitespace() {
        while (!isAtEnd()) {
            char c = peek();
            if (c == ' ' || c == '\r' || c == '\t' || c == '\n') {
                advance();
            } else if (c == '/' && pos + 1 < length && source.charAt(pos + 1) == '/') {
                // line comment
                while (!isAtEnd() && peek() != '\n')
                    advance();
            } else {
                break;
            }
        }
    }

    private Token readNumber(int startLine, int startCol, char first) {
        StringBuilder sb = new StringBuilder();
        sb.append(first);
        while (!isAtEnd() && isDigit(peek())) {
            sb.append(advance());
        }
        return new Token(TokenType.INT_LITERAL, sb.toString(), startLine, startCol);
    }

    private Token readIdentOrKeyword(int startLine, int startCol, char first) {
        StringBuilder sb = new StringBuilder();
        sb.append(first);
        while (!isAtEnd() && isAlphaNumeric(peek())) {
            sb.append(advance());
        }
        String text = sb.toString();
        TokenType type = KEYWORDS.getOrDefault(text, TokenType.IDENT);
        return new Token(type, text, startLine, startCol);
    }

    private Token readString(int startLine, int startCol) {
        StringBuilder sb = new StringBuilder();
        while (!isAtEnd()) {
            char c = peek();
            if (c == '"') {
                advance(); // closing quote
                break;
            }
            if (c == '\\') {
                advance();
                if (isAtEnd())
                    throw error("Unterminated string literal");
                char e = peek();
                switch (e) {
                    case 'n':
                        sb.append('\n');
                        break;
                    case 't':
                        sb.append('\t');
                        break;
                    case 'r':
                        sb.append('\r');
                        break;
                    case '"':
                        sb.append('"');
                        break;
                    case '\\':
                        sb.append('\\');
                        break;
                    default:
                        sb.append(e);
                        break;
                }
                advance();
            } else {
                sb.append(advance());
            }
        }
        return new Token(TokenType.STRING_LITERAL, sb.toString(), startLine, startCol);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private RuntimeException error(String msg) {
        return new RuntimeException("Lexer error at " + line + ":" + col + " - " + msg);
    }
}
