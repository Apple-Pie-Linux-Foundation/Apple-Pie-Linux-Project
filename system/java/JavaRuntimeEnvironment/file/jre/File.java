package jre.io.File;

import java.io.File; // Importing the File class from java.io package

public class Main {
    public static void main(String[] args) {
        // Creating a new File object representing a file or directory path
        File myFile = new File("/path/to/your/file.txt");
        
        // Performing various operations on the File object
        if (myFile.exists()) {
            System.out.println("The file exists.");
        } else {
            System.out.println("The file does not exist.");
        }
        
        if (myFile.isDirectory()) {
            System.out.println("The file is a directory.");
        } else {
            System.out.println("The file is not a directory.");
        }
        
        if (myFile.canRead()) {
            System.out.println("You have read permission.");
        } else {
            System.out.println("You don't have read permission.");
        }
        
        if (myFile.length() > 0) {
            System.out.printf("The size of the file is %d bytes.", myFile.length());
        } else {
            System.out.println("The file has zero length.");
        }
    }
}
