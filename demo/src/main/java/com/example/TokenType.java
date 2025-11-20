package com.example;

public enum TokenType {
    // Single-char
    L_PAREN, R_PAREN, L_BRACE, R_BRACE, SEMI, COMMA,
    PLUS, MINUS, STAR, SLASH, ASSIGN, // =

    // Two-char / comparators / logical
    LT, LE, GT, GE, EQ, NE, AND_AND, OR_OR,

    // Literals & identifiers
    IDENT, INT_LITERAL,

    // Keywords
    KW_INT, KW_IF, KW_ELSE, KW_WHILE, KW_PRINT,

    EOF
}
