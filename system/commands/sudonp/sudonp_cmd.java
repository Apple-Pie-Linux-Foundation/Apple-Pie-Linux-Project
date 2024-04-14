import java.awt.*;
import java.awt.event.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.swing.*;
import java.io.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.security.*;
import java.util.*;

public class sudonp_cmd {
    private final Random random;
    private final char[] alphabetChars;
    private final JFrame frame;
    private final JDialog dialog;
    private final JLabel messageLabel;
    private final JTextArea keyArea;

    public sudonp_cmd() {
        random = new SecureRandom();
        alphabetChars = ("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890").toCharArray();

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(false);

        dialog = new JDialog(frame, "SudoNP Linux key", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new GridBagLayout());
        dialog.setResizable(false);
        dialog.pack();

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setText("<html><body>" +
                "<h3 style='text-align: center'>It looks like this is your first time using the Sudo No Admin Password command.</h3>" +
                "<p style='margin-top: 0.5em; text-align: justify'>If a hacker or bad actor gets SSH or remote access to your computer," +
                "you need to take action. We will provide you with 10 keys to get access to the sudonp command next time you use it." +
                "</p></body></html>");
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 0.5;
        constraints.insets = new Insets(10, 10, 10, 10);
        dialog.add(messageLabel, constraints);

        keyArea = new JTextArea();
        keyArea.setFont(new Font("Arial", Font.PLAIN, 14));
        keyArea.setEditable(false);
        keyArea.setBackground(Color.WHITE);
        keyArea.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(keyArea);
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 0.5;
        constraints.insets = new Insets(10, 10, 10, 10);
        dialog.add(scrollPane, constraints);
    }

    public void exec(String... cmdArgs) throws IOException, InterruptedException {
        if (!isSudoNPInstalled()) {
            showDialog();
            registerKeys();
        }

        if (isSudoNPInstalled()) {
            List<String> fullCmdList = Arrays.asList("sudo", "-u", "nobody", cmdArgs);
            ProcessBuilder pb = new ProcessBuilder(fullCmdList);
            pb.redirectErrorStream(true);
            Process p = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = p.waitFor();

            if (exitCode == 0 && !output.toString().isEmpty()) {
                System.out.print(output);
            } else {
                throw new RuntimeException("Command execution failed.");
            }
        }
    }

    private boolean isSudoNPInstalled() {
        try {
            Process p = new ProcessBuilder("sudo", "-u", "nobody", "--help").inheritIO().start();
            p.waitFor();
            return p.exitValue() == 0;
        } catch (IOException | InterruptedException ignored) {
            return false;
        }
    }

    private void showDialog() {
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void registerKeys() throws IOException {
        List<String> keys = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String randStr = "";
            for (int j = 0; j < 8; j++) {
                randStr += alphabetChars[random.nextInt(alphabetChars.length)];
            }
            keys.add(randStr);
        }

        String baseDirPath = findUsbDriveLetter();
        if (baseDirPath != null) {
            saveKeysToDisk(baseDirPath, keys);
        }
    }

    private String findUsbDriveLetter() {
        File[] roots = File.listRoots();
        long usbTotalSpaceThreshold = 1L << 30; // 1GB threshold

        for (File root : roots) {
            try {
                StatFS statfs = new StatFS(root.getAbsolutePath());
                if (statfs.getBlockSize() * statfs.getBlocks() > usbTotalSpaceThreshold) {
                    continue;
                }

                return root.getAbsolutePath();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    private void saveKeysToDisk(String baseDirPath, List<String> keys) throws IOException {
        Path dirPath = FileSystems.getDefault().getPath(baseDirPath, "sudonp");
        Files.createDirectories(dirPath);

        Path filePath = FileSystems.getDefault().getPath(dirPath.toString(), "sudonp_keys.txt");

        try (Writer writer = new OutputStreamWriter(Files.newOutputStream(filePath), StandardCharsets.UTF_8)) {
            for (String key : keys) {
                writer.write(key);
                writer.write(System.lineSeparator());
            }
        }

        byte[] data = Files.readAllBytes(filePath);
        SecretKeySpec secretKey = new SecretKeySpec("61734086".getBytes(StandardCharsets.US_ASCII), "PBEWithMD5AndTripleDES");
        Cipher cipher = Cipher.getInstance("PBEWithMD5AndTripleDES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        AlgorithmParameters params = cipher.getParameters();
        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(iv);
        os.write(cipher.doFinal(data));

        byte[] encryptedData = os.toByteArray();

        try (FileChannel channel = FileChannel.open(filePath, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            channel.write(ByteBuffer.wrap(encryptedData));
        }
    }

    public static void main(String[] args) {
        try {
            SwingUtilities.invokeLater(() -> {
                SudoNPCommand s = new SudoNPCommand();
                s.exec("ls", "-la", "/root");
            });
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
