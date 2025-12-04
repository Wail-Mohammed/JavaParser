package com.example;

import java.util.List;

import com.example.Ast.Stmt;

public class CompilerFacade {

    public static String compileAndRun(String source) {
        StringBuilder log = new StringBuilder();

        try {
            // 1) Lexing
            Lexer lexer = new Lexer(source);
            List<Token> tokens = lexer.scanTokens();

            // Print tokens first
            log.append("=== Tokens ===\n");
            for (Token t : tokens) {
                if (t.type == TokenType.EOF)
                    continue; // optional: hide EOF
                log.append(t).append('\n');
            }

            // 2) Parsing
            Parser parser = new Parser(tokens);
            List<Stmt> program = parser.parseProgram();

            // 3) Interpretation (program output)
            log.append("\n=== Program Output ===\n");
            Interpreter interpreter = new Interpreter(log);
            interpreter.run(program);

        } catch (Exception ex) {
            // Make sure App sees this as an error (it checks result.startsWith("ERROR:"))
            log.setLength(0); // clear partial output
            log.append("ERROR: ").append(ex.getMessage()).append('\n');
        }

        return log.toString();
    }
}
