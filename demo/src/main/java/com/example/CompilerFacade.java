package com.example;

import java.util.*;

public class CompilerFacade {

    public static String compileAndRun(String source) {
        StringBuilder out = new StringBuilder();
        try {
            // 1) Lex
            Lexer lx = new Lexer(source);
            var tokens = lx.lex();

            // 2) Parse
            Parser ps = new Parser(tokens);
            var program = ps.parseProgram();

            // 3) Interpret (execute)
            Interpreter interp = new Interpreter(out);
            interp.run(program);

            if (out.length() == 0)
                out.append("(no output)\n");
            return "--- OK ---\n" + out;
        } catch (RuntimeException ex) {
            return "ERROR: " + ex.getMessage() + "\n";
        }
    }
}
