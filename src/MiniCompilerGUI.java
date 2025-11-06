import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.*;
import java.util.*;
import java.util.regex.*;

public class MiniCompilerGUI extends JFrame {

    private JTextArea inputArea, outputArea;
    private JButton compileButton, clearButton;

    public MiniCompilerGUI() {
        setTitle("Mini Java Compiler");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout
        setLayout(new BorderLayout(10, 10));

        // Input area
        inputArea = new JTextArea(10, 60);
        inputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        inputArea.setBorder(BorderFactory.createTitledBorder("Source Code"));

        // Output area
        outputArea = new JTextArea(10, 60);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        outputArea.setEditable(false);
        outputArea.setBorder(BorderFactory.createTitledBorder("Compiler Output"));

        // Buttons
        JPanel buttonPanel = new JPanel();
        compileButton = new JButton("Compile");
        clearButton = new JButton("Clear");
        buttonPanel.add(compileButton);
        buttonPanel.add(clearButton);

        add(new JScrollPane(inputArea), BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        // Button actions
        compileButton.addActionListener(e -> compile(inputArea.getText()));
        clearButton.addActionListener(e -> {
            inputArea.setText("");
            outputArea.setText("");
        });
    }

    // Compilation pipeline
    private void compile(String source) {
        StringBuilder output = new StringBuilder();
        output.append("--- Starting Compilation ---\n");

        List<String> tokens = lexicalAnalysis(source, output);
        boolean syntaxOk = syntaxCheck(source, output);
        if (syntaxOk) {
            semanticCheck(tokens, output);
        }

        output.append("--- Compilation Finished ---\n");
        outputArea.setText(output.toString());
    }

    // Lexical Analyzer
    private List<String> lexicalAnalysis(String source, StringBuilder output) {
        List<String> tokens = new ArrayList<>();
        Pattern pattern = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*|\\d+|[+\\-*/=(){};]");
        Matcher matcher = pattern.matcher(source);

        while (matcher.find()) {
            tokens.add(matcher.group());
        }

        if (!source.matches("[a-zA-Z0-9_+\\-*/=(){};\\s]*")) {
            output.append("Lexical Error: Illegal symbol detected!\n");
        } else {
            output.append("Tokens: " + tokens + "\n");
        }
        return tokens;
    }

    // Syntax Checker
    private boolean syntaxCheck(String statement, StringBuilder output) {
        String pattern = "^(int|float|double|String)\\s+[a-zA-Z_][a-zA-Z0-9_]*\\s*=\\s*[^;]+;$";
        boolean match = statement.trim().matches(pattern);

        if (match) {
            output.append("Syntax OK\n");
        } else {
            output.append("Syntax Error: Invalid declaration syntax.\n");
        }
        return match;
    }

    // Semantic Analyzer
    private void semanticCheck(List<String> tokens, StringBuilder output) {
        Set<String> declaredVars = new HashSet<>();
        List<String> varsInThisStatement = new ArrayList<>();
        boolean inDeclaration = false;

        // A Set of known keywords to ignore
        Set<String> keywords = new HashSet<>(Arrays.asList("int", "float", "double", "String"));

        for (String token : tokens) {
            // Start of a declaration
            if (keywords.contains(token)) {
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
                        output.append("Semantic Error: Variable '").append(token).append("' used in its own initializer.\n");
                    }
                    // Check for other undeclared variables (e.g., "int x = y;")
                    else if (!declaredVars.contains(token)) {
                        output.append("Semantic Error: Variable '").append(token).append("' used before declaration.\n");
                    }
                }
            }
            // This is a normal variable use (not in a declaration) (e.g., "y = 5;")
            else if (Character.isLetter(token.charAt(0)) && !declaredVars.contains(token) && !keywords.contains(token)) {
                output.append("Semantic Error: Variable '").append(token).append("' used before declaration.\n");
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MiniCompilerGUI().setVisible(true);
        });
    }
}