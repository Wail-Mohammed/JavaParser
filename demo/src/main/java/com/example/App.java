package com.example;

//Steps to compile: from root directory where pom.xml is present
//1. mvn clean compile
//2. java -cp target/classes com.example.App

// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.ActionEvent;

// public class App extends JFrame {
//     private final JTextArea inputArea = new JTextArea(18, 70);
//     private final JTextArea outputArea = new JTextArea(14, 70);
//     private final JLabel statusLabel = new JLabel("Ready.");

//     public App() {

//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//         // Monospaced font for "code" look
//         inputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
//         outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
//         outputArea.setEditable(false);

//         // --- Top: Source code panel ---
//         JPanel sourcePanel = new JPanel(new BorderLayout());
//         sourcePanel.setBorder(BorderFactory.createTitledBorder("Source Code (Mini Java-like)"));
//         sourcePanel.add(new JScrollPane(inputArea), BorderLayout.CENTER);

//         // --- Bottom: Output panel ---
//         JPanel outputPanel = new JPanel(new BorderLayout());
//         outputPanel.setBorder(BorderFactory.createTitledBorder("Compiler / Runtime Output"));
//         outputPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

//         // --- Center: split pane between source and output ---
//         JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sourcePanel, outputPanel);
//         split.setResizeWeight(0.55); // more space to source
//         split.setContinuousLayout(true);

//         // --- Buttons toolbar ---
//         JToolBar toolbar = new JToolBar();
//         toolbar.setFloatable(false);

//         JButton compileRunBtn = new JButton("Compile & Run");
//         JButton demoBtn = new JButton("Run Demo Programs");
//         JButton helpBtn = new JButton("Language Help");
//         JButton clearBtn = new JButton("Clear");

//         toolbar.add(compileRunBtn);
//         toolbar.add(demoBtn);
//         toolbar.add(helpBtn);
//         toolbar.addSeparator();
//         toolbar.add(clearBtn);

//         // --- Status bar ---
//         JPanel statusBar = new JPanel(new BorderLayout());
//         statusBar.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
//         statusBar.add(statusLabel, BorderLayout.WEST);

//         // --- Menu bar ---
//         setJMenuBar(createMenuBar());

//         // --- Layout frame ---
//         add(toolbar, BorderLayout.NORTH);
//         add(split, BorderLayout.CENTER);
//         add(statusBar, BorderLayout.SOUTH);

//         // --- Button actions ---

//         compileRunBtn.addActionListener(e -> {
//             String src = inputArea.getText().trim();
//             if (src.isEmpty()) {
//                 setStatus("No source code to compile.");
//                 outputArea.setText("Please type a program in the Source Code area.\n");
//                 return;
//             }
//             String result = CompilerFacade.compileAndRun(src);
//             outputArea.setText(result);
//             if (result.startsWith("ERROR:")) {
//                 setStatus("Compilation or runtime error.");
//             } else {
//                 setStatus("Compiled and ran successfully.");
//             }
//         });

//         demoBtn.addActionListener(e -> runDemoPrograms());

//         clearBtn.addActionListener(e -> {
//             inputArea.setText("");
//             outputArea.setText("");
//             setStatus("Cleared source and output.");
//         });

//         helpBtn.addActionListener(this::showLanguageHelp);

//         // --- Initial sample program ---
//         inputArea.setText(
//                 """
//                         // Sample program: sum of 0..4 and a simple if
//                         int x = 0;
//                         int sum = 0;

//                         while (x < 5) {
//                             sum = sum + x;
//                             x = x + 1;
//                         }

//                         if (sum >= 10) {
//                             print(sum);      // prints 10
//                         } else {
//                             print(0);
//                         }

//                         {
//                             int y = 3;
//                             if (y > 1 && y < 5) {
//                                 print(y * 2); // prints 6
//                             }
//                         }
//                         """);

//         setSize(900, 750);
//         setLocationRelativeTo(null); // center on screen
//     }

//     private JMenuBar createMenuBar() {
//         JMenuBar bar = new JMenuBar();

//         JMenu fileMenu = new JMenu("File");
//         JMenuItem exitItem = new JMenuItem("Exit");
//         exitItem.addActionListener(e -> System.exit(0));
//         fileMenu.add(exitItem);

//         JMenu helpMenu = new JMenu("Help");
//         JMenuItem langHelp = new JMenuItem("Language Reference");
//         JMenuItem about = new JMenuItem("About");

//         langHelp.addActionListener(this::showLanguageHelp);
//         about.addActionListener(e -> JOptionPane.showMessageDialog(
//                 this,
//                 """
//                         Mini Java-like Compiler

//                         This is a small educational compiler/interpreter that:
//                         - Lexes (tokenizes) your input
//                         - Parses it into a syntax tree
//                         - Emits simple pseudo-bytecode
//                         - Interprets the program and shows the output

//                         It's designed to demonstrate basic compiler concepts
//                         like grammars, parse trees, and control structures.
//                         """,
//                 "About",
//                 JOptionPane.INFORMATION_MESSAGE));

//         helpMenu.add(langHelp);
//         helpMenu.add(about);

//         bar.add(fileMenu);
//         bar.add(helpMenu);
//         return bar;
//     }

//     private void showLanguageHelp(ActionEvent e) {
//         String msg = """
//                 Mini Java-like Language Reference
//                 =================================

//                 Types:
//                   - int   (integers only)

//                 Statements:
//                   - int x;
//                   - int x = 5;
//                   - x = x + 1;
//                   - print(x);
//                   - if (condition) statement else statement;
//                   - while (condition) statement;
//                   - { statement* }   // block, introduces a new scope

//                 Expressions:
//                   - integer literals: 0, 1, 42
//                   - variables: x, y
//                   - arithmetic: +, -, *, /
//                   - comparison: <, <=, >, >=, ==, !=
//                   - logical: &&, ||

//                 Examples:
//                   int x = 0;
//                   while (x < 3) {
//                       print(x);
//                       x = x + 1;
//                   }

//                   int n = 5;
//                   if (n > 3) {
//                       print(n);
//                   } else {
//                       print(0);
//                   }

//                 Notes:
//                   - true is represented as 1, false as 0.
//                   - Division is integer division.
//                   - Using 'print(expr);' sends the value to the output panel.
//                 """;

//         JTextArea text = new JTextArea(msg);
//         text.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
//         text.setEditable(false);
//         JScrollPane scroll = new JScrollPane(text);
//         scroll.setPreferredSize(new Dimension(650, 400));

//         JOptionPane.showMessageDialog(
//                 this,
//                 scroll,
//                 "Language Reference",
//                 JOptionPane.INFORMATION_MESSAGE);
//     }

//     private void runDemoPrograms() {
//         String[] demos = {
//                 """
//                         // Demo 1: while loop
//                         int x = 0;
//                         while (x < 3) {
//                             print(x);
//                             x = x + 1;
//                         }
//                         """,
//                 """
//                         // Demo 2: if / else
//                         int n = 5;
//                         if (n > 3) {
//                             print(n);
//                         } else {
//                             print(0);
//                         }
//                         """,
//                 """
//                         // Demo 3: logical && and range test
//                         int y = 3;
//                         if (y > 1 && y < 5) {
//                             print(y * 2);
//                         }
//                         """
//         };

//         StringBuilder out = new StringBuilder();
//         for (int i = 0; i < demos.length; i++) {
//             out.append("===== Demo ").append(i + 1).append(" =====\n");
//             out.append(demos[i]).append("\n");
//             out.append(CompilerFacade.compileAndRun(demos[i])).append("\n");
//         }
//         outputArea.setText(out.toString());
//         setStatus("Ran demo programs.");
//     }

//     private void setStatus(String msg) {
//         statusLabel.setText(msg);
//     }

//     public static void main(String[] args) {
//         SwingUtilities.invokeLater(() -> new App().setVisible(true));
//     }
// }

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class App extends JFrame {
    private final JTextArea inputArea = new JTextArea(18, 70);

    // Output Tabs
    private final JTextArea consoleArea = new JTextArea(14, 70);
    private final JTextArea tokenArea = new JTextArea(14, 70);
    private final JTextArea bytecodeArea = new JTextArea(14, 70);
    private final JTextArea semanticArea = new JTextArea(14, 70);
    private final JTree astTree = new JTree(new DefaultMutableTreeNode("Root"));

    private final JLabel statusLabel = new JLabel("Ready");
    private int fontSize = 14;

    // --- Colors ---
    private static final Color BG_MAIN = new Color(240, 242, 245);
    private static final Color BG_PANEL = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(33, 37, 41);
    private static final Color BORDER_COLOR = new Color(220, 224, 229);
    private static final Color SIDEBAR_BG = new Color(235, 236, 238);
    private static final Color SELECTION_BG = new Color(220, 230, 250);

    private static final Color BTN_BLUE = new Color(59, 130, 246);
    private static final Color BTN_GREEN = new Color(34, 197, 94);
    private static final Color BTN_PURPLE = new Color(168, 85, 247);
    private static final Color BTN_RED = new Color(239, 68, 68);
    private static final Color BTN_DARK = new Color(55, 65, 81);
    private static final Color BTN_GREY = new Color(108, 117, 125);

    public App() {
        super("Mini Java-like Compiler - Educational Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_MAIN);
        setContentPane(mainPanel);

        updateFont();
        inputArea.setTabSize(4);
        consoleArea.setEditable(false);
        tokenArea.setEditable(false);
        bytecodeArea.setEditable(false);
        semanticArea.setEditable(false);

        // --- Layout ---
        JPanel sourcePanel = createTitledPanel("Source Code", new JScrollPane(inputArea));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("🖥️ Console", new JScrollPane(consoleArea));
        tabbedPane.addTab("🔍 Tokens", new JScrollPane(tokenArea));
        tabbedPane.addTab("🌳 AST", new JScrollPane(astTree));
        tabbedPane.addTab("✅ Semantics", new JScrollPane(semanticArea));
        tabbedPane.addTab("⚙️ Bytecode", new JScrollPane(bytecodeArea));

        JPanel outputContainer = createTitledPanel("Compiler Output", tabbedPane);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sourcePanel, outputContainer);
        split.setResizeWeight(0.55);
        split.setDividerSize(5);
        split.setBorder(null);

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(BG_MAIN);

        toolbar.add(createButton("Open", e -> openFile(), BTN_BLUE));
        toolbar.add(createButton("Save", e -> saveFile(), BTN_BLUE));
        toolbar.add(Box.createHorizontalStrut(15));
        toolbar.add(createButton("▶ Compile & Run", e -> runCompiler(), BTN_GREEN));
        toolbar.add(createButton("⚡ Live Demo", e -> runDemos(), BTN_PURPLE));
        toolbar.add(createButton("🗑 Clear", e -> clearWorkspace(), BTN_DARK));
        toolbar.add(Box.createHorizontalStrut(15));
        toolbar.add(createButton("📚 Learning Guide", e -> showLearningGuide(), BTN_RED));
        toolbar.add(Box.createHorizontalGlue());
        toolbar.add(createButton("A+", e -> changeFontSize(2), Color.BLACK));
        toolbar.add(createButton("A-", e -> changeFontSize(-2), Color.BLACK));

        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(Color.WHITE);
        statusBar.setBorder(new EmptyBorder(5, 10, 5, 10));
        statusBar.add(statusLabel, BorderLayout.WEST);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));

        mainPanel.add(toolbar, BorderLayout.NORTH);
        mainPanel.add(split, BorderLayout.CENTER);
        mainPanel.add(statusBar, BorderLayout.SOUTH);

        // Initial Code (Showcasing String Variables)
        inputArea.setText("""
                string name = "Student";
                int score = 95;

                print("Report for: " + name);
                print("Final Score: " + score);
                """);

        setSize(1300, 900);
        setLocationRelativeTo(null);
    }

    // --- Core Logic ---

    private void runCompiler() {
        String src = inputArea.getText();
        if (src.trim().isEmpty())
            return;
        setStatus("Compiling...", Color.BLACK);

        SwingUtilities.invokeLater(() -> {
            try {
                // 1. Lexer
                Lexer lexer = new Lexer(src);
                List<Token> tokens = lexer.scanTokens();
                StringBuilder tokenSb = new StringBuilder();
                for (Token t : tokens)
                    tokenSb.append(t).append("\n");
                tokenArea.setText(tokenSb.toString());

                // 2. Parser
                Parser parser = new Parser(tokens);
                List<Ast.Stmt> program = parser.parseProgram();
                updateAstTree(program);

                // 3. Semantics
                SemanticAnalyzer semantics = new SemanticAnalyzer();
                List<String> semanticErrors = semantics.analyze(program);
                if (!semanticErrors.isEmpty()) {
                    StringBuilder sb = new StringBuilder("SEMANTIC ERRORS:\n");
                    for (String err : semanticErrors)
                        sb.append("❌ ").append(err).append("\n");
                    semanticArea.setText(sb.toString());
                    semanticArea.setForeground(Color.RED);
                    setStatus("Semantic Errors", Color.RED);
                    consoleArea.setText("Compilation stopped.");
                    return;
                } else {
                    semanticArea.setText("✅ Checks Passed.");
                    semanticArea.setForeground(new Color(0, 150, 0));
                }

                // 4. Bytecode
                BytecodeEmitter emitter = new BytecodeEmitter();
                String bytecode = emitter.generate(program);
                bytecodeArea.setText(bytecode);

                // 5. Interpreter
                StringBuilder consoleOutput = new StringBuilder();
                Interpreter interpreter = new Interpreter(consoleOutput);
                interpreter.run(program);
                consoleArea.setText(consoleOutput.toString());

                setStatus("Success", new Color(0, 150, 0));

            } catch (Exception ex) {
                consoleArea.setText("RUNTIME ERROR:\n" + ex.getMessage());
                setStatus("Error", Color.RED);
            }
        });
    }

    private void runDemos() {
        // Updated Demo to include String Variables
        inputArea.setText("""
                int age = 20;
                string status = "Unknown";

                if (age >= 18) {
                    status = "Adult";
                } else {
                    status = "Minor";
                }

                print("Age: " + age);
                print("Status: " + status);
                """);
        runCompiler();
    }

    // --- Learning Guide ---

    private void showLearningGuide() {
        JDialog d = new JDialog(this, "Interactive Learning Guide", true);
        d.setSize(1100, 750);
        d.setLocationRelativeTo(this);

        JSplitPane split = new JSplitPane();
        split.setDividerLocation(220);
        split.setDividerSize(1);
        split.setBorder(null);

        CardLayout cardLayout = new CardLayout();
        JPanel contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        String[] topics = { "1. Architecture", "2. Quiz: Variables", "3. Quiz: Logic", "4. Debugging",
                "5. Challenges" };

        DefaultListModel<String> model = new DefaultListModel<>();
        for (String t : topics)
            model.addElement(t);
        JList<String> navList = new JList<>(model);
        navList.setSelectedIndex(0);
        navList.setBackground(SIDEBAR_BG);
        navList.setForeground(TEXT_COLOR);
        navList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        navList.setFixedCellHeight(45);

        navList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                l.setBorder(new EmptyBorder(0, 15, 0, 0));
                if (isSelected) {
                    l.setBackground(SELECTION_BG);
                    l.setForeground(BTN_BLUE);
                    l.setFont(l.getFont().deriveFont(Font.BOLD));
                } else {
                    l.setBackground(SIDEBAR_BG);
                    l.setForeground(TEXT_COLOR);
                    l.setFont(l.getFont().deriveFont(Font.PLAIN));
                }
                return l;
            }
        });

        contentPanel.add(createArchitectureCard(), topics[0]);
        contentPanel.add(createMultiQuizCard(getVarQuestions()), topics[1]);
        contentPanel.add(createMultiQuizCard(getLogicQuestions()), topics[2]);
        contentPanel.add(createDebugLabCard(), topics[3]);
        contentPanel.add(createChallengeCard(), topics[4]);

        navList.addListSelectionListener(ev -> {
            String selected = navList.getSelectedValue();
            if (selected != null)
                cardLayout.show(contentPanel, selected);
        });

        split.setLeftComponent(new JScrollPane(navList) {
            {
                setBorder(null);
            }
        });
        split.setRightComponent(contentPanel);
        d.add(split);
        d.setVisible(true);
    }

    private JPanel createArchitectureCard() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JTextArea t = new JTextArea("""
                            COMPILER ARCHITECTURE & TOOLS

                            1. LEXER (Scanner)
                               • Output: Stream of 'Tokens'.

                            2. PARSER
                               • Output: Abstract Syntax Tree (AST).


                [Image of abstract syntax tree diagram]


                            3. SEMANTIC ANALYZER
                               • Checks for errors (e.g., using undefined variables).

                            4. INTERPRETER
                               • Executes the code logic line-by-line.

                            =========================================
                            DATA TYPES
                            =========================================
                            • int: Whole numbers (1, 2, 100)
                            • string: Text ("Hello", "Java")
                            • bool: True/False logic
                            """);
        t.setFont(new Font("Monospaced", Font.PLAIN, 14));
        t.setEditable(false);
        t.setOpaque(false);
        p.add(new JScrollPane(t), BorderLayout.CENTER);
        return p;
    }

    private JPanel createDebugLabCard() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel header = new JLabel("🛠️ DEBUGGING LAB");
        header.setFont(new Font("SansSerif", Font.BOLD, 18));

        JTextArea t = new JTextArea("Task: Fix the error. String variables must be declared with 'string'.");
        t.setEditable(false);
        t.setOpaque(false);

        JTextArea code = new JTextArea("name = \"Alice\"; // Error: Missing type");
        code.setFont(new Font("Monospaced", Font.PLAIN, 14));
        code.setBorder(new LineBorder(Color.GRAY));

        JButton check = createButton("Check Fix", e -> {
            if (code.getText().contains("string name"))
                JOptionPane.showMessageDialog(p, "✅ Correct!");
            else
                JOptionPane.showMessageDialog(p, "❌ Missing 'string' keyword.");
        }, BTN_BLUE);

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(t, BorderLayout.NORTH);
        center.add(code, BorderLayout.CENTER);
        p.add(header, BorderLayout.NORTH);
        p.add(center, BorderLayout.CENTER);
        p.add(check, BorderLayout.SOUTH);
        return p;
    }

    private JPanel createChallengeCard() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel header = new JLabel("🏆 CODE CHALLENGE");
        header.setFont(new Font("SansSerif", Font.BOLD, 18));

        JTextArea t = new JTextArea("Challenge: Create a string variable and print it.");
        t.setEditable(false);
        t.setOpaque(false);

        JTextArea code = new JTextArea("// Declare 'string msg' and print it\n");
        code.setFont(new Font("Monospaced", Font.PLAIN, 14));
        code.setBorder(new LineBorder(Color.GRAY));

        JButton run = createButton("Test Code", e -> {
            String res = CompilerFacade.compileAndRun(code.getText());
            if (res.length() > 0 && !res.startsWith("ERROR"))
                JOptionPane.showMessageDialog(p, "🎉 Success!");
            else
                JOptionPane.showMessageDialog(p, "❌ No output or error occurred.");
        }, BTN_GREEN);

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(t, BorderLayout.NORTH);
        center.add(new JScrollPane(code), BorderLayout.CENTER);
        p.add(header, BorderLayout.NORTH);
        p.add(center, BorderLayout.CENTER);
        p.add(run, BorderLayout.SOUTH);
        return p;
    }

    private JPanel createMultiQuizCard(List<Question> questions) {
        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(false);
        JLabel qLabel = new JLabel();
        qLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        JPanel optionsPanel = new JPanel(new GridLayout(3, 1));
        optionsPanel.setOpaque(false);
        JRadioButton[] radios = new JRadioButton[3];
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < 3; i++) {
            radios[i] = new JRadioButton();
            radios[i].setOpaque(false);
            group.add(radios[i]);
            optionsPanel.add(radios[i]);
        }

        JLabel feedback = new JLabel(" ");
        JButton checkBtn = createButton("Check Answer", null, BTN_BLUE);
        JButton nextBtn = createButton("Next", null, BTN_GREY);
        nextBtn.setEnabled(false);

        final int[] idx = { 0 };
        Runnable loadQ = () -> {
            Question q = questions.get(idx[0]);
            qLabel.setText("Q" + (idx[0] + 1) + ": " + q.text);
            for (int i = 0; i < 3; i++) {
                radios[i].setText(q.options[i]);
                radios[i].setSelected(false);
                radios[i].setEnabled(true);
            }
            feedback.setText(" ");
            checkBtn.setEnabled(true);
            nextBtn.setEnabled(false);
        };

        checkBtn.addActionListener(e -> {
            int sel = -1;
            for (int i = 0; i < 3; i++)
                if (radios[i].isSelected())
                    sel = i;
            if (sel == -1)
                return;
            Question q = questions.get(idx[0]);
            if (sel == q.correct) {
                feedback.setText("✅ Correct! " + q.explanation);
                feedback.setForeground(new Color(0, 150, 0));
            } else {
                feedback.setText("❌ Wrong.");
                feedback.setForeground(Color.RED);
            }
            checkBtn.setEnabled(false);
            nextBtn.setEnabled(true);
        });

        nextBtn.addActionListener(e -> {
            if (idx[0] < questions.size() - 1) {
                idx[0]++;
                loadQ.run();
            } else {
                JOptionPane.showMessageDialog(card, "Quiz Complete!");
                idx[0] = 0;
                loadQ.run();
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setOpaque(false);
        btnPanel.add(checkBtn);
        btnPanel.add(nextBtn);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);
        center.add(qLabel);
        center.add(optionsPanel);
        center.add(feedback);
        center.add(btnPanel);

        card.add(center, BorderLayout.CENTER);
        loadQ.run();
        return card;
    }

    static class Question {
        String text;
        String[] options;
        int correct;
        String explanation;

        Question(String t, String[] o, int c, String e) {
            text = t;
            options = o;
            correct = c;
            explanation = e;
        }
    }

    private List<Question> getVarQuestions() {
        List<Question> q = new ArrayList<>();
        q.add(new Question("Keyword for integers?", new String[] { "var", "int", "num" }, 1,
                "int stands for Integer."));
        q.add(new Question("Keyword for text?", new String[] { "string", "txt", "word" }, 0, "Strings store text."));
        return q;
    }

    private List<Question> getLogicQuestions() {
        List<Question> q = new ArrayList<>();
        q.add(new Question("Loop keyword?", new String[] { "loop", "while", "repeat" }, 1, "While loops repeat code."));
        q.add(new Question("Result of 10 > 2?", new String[] { "1 (True)", "0 (False)", "Error" }, 0,
                "Evaluates to 1."));
        return q;
    }

    // --- Helpers ---
    private void openFile() {
        JFileChooser c = new JFileChooser();
        if (c.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            try (FileReader r = new FileReader(c.getSelectedFile())) {
                inputArea.read(r, null);
            } catch (Exception e) {
            }
    }

    private void saveFile() {
        JFileChooser c = new JFileChooser();
        if (c.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
            try (FileWriter w = new FileWriter(c.getSelectedFile())) {
                inputArea.write(w);
            } catch (Exception e) {
            }
    }

    private void clearWorkspace() {
        inputArea.setText("");
        consoleArea.setText("");
    }

    private void changeFontSize(int d) {
        fontSize = Math.max(12, Math.min(32, fontSize + d));
        updateFont();
    }

    private void updateFont() {
        Font f = new Font("Monospaced", Font.PLAIN, fontSize);
        inputArea.setFont(f);
        consoleArea.setFont(f);
        tokenArea.setFont(f);
        bytecodeArea.setFont(f);
    }

    private JPanel createTitledPanel(String t, JComponent c) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_PANEL);
        p.setBorder(new LineBorder(BORDER_COLOR, 1));
        JLabel l = new JLabel(t);
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        l.setForeground(TEXT_COLOR);
        l.setBorder(new EmptyBorder(8, 10, 8, 10));
        p.add(l, BorderLayout.NORTH);
        p.add(c, BorderLayout.CENTER);
        return p;
    }

    private JButton createButton(String t, java.awt.event.ActionListener l, Color c) {
        JButton b = new JButton(t);
        b.addActionListener(l);
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setBorder(new EmptyBorder(8, 15, 8, 15));
        return b;
    }

    private void setStatus(String m, Color c) {
        statusLabel.setText(m);
        statusLabel.setForeground(c);
    }

    private void updateAstTree(List<Ast.Stmt> program) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Program");
        for (Ast.Stmt stmt : program)
            root.add(buildAstNode(stmt));
        ((DefaultTreeModel) astTree.getModel()).setRoot(root);
        for (int i = 0; i < astTree.getRowCount(); i++)
            astTree.expandRow(i);
    }

    private DefaultMutableTreeNode buildAstNode(Object node) {
        if (node instanceof Ast.Stmt stmt) {
            if (stmt instanceof Ast.VarDecl v)
                return new DefaultMutableTreeNode("VarDecl: " + v.name());
            if (stmt instanceof Ast.Print p) {
                DefaultMutableTreeNode n = new DefaultMutableTreeNode("Print");
                n.add(buildAstNode(p.value()));
                return n;
            }
            if (stmt instanceof Ast.Assign a) {
                DefaultMutableTreeNode n = new DefaultMutableTreeNode("Assign: " + a.name());
                n.add(buildAstNode(a.value()));
                return n;
            }
            if (stmt instanceof Ast.While w) {
                DefaultMutableTreeNode n = new DefaultMutableTreeNode("While");
                n.add(buildAstNode(w.condition()));
                DefaultMutableTreeNode body = new DefaultMutableTreeNode("Body");
                n.add(body);
                body.add(buildAstNode(w.body()));
                return n;
            }
            if (stmt instanceof Ast.If i) {
                DefaultMutableTreeNode n = new DefaultMutableTreeNode("If");
                n.add(buildAstNode(i.condition()));
                n.add(buildAstNode(i.thenBranch()));
                if (i.elseBranch() != null)
                    n.add(buildAstNode(i.elseBranch()));
                return n;
            }
            if (stmt instanceof Ast.Block b) {
                DefaultMutableTreeNode n = new DefaultMutableTreeNode("Block");
                for (Ast.Stmt s : b.statements())
                    n.add(buildAstNode(s));
                return n;
            }
        } else if (node instanceof Ast.Expr expr) {
            if (expr instanceof Ast.Binary b) {
                DefaultMutableTreeNode n = new DefaultMutableTreeNode("Binary: " + b.op());
                n.add(buildAstNode(b.left()));
                n.add(buildAstNode(b.right()));
                return n;
            }
            if (expr instanceof Ast.IntLiteral i)
                return new DefaultMutableTreeNode("Int: " + i.value());
            if (expr instanceof Ast.StringLiteral s)
                return new DefaultMutableTreeNode("String: \"" + s.value() + "\"");
            if (expr instanceof Ast.Var v)
                return new DefaultMutableTreeNode("VarUsage: " + v.name());
        }
        return new DefaultMutableTreeNode(node.getClass().getSimpleName());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App().setVisible(true));
    }

    // --- COMPILER BACKEND ---

    static class Ast {
        interface Stmt {
            void accept(StmtVisitor v);
        }

        interface Expr {
            <R> R accept(ExprVisitor<R> v);
        }

        interface StmtVisitor {
            void visitVarDecl(VarDecl s);

            void visitPrint(Print s);

            void visitAssign(Assign s);

            void visitIf(If s);

            void visitWhile(While s);

            void visitBlock(Block s);
        }

        interface ExprVisitor<R> {
            R visitInt(IntLiteral e);

            R visitString(StringLiteral e);

            R visitVar(Var e);

            R visitBinary(Binary e);

            R visitGrouping(Grouping e);
        }

        record VarDecl(String name, Expr init) implements Stmt {
            public void accept(StmtVisitor v) {
                v.visitVarDecl(this);
            }
        }

        record Print(Expr value) implements Stmt {
            public void accept(StmtVisitor v) {
                v.visitPrint(this);
            }
        }

        record Assign(String name, Expr value) implements Stmt {
            public void accept(StmtVisitor v) {
                v.visitAssign(this);
            }
        }

        record If(Expr condition, Stmt thenBranch, Stmt elseBranch) implements Stmt {
            public void accept(StmtVisitor v) {
                v.visitIf(this);
            }
        }

        record While(Expr condition, Stmt body) implements Stmt {
            public void accept(StmtVisitor v) {
                v.visitWhile(this);
            }
        }

        record Block(List<Stmt> statements) implements Stmt {
            public void accept(StmtVisitor v) {
                v.visitBlock(this);
            }
        }

        record IntLiteral(int value) implements Expr {
            public <R> R accept(ExprVisitor<R> v) {
                return v.visitInt(this);
            }
        }

        record StringLiteral(String value) implements Expr {
            public <R> R accept(ExprVisitor<R> v) {
                return v.visitString(this);
            }
        }

        record Var(String name) implements Expr {
            public <R> R accept(ExprVisitor<R> v) {
                return v.visitVar(this);
            }
        }

        record Binary(Expr left, String op, Expr right) implements Expr {
            public <R> R accept(ExprVisitor<R> v) {
                return v.visitBinary(this);
            }
        }

        record Grouping(Expr inner) implements Expr {
            public <R> R accept(ExprVisitor<R> v) {
                return v.visitGrouping(this);
            }
        }
    }

    static class SemanticAnalyzer implements Ast.StmtVisitor, Ast.ExprVisitor<Void> {
        private final List<String> errors = new ArrayList<>();
        private final Stack<Set<String>> scopes = new Stack<>();

        public SemanticAnalyzer() {
            scopes.push(new HashSet<>());
        }

        public List<String> analyze(List<Ast.Stmt> program) {
            for (Ast.Stmt s : program)
                s.accept(this);
            return errors;
        }

        private void declare(String name) {
            if (scopes.peek().contains(name))
                errors.add("Variable '" + name + "' already declared.");
            else
                scopes.peek().add(name);
        }

        private void check(String name) {
            for (int i = scopes.size() - 1; i >= 0; i--)
                if (scopes.get(i).contains(name))
                    return;
            errors.add("Variable '" + name + "' not defined.");
        }

        public void visitVarDecl(Ast.VarDecl s) {
            if (s.init() != null)
                s.init().accept(this);
            declare(s.name());
        }

        public void visitAssign(Ast.Assign s) {
            s.value().accept(this);
            check(s.name());
        }

        public void visitPrint(Ast.Print s) {
            s.value().accept(this);
        }

        public void visitIf(Ast.If s) {
            s.condition().accept(this);
            s.thenBranch().accept(this);
            if (s.elseBranch() != null)
                s.elseBranch().accept(this);
        }

        public void visitWhile(Ast.While s) {
            s.condition().accept(this);
            s.body().accept(this);
        }

        public void visitBlock(Ast.Block s) {
            scopes.push(new HashSet<>());
            for (Ast.Stmt stmt : s.statements())
                stmt.accept(this);
            scopes.pop();
        }

        public Void visitInt(Ast.IntLiteral e) {
            return null;
        }

        public Void visitString(Ast.StringLiteral e) {
            return null;
        }

        public Void visitVar(Ast.Var e) {
            check(e.name());
            return null;
        }

        public Void visitBinary(Ast.Binary e) {
            e.left().accept(this);
            e.right().accept(this);
            return null;
        }

        public Void visitGrouping(Ast.Grouping e) {
            e.inner().accept(this);
            return null;
        }
    }

    static class BytecodeEmitter implements Ast.StmtVisitor, Ast.ExprVisitor<Void> {
        private final StringBuilder sb = new StringBuilder();
        private int labelCount = 0;

        public String generate(List<Ast.Stmt> program) {
            for (Ast.Stmt s : program)
                s.accept(this);
            return sb.toString();
        }

        private void emit(String op) {
            sb.append(op).append("\n");
        }

        private String nextLabel() {
            return "L" + (labelCount++);
        }

        public void visitVarDecl(Ast.VarDecl s) {
            if (s.init() != null)
                s.init().accept(this);
            else
                emit("PUSH 0");
            emit("STORE " + s.name());
        }

        public void visitPrint(Ast.Print s) {
            s.value().accept(this);
            emit("PRINT");
        }

        public void visitAssign(Ast.Assign s) {
            s.value().accept(this);
            emit("STORE " + s.name());
        }

        public void visitBlock(Ast.Block s) {
            for (Ast.Stmt stmt : s.statements())
                stmt.accept(this);
        }

        public void visitIf(Ast.If s) {
            String elseLabel = nextLabel();
            String endLabel = nextLabel();
            s.condition().accept(this);
            emit("JMP_FALSE " + elseLabel);
            s.thenBranch().accept(this);
            emit("GOTO " + endLabel);
            emit(elseLabel + ":");
            if (s.elseBranch() != null)
                s.elseBranch().accept(this);
            emit(endLabel + ":");
        }

        public void visitWhile(Ast.While s) {
            String startLabel = nextLabel();
            String endLabel = nextLabel();
            emit(startLabel + ":");
            s.condition().accept(this);
            emit("JMP_FALSE " + endLabel);
            s.body().accept(this);
            emit("GOTO " + startLabel);
            emit(endLabel + ":");
        }

        public Void visitInt(Ast.IntLiteral e) {
            emit("PUSH " + e.value());
            return null;
        }

        public Void visitString(Ast.StringLiteral e) {
            emit("PUSH \"" + e.value() + "\"");
            return null;
        }

        public Void visitVar(Ast.Var e) {
            emit("LOAD " + e.name());
            return null;
        }

        public Void visitBinary(Ast.Binary e) {
            e.left().accept(this);
            e.right().accept(this);
            switch (e.op()) {
                case "+" -> emit("ADD");
                case "-" -> emit("SUB");
                case "*" -> emit("MUL");
                case "/" -> emit("DIV");
                case "<" -> emit("LT");
                case ">" -> emit("GT");
                case "==" -> emit("EQ");
                case "<=" -> emit("LE");
                case ">=" -> emit("GE");
                case "!=" -> emit("NEQ");
            }
            return null;
        }

        public Void visitGrouping(Ast.Grouping e) {
            e.inner().accept(this);
            return null;
        }
    }

    // --- UPDATED INTERPRETER WITH STRING & OBJECT SUPPORT ---
    static class Interpreter implements Ast.ExprVisitor<Object>, Ast.StmtVisitor {
        private final Environment env = new Environment();
        private final StringBuilder out;

        public Interpreter(StringBuilder out) {
            this.out = out;
        }

        public void run(List<Ast.Stmt> program) {
            for (Ast.Stmt s : program)
                s.accept(this);
        }

        public void visitVarDecl(Ast.VarDecl s) {
            env.declare(s.name(), s.init() == null ? 0 : eval(s.init()));
        }

        public void visitAssign(Ast.Assign s) {
            env.set(s.name(), eval(s.value()));
        }

        public void visitIf(Ast.If s) {
            if (truthy(eval(s.condition())))
                s.thenBranch().accept(this);
            else if (s.elseBranch() != null)
                s.elseBranch().accept(this);
        }

        public void visitWhile(Ast.While s) {
            while (truthy(eval(s.condition())))
                s.body().accept(this);
        }

        public void visitBlock(Ast.Block s) {
            env.push();
            try {
                for (Ast.Stmt stmt : s.statements())
                    stmt.accept(this);
            } finally {
                env.pop();
            }
        }

        public void visitPrint(Ast.Print s) {
            out.append(eval(s.value())).append("\n");
        }

        private Object eval(Ast.Expr e) {
            return e.accept(this);
        }

        private boolean truthy(Object o) {
            if (o instanceof Integer i)
                return i != 0;
            return false;
        }

        public Object visitInt(Ast.IntLiteral e) {
            return e.value();
        }

        public Object visitString(Ast.StringLiteral e) {
            return e.value();
        }

        public Object visitVar(Ast.Var e) {
            return env.get(e.name());
        }

        public Object visitGrouping(Ast.Grouping e) {
            return eval(e.inner());
        }

        public Object visitBinary(Ast.Binary e) {
            Object a = eval(e.left());
            Object b = eval(e.right());
            if (e.op().equals("+")) {
                if (a instanceof String || b instanceof String)
                    return String.valueOf(a) + String.valueOf(b);
                if (a instanceof Integer ia && b instanceof Integer ib)
                    return ia + ib;
            }
            if (a instanceof Integer ia && b instanceof Integer ib) {
                return switch (e.op()) {
                    case "-" -> ia - ib;
                    case "*" -> ia * ib;
                    case "/" -> ib == 0 ? 0 : ia / ib;
                    case "<" -> ia < ib ? 1 : 0;
                    case ">" -> ia > ib ? 1 : 0;
                    case "<=" -> ia <= ib ? 1 : 0;
                    case ">=" -> ia >= ib ? 1 : 0;
                    case "==" -> ia.equals(ib) ? 1 : 0;
                    case "!=" -> !ia.equals(ib) ? 1 : 0;
                    default -> 0;
                };
            }
            return 0;
        }
    }

    static class CompilerFacade {
        public static String compileAndRun(String source) {
            StringBuilder log = new StringBuilder();
            try {
                Lexer lexer = new Lexer(source);
                List<Token> tokens = lexer.scanTokens();
                Parser parser = new Parser(tokens);
                List<Ast.Stmt> program = parser.parseProgram();
                Interpreter interpreter = new Interpreter(log);
                interpreter.run(program);
            } catch (Exception ex) {
                log.append("ERROR: ").append(ex.getMessage());
            }
            return log.toString();
        }
    }

    static class Parser {
        private final List<Token> tokens;
        private int current = 0;

        public Parser(List<Token> tokens) {
            this.tokens = tokens;
        }

        public List<Ast.Stmt> parseProgram() {
            List<Ast.Stmt> s = new ArrayList<>();
            while (!isAtEnd())
                s.add(statement());
            return s;
        }

        private Ast.Stmt statement() {
            if (match(TokenType.KW_INT))
                return varDecl();
            // FIXED: Allow string declarations
            if (match(TokenType.KW_STRING))
                return varDecl();
            if (match(TokenType.KW_PRINT))
                return printStmt();
            if (match(TokenType.KW_IF))
                return ifStmt();
            if (match(TokenType.KW_WHILE))
                return whileStmt();
            if (match(TokenType.LBRACE))
                return new Ast.Block(block());
            if (check(TokenType.IDENT))
                return assignStmt();
            throw new RuntimeException("Expect statement at line " + peek().line);
        }

        private Ast.Stmt varDecl() {
            Token n = consume(TokenType.IDENT, "Name expected");
            Ast.Expr i = match(TokenType.EQUAL) ? expression() : null;
            consume(TokenType.SEMICOLON, "Expected ';' after declaration at line " + n.line);
            return new Ast.VarDecl(n.lexeme, i);
        }

        private Ast.Stmt printStmt() {
            consume(TokenType.LPAREN, "(");
            Ast.Expr v = expression();
            consume(TokenType.RPAREN, ")");
            consume(TokenType.SEMICOLON, ";");
            return new Ast.Print(v);
        }

        private Ast.Stmt ifStmt() {
            consume(TokenType.LPAREN, "(");
            Ast.Expr c = expression();
            consume(TokenType.RPAREN, ")");
            Ast.Stmt t = statement();
            Ast.Stmt e = match(TokenType.KW_ELSE) ? statement() : null;
            return new Ast.If(c, t, e);
        }

        private Ast.Stmt whileStmt() {
            consume(TokenType.LPAREN, "(");
            Ast.Expr c = expression();
            consume(TokenType.RPAREN, ")");
            return new Ast.While(c, statement());
        }

        private List<Ast.Stmt> block() {
            List<Ast.Stmt> s = new ArrayList<>();
            while (!check(TokenType.RBRACE) && !isAtEnd())
                s.add(statement());
            consume(TokenType.RBRACE, "}");
            return s;
        }

        private Ast.Stmt assignStmt() {
            Token n = consume(TokenType.IDENT, "ID");
            consume(TokenType.EQUAL, "Expected '='");
            Ast.Expr v = expression();
            consume(TokenType.SEMICOLON, ";");
            return new Ast.Assign(n.lexeme, v);
        }

        private Ast.Expr expression() {
            return equality();
        }

        private Ast.Expr equality() {
            Ast.Expr e = comparison();
            while (match(TokenType.EQUAL_EQUAL, TokenType.BANG_EQUAL))
                e = new Ast.Binary(e, previous().lexeme, comparison());
            return e;
        }

        private Ast.Expr comparison() {
            Ast.Expr e = term();
            while (match(TokenType.LESS, TokenType.GREATER, TokenType.LESS_EQUAL, TokenType.GREATER_EQUAL))
                e = new Ast.Binary(e, previous().lexeme, term());
            return e;
        }

        private Ast.Expr term() {
            Ast.Expr e = factor();
            while (match(TokenType.PLUS, TokenType.MINUS))
                e = new Ast.Binary(e, previous().lexeme, factor());
            return e;
        }

        private Ast.Expr factor() {
            Ast.Expr e = primary();
            while (match(TokenType.STAR, TokenType.SLASH))
                e = new Ast.Binary(e, previous().lexeme, primary());
            return e;
        }

        private Ast.Expr primary() {
            if (match(TokenType.INT_LITERAL))
                return new Ast.IntLiteral(Integer.parseInt(previous().lexeme));
            if (match(TokenType.STRING_LITERAL))
                return new Ast.StringLiteral(previous().lexeme);
            if (match(TokenType.IDENT))
                return new Ast.Var(previous().lexeme);
            if (match(TokenType.LPAREN)) {
                Ast.Expr e = expression();
                consume(TokenType.RPAREN, ")");
                return new Ast.Grouping(e);
            }
            throw new RuntimeException("Expect expression");
        }

        private boolean match(TokenType... t) {
            for (TokenType type : t)
                if (check(type)) {
                    advance();
                    return true;
                }
            return false;
        }

        private boolean check(TokenType t) {
            return !isAtEnd() && peek().type == t;
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

        private Token consume(TokenType t, String m) {
            if (check(t))
                return advance();
            throw new RuntimeException(m + " at line " + peek().line);
        }
    }

    static class Lexer {
        private final String src;
        private int start = 0, current = 0, line = 1;
        private final List<Token> tokens = new ArrayList<>();
        private static final Map<String, TokenType> k = new HashMap<>();
        static {
            k.put("int", TokenType.KW_INT);
            k.put("if", TokenType.KW_IF);
            k.put("else", TokenType.KW_ELSE);
            k.put("while", TokenType.KW_WHILE);
            k.put("print", TokenType.KW_PRINT);
            k.put("string", TokenType.KW_STRING);
            k.put("bool", TokenType.KW_BOOL);
            k.put("float", TokenType.KW_FLOAT);
        }

        public Lexer(String src) {
            this.src = src;
        }

        public List<Token> scanTokens() {
            while (!isAtEnd()) {
                start = current;
                scanToken();
            }
            tokens.add(new Token(TokenType.EOF, "", line));
            return tokens;
        }

        private void scanToken() {
            char c = advance();
            switch (c) {
                case '(':
                    add(TokenType.LPAREN);
                    break;
                case ')':
                    add(TokenType.RPAREN);
                    break;
                case '{':
                    add(TokenType.LBRACE);
                    break;
                case '}':
                    add(TokenType.RBRACE);
                    break;
                case ';':
                    add(TokenType.SEMICOLON);
                    break;
                case '+':
                    add(TokenType.PLUS);
                    break;
                case '-':
                    add(TokenType.MINUS);
                    break;
                case '*':
                    add(TokenType.STAR);
                    break;
                case '!':
                    add(match('=') ? TokenType.BANG_EQUAL : TokenType.EOF);
                    break;
                case '/':
                    if (match('/')) {
                        while (peek() != '\n' && !isAtEnd())
                            advance();
                    } else
                        add(TokenType.SLASH);
                    break;
                case '=':
                    add(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
                    break;
                case '<':
                    add(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
                    break;
                case '>':
                    add(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
                    break;
                case '"':
                    string();
                    break;
                case ' ':
                case '\r':
                case '\t':
                    break;
                case '\n':
                    line++;
                    break;
                default:
                    if (isDigit(c))
                        number();
                    else if (isAlpha(c))
                        identifier();
                    else
                        throw new RuntimeException("Bad char: " + c);
            }
        }

        private void string() {
            while (peek() != '"' && !isAtEnd()) {
                if (peek() == '\n')
                    line++;
                advance();
            }
            if (isAtEnd())
                throw new RuntimeException("Unterminated string");
            advance();
            add(TokenType.STRING_LITERAL, src.substring(start + 1, current - 1));
        }

        private void number() {
            while (isDigit(peek()))
                advance();
            add(TokenType.INT_LITERAL);
        }

        private void identifier() {
            while (isAlphaNumeric(peek()))
                advance();
            String txt = src.substring(start, current);
            add(k.getOrDefault(txt, TokenType.IDENT));
        }

        private boolean isAtEnd() {
            return current >= src.length();
        }

        private char advance() {
            return src.charAt(current++);
        }

        private char peek() {
            return isAtEnd() ? '\0' : src.charAt(current);
        }

        private boolean match(char e) {
            if (isAtEnd() || src.charAt(current) != e)
                return false;
            current++;
            return true;
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

        private void add(TokenType t) {
            add(t, src.substring(start, current));
        }

        private void add(TokenType t, String l) {
            tokens.add(new Token(t, l, line));
        }
    }

    // --- UPDATED ENVIRONMENT (Objects) ---
    static class Environment {
        private final Deque<Map<String, Object>> scopes = new ArrayDeque<>();

        public Environment() {
            push();
        }

        public void push() {
            scopes.push(new HashMap<>());
        }

        public void pop() {
            scopes.pop();
        }

        public void declare(String n, Object v) {
            if (scopes.peek().containsKey(n))
                throw new RuntimeException("Redeclared: " + n);
            scopes.peek().put(n, v);
        }

        public void set(String n, Object v) {
            for (Map<String, Object> s : scopes)
                if (s.containsKey(n)) {
                    s.put(n, v);
                    return;
                }
            throw new RuntimeException("Undefined: " + n);
        }

        public Object get(String n) {
            for (Map<String, Object> s : scopes)
                if (s.containsKey(n))
                    return s.get(n);
            throw new RuntimeException("Undefined: " + n);
        }
    }

    enum TokenType {
        KW_INT, KW_PRINT, KW_IF, KW_ELSE, KW_WHILE, KW_STRING, KW_BOOL, KW_FLOAT,
        IDENT, INT_LITERAL, STRING_LITERAL,
        PLUS, MINUS, STAR, SLASH,
        EQUAL, EQUAL_EQUAL, BANG_EQUAL, LESS, GREATER, LESS_EQUAL, GREATER_EQUAL,
        LPAREN, RPAREN, LBRACE, RBRACE, SEMICOLON, EOF
    }

    record Token(TokenType type, String lexeme, int line) {
        @Override
        public String toString() {
            return String.format("[%s] '%s' (Line %d)", type, lexeme, line);
        }
    }
}