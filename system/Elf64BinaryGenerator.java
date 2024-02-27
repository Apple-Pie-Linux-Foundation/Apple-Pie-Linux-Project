import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class Elf64BinaryGenerator {

    public static void main(String[] args) {
        try (FileOutputStream fos = new FileOutputStream("elf64.bin")) {
            byte[] header = generateRandomBytes(40);
            byte[] emptyBytes = new byte[5];
            byte[] content = generateRandomBytes(40000);

            fos.write(header);
            fos.write(emptyBytes);
            fos.write(content);

            System.out.println("elf64.bin generated successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred while writing the file: " + e.getMessage());
        }
    }

    private static byte[] generateRandomBytes(int length) {
        byte[] bytes = new byte[length];
        new Random().nextBytes(bytes);
        return bytes;
    }
}

