import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class ELF64 {

    public static void main(String[] args) {
        try {
            Random random = new Random();
            byte[] header = new byte[90];
            random.nextBytes(header);

            byte[] randomBytes = new byte[90000];
            random.nextBytes(randomBytes);

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter data to insert at the beginning of the file: ");
            String userData = scanner.nextLine();
            byte[] userDataBytes = userData.getBytes();

            FileOutputStream fos = new FileOutputStream("elf64_system.bin");
            fos.write(header);
            fos.write(randomBytes);
            fos.write(userDataBytes);

            fos.close();
            System.out.println("elf64_system.bin file generated successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
}
