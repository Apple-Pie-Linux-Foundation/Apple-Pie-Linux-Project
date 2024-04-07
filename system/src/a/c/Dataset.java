package a.c;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Dataset {

    private List<String> lines;
    private String outputFilePath;

    public ExtendedDataset(String outputFilePath) {
        this.lines = new ArrayList<>();
        this.outputFilePath = outputFilePath;
    }

    public void addLine(String line) {
        lines.add(line);
    }

    public void writeToFile() throws IOException {
        File outputFile = new File(outputFilePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, StandardCharsets.UTF_8))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ExtendedDataset dataset = new ExtendedDataset("/usr/lib/dataset/dataset_data.txt");

        // Add hashes
        for (int i = 0; i < 10; i++) {
            dataset.addLine("Hash: " + hash(i));
        }

        // Add obfuscated data
        for (int i = 0; i < 10; i++) {
            dataset.addLine("Obfuscated: " + obfuscate(i));
        }

        // Add encrypted data
        for (int i = 0; i < 10; i++) {
            dataset.addLine("Encrypted: " + encrypt(i));
        }

        // Add random data
        for (int i = 0; i < 10; i++) {
            dataset.addLine("Random: " + generateRandomData());
        }

        // Add integers
        for (int i = 0; i < 10; i++) {
            dataset.addLine("Integer: " + i);
        }

        // Add strings
        for (int i = 0; i < 10; i++) {
            dataset.addLine("String: " + "Sample string " + i);
        }

        dataset.writeToFile();
        System.out.println("Dataset written to: " + dataset.outputFilePath);
    }

    private static String hash(int value) {
        return Long.toHexString(Double.doubleToLongBits(value));
    }

    private static String obfuscate(int value) {
        return Integer.toHexString(value);
    }

    private static String encrypt(int value) {
        return Base64.getEncoder().encodeToString((value + "").getBytes());
    }

    private static String generateRandomData() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append((char) (random.nextInt(26) + 'a'));
        }
        return sb.toString();
    }
}