package com.example;

public enum TokenType {
    // Single-character tokens
    PLUS, MINUS, STAR, SLASH,
    LPAREN, RPAREN,
    LBRACE, RBRACE,
    SEMICOLON,
    EQUAL,
    LESS, GREATER,

    // One or two character tokens
    EQUAL_EQUAL,
    BANG_EQUAL,
    LESS_EQUAL,
    GREATER_EQUAL,

    // Literals
    IDENT,
    INT_LITERAL,
    STRING_LITERAL,

    // Keywords
    KW_INT,
    KW_IF,
    KW_ELSE,
    KW_WHILE,
    KW_PRINT,

    EOF
}
