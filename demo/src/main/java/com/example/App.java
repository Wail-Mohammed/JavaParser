package com.example;

//Steps to compile: from root directory where pom.xml is present
//1. mvn clean compile
//2. java -cp target/classes com.example.App

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {
    private final JTextArea inputArea = new JTextArea(14, 70);
    private final JTextArea outputArea = new JTextArea(12, 70);

    public App() {
        super("Mini Java-like Compiler (statements, loops, conditionals)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        inputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        outputArea.setEditable(false);

        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createTitledBorder("Source Code"));
        top.add(new JScrollPane(inputArea));

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBorder(BorderFactory.createTitledBorder("Compiler / Runtime Output"));
        bottom.add(new JScrollPane(outputArea));

        JPanel buttons = new JPanel();
        JButton compileBtn = new JButton("Compile & Run");
        JButton clearBtn = new JButton("Clear");
        buttons.add(compileBtn);
        buttons.add(clearBtn);

        add(top, BorderLayout.NORTH);
        add(buttons, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        compileBtn.addActionListener(e -> {
            String src = inputArea.getText();
            String result = CompilerFacade.compileAndRun(src);
            outputArea.setText(result);
        });
        clearBtn.addActionListener(e -> {
            inputArea.setText("");
            outputArea.setText("");
        });

        setSize(900, 700);
        setLocationRelativeTo(null);

        // sample program
        inputArea.setText(
                """
                        // Declarations
                        int x = 0;
                        int sum = 0;

                        // while loop
                        while (x < 5) {
                            sum = sum + x;
                            x = x + 1;
                        }

                        // if / else
                        if (sum >= 10) {
                            print(sum);      // prints 10
                        } else {
                            print(0);
                        }

                        // another block & logical ops
                        {
                            int y = 3;
                            if (y > 1 && y < 5) { print(y * 2); } // prints 6
                        }
                        """);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App().setVisible(true));
    }
}
