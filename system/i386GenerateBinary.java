import java.io.*;
import java.util.*;

public class i386GenerateBinary {

    public static void main(String[] args) {
        try {
            File file = new File("i386.bin");
            FileOutputStream fos = new FileOutputStream(file);

            // Generate 900-byte i386 System header
            byte[] seHeader = new byte[900];
            // Code to generate i386 System header bytes

            // Generate 145008263 random bytes
            byte[] randomBytes = new byte[145008263];
            new Random().nextBytes(randomBytes);

            // Ask user for additional data to insert at the beginning
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter additional data to insert:");
            String additionalData = scanner.nextLine();
            byte[] additionalBytes = additionalData.getBytes();

            // Write data to the file
            fos.write(seHeader);
            fos.write(randomBytes);
            fos.write(additionalBytes);

            fos.close();
            System.out.println("File generated successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred while generating the file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}
