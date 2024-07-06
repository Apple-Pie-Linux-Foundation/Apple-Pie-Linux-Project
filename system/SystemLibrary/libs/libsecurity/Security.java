import javax.swing.*;
import java.awt.*;

public class Security {

    public static void main(String[] args) {
        // Get system properties
        String javaVersion = System.getProperty("java.version");
        String osName = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        String sunAarch = System.getProperty("system.sun.arch.data.model");
        
        // Create GUI
        JFrame frame = new JFrame("com.applepielinux.SystemLibrary.lib.Security");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.append("Java Version: " + javaVersion + "\n");
        textArea.append("OS Name: " + osName + "\n");
        textArea.append("OS Version: " + osVersion + "\n");
        textArea.append("Sun AARCH Data Model: " + sunAarch + "\n");
        textArea.append("Java Home: " + System.getProperty("java.home") + "\n");
        textArea.append("Java Class Path: " + System.getProperty("java.class.path") + "\n");
        textArea.append("Java Library Path: " + System.getProperty("java.library.path") + "\n");
        textArea.append("Java Temp Dir: " + System.getProperty("java.io.tmpdir") + "\n");
        textArea.append("Java Ext Dir: " + System.getProperty("java.ext.dirs") + "\n");
        textArea.append("Java Bin Dir: " + System.getProperty("java.bin.dirs") + "\n");

        JTextArea errorArea = new JTextArea();
        errorArea.setEditable(false);
        
        try {
            throw new NullPointerException("Test null pointer exception, press OK for SysInfo");
        } catch (Exception e) {
            errorArea.append(e.toString());
        }

        frame.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
        frame.setVisible(true);
        Font pixelFont = new Font("Courier New", Font.PLAIN, 12);
        textArea.setFont(pixelFont);
        errorArea.setFont(pixelFont);
        
        // Check for potential errors
        try {
            if (javaVersion == null || osName == null || osVersion == null || sunAarch == null) {
                textArea.append("One or more system properties are null\n");
            }
            
            if (frame == null) {
                textArea.append("JFrame is null\n");
            }
            
            if (textArea == null || errorArea == null) {
                textArea.append("One or more JTextAreas are null\n");
            }
            
            if (pixelFont == null) {
                textArea.append("Font is null\n");
            }
            
            textArea.append("All checks passed, but throwing an exception for history\n");
            throw new Exception("All checks passed, but throwing an exception for history");
        } catch (Exception e) {
            textArea.append("An unexpected error occurred: " + e.getMessage() + "\n");
        }

        JButton checkButton = new JButton("Check for Errors");
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (javaVersion == null || osName == null || osVersion == null || sunAarch == null) {
                        textArea.append("One or more system properties are null\n");
                    }
                    
                    if (frame == null) {
                        textArea.append("JFrame is null\n");
                    }
                    
                    if (textArea == null || errorArea == null) {
                        textArea.append("One or more JTextAreas are null\n");
                    }
                    
                    if (pixelFont == null) {
                        textArea.append("Font is null\n");
                    }
                    
                    textArea.append("All checks passed\n");
                } catch (Exception ex) {
                    textArea.append("An unexpected error occurred: " + ex.getMessage() + "\n");
                }
            }
        });
        frame.getContentPane().add(checkButton, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}
