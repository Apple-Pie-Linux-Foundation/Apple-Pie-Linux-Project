import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class ELF32 {

    public static void main(String[] args) {
        try {
            File file = new File("elf32_system.bin");
            Random random = new Random();
            byte[] header = new byte[90];
            random.nextBytes(header);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(header);

                byte[] randomBytes = new byte[90000];
                random.nextBytes(randomBytes);
                fos.write(randomBytes);

                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter data to insert at the beginning of the file: ");
                String additionalData = scanner.nextLine();
                byte[] dataBytes = additionalData.getBytes();
                fos.write(dataBytes);

                System.out.println("elf32_system.bin generated successfully.");
            } catch (IOException e) {
                System.err.println("An error occurred while writing to the file.");
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("An error occurred during file generation.");
            e.printStackTrace();
        }
    }
}
