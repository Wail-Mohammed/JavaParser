//import javax.tools.*;
//import java.net.URI;
//import java.util.*;
//
//public class Parser {
//    static class StringJavaFileObject extends SimpleJavaFileObject {
//        final String code;
//
//        StringJavaFileObject(String className, String code) {
//            super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
//            this.code = code;
//        }
//
//        @Override
//        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
//            return code;
//        }
//    }
//
//    public static void main(String[] args) {
//        String code = """
//                package demo;
//                import java.util.*;
//                public class Hello {
//                    public static void main(String[] args) {
//                        int x = 1;
//                        System.out.println(x + y); // 'y' is undeclared -> semantic error
//                    }
//                }
//                """;
//
//        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//        if (compiler == null) {
//            System.err.println("No system Java compiler found (are you on a JRE?). Use a JDK.");
//            return;
//        }
//
//        DiagnosticCollector<JavaFileObject> diags = new DiagnosticCollector<>();
//        StandardJavaFileManager fm = compiler.getStandardFileManager(diags, null, null);
//
//        // -proc:none avoids annotation processing; -Xlint can surface more issues
//        List<String> options = Arrays.asList("-proc:none", "-Xlint");
//        JavaFileObject file = new StringJavaFileObject("demo.Hello", code);
//
//        JavaCompiler.CompilationTask task = compiler.getTask(null, fm, diags, options, null,
//                Collections.singletonList(file));
//
//        Boolean success = task.call(); // parse + analyze + (would) compile
//
//        System.out.println(success ? "Parsed & analyzed OK" : "Errors:");
//        for (Diagnostic<?> d : diags.getDiagnostics()) {
//            System.out.printf("[%s] line %d:%d %s%n",
//                    d.getKind(), d.getLineNumber(), d.getColumnNumber(), d.getMessage(null));
//        }
//    }
//}





import java.util.*;





import java.util.regex.*;

public class Parser {

    // Lexical Analyzer: Split source into tokens and check for invalid characters
    public static List<String> lexicalAnalysis(String source) {
        List<String> tokens = new ArrayList<>();
        Pattern pattern = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*|\\d+|[+\\-*/=(){};]");
        Matcher matcher = pattern.matcher(source);

        while (matcher.find()) {
            tokens.add(matcher.group());
        }

        if (!source.matches("[a-zA-Z0-9_+\\-*/=(){};\\s]*")) {
            System.out.println("Lexical Error: Illegal symbol detected!");
        } else {
            System.out.println("Tokens: " + tokens);
        }

        return tokens;
    }

    // Syntax Checker: match declarations like "int x = 5;"
    public static boolean syntaxCheck(String statement) {
        // Matches: type identifier = value;
        String pattern = "^(int|float|double|String)\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*=\\s*[^;]+;$";
        // add another set of patterns for JAVA keywords
        boolean match = statement.trim().matches(pattern);

        if (match) {
            System.out.println("Syntax OK");
        } else {
            System.out.println("Syntax Error: Invalid declaration syntax.");
        }
        return match;
    }

    // Semantic Analyzer: Detect undeclared variable usage
//    public static void semanticCheck(List<String> tokens) {
//        Set<String> declaredVars = new HashSet<>();
//        for (int i = 0; i < tokens.size(); i++) {
//            if (tokens.get(i).equals("int") && i + 1 < tokens.size()) {
//                declaredVars.add(tokens.get(i + 1));
//            }
//        }
//
//        for (String token : tokens) {
//            if (Character.isLetter(token.charAt(0)) && !declaredVars.contains(token)
//                    && !Arrays.asList("int", "float", "double", "String").contains(token)) {
//                System.out.println("Semantic Warning: Variable '" + token + "' used before declaration.");
//            }
//        }
//    }
    public static void semanticCheck(List<String> tokens) {
        Set<String> declaredVars = new HashSet<>();
        List<String> varsInThisStatement = new ArrayList<>();
        boolean inDeclaration = false;

        for (String token : tokens) {
            // Start of a declaration
            if (token.equals("int") || token.equals("float") || token.equals("double") || token.equals("String")) {
                inDeclaration = true;
                continue; // Skip to the next token
            }

            if (inDeclaration) {
                // This token is the variable being declared (e.g., 'x' in "int x = ...")
                if (varsInThisStatement.isEmpty() && Character.isLetter(token.charAt(0))) {
                    varsInThisStatement.add(token);
                }
                // This token is on the right-hand side of the '='
                else if (Character.isLetter(token.charAt(0))) {
                    // Check for self-reference (e.g., "int x = x;")
                    if (varsInThisStatement.contains(token)) {
                        System.out.println("Semantic Error: Variable '" + token + "' used in its own initializer.");
                    }
                    // Check for other undeclared variables (e.g., "int x = y;")
                    else if (!declaredVars.contains(token)) {
                        System.out.println("Semantic Error: Variable '" + token + "' used before declaration.");
                    }
                }
            }
            // This is a normal variable use (not in a declaration) (e.g., "y = 5;")
            else if (Character.isLetter(token.charAt(0)) && !declaredVars.contains(token)) {
                System.out.println("Semantic Error: Variable '" + token + "' used before declaration.");
            }

            // At the end of the statement, add the new variable to the "safe" list
            if (token.equals(";")) {
                if (inDeclaration) {
                    declaredVars.addAll(varsInThisStatement); // Now 'x' is officially declared
                    varsInThisStatement.clear();
                    inDeclaration = false;
                }
            }
        }
    }

    public static void compile(String source) {
        System.out.println("\n--- Starting Compilation ---");
        List<String> tokens = lexicalAnalysis(source);
        boolean syntaxOk = syntaxCheck(source);
        if (syntaxOk)
            semanticCheck(tokens);
        System.out.println("--- Compilation Finished ---");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter source code to compile:");
        String input = sc.nextLine();

        compile(input);
        sc.close();
    }
}