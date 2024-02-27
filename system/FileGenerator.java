import java.io.*;
import java.util.Random;

public class FileGenerator {

    public static void main(String[] args) {
        for (int i = 1; i <= 40; i++) {
            String fileName = "i386_" + generateRandomHex() + ".sys";
            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                // Write i386 header
                fos.write("i386 header".getBytes());

                // Write 50,000 random bytes
                byte[] randomBytes = new byte[50000];
                new Random().nextBytes(randomBytes);
                fos.write(randomBytes);

                // Prompt user for additional data
                System.out.println("Enter additional data:");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String userData = reader.readLine();

                // Obfuscate user data
                byte[] obfuscatedData = obfuscateData(userData);

                // Write obfuscated data below the header
                fos.write(new byte[10]); // 10 empty bytes below the header
                fos.write(obfuscatedData);

                System.out.println("Data obfuscated and written to file: " + fileName);
            } catch (IOException e) {
                System.err.println("Error creating file: " + e.getMessage());
            }
        }
    }

    private static String generateRandomHex() {
        // Generate a random 10-character hex identifier
        return Long.toHexString(Double.doubleToLongBits(Math.random()));
    }

    private static byte[] obfuscateData(String data) {
        // Obfuscate data by converting to Unicode
        byte[] obfuscatedData = data.getBytes();
        System.out.print("HEX bytes written to obfuscated data: ");
        for (byte b : obfuscatedData) {
            System.out.print(Integer.toHexString(b & 0xFF) + " ");
        }
        System.out.println();
        return obfuscatedData;
    }
}
