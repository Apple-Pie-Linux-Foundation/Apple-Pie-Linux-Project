import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class Elf32BinaryGenerator {

    public static void main(String[] args) {
        try (FileOutputStream fos = new FileOutputStream("elf32.bin")) {
            Random random = new Random();

            // Generate ELF32 header with 40 random bytes
            byte[] elfHeader = new byte[40];
            random.nextBytes(elfHeader);
            fos.write(elfHeader);

            // Write 5 empty bytes
            byte[] emptyBytes = new byte[5];
            fos.write(emptyBytes);

            // Generate 40000 random bytes
            byte[] randomBytes = new byte[40000];
            random.nextBytes(randomBytes);
            fos.write(randomBytes);

            System.out.println("elf32.bin generated successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred while writing the file: " + e.getMessage());
        }
    }
}
