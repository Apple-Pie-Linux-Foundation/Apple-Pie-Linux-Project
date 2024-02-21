import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Browser extends JFrame {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new Browser().setVisible(true);
        });
    }

    public Browser() {
        super("CatWeaselBrowser");
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints cons = new GridBagConstraints();

        // Initialize the address bar
        JTextField addressBar = new JTextField(40);
        addressBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        URI uri = new URI(addressBar.getText());
                        retrieveIndexFile(uri.getHost(), uri.getPort());
                    } catch (URISyntaxException e) {
                        showErrorMessage(e);
                    }
                }
            }
        });

        // Initialize the editor pane
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);

        // Set the address bar at the top of the window
        cons.gridx = 0;
        cons.gridy = 0;
        cons.fill = GridBagConstraints.HORIZONTAL;
        add(addressBar, cons);

        // Set the editor pane to occupy the rest of the space
        cons.weighty = 1.0;
        cons.anchor = GridBagConstraints.PAGE_START;
        add(new JScrollPane(editorPane), cons);
    }

    /**
     * Retrieves the index.html file for the given domain and port.
     */
    private void retrieveIndexFile(String domain, int port) {
        String indexHtml = "";
        PrintWriter writer = null;
        BufferedReader reader = null;

        try (Socket socket = new Socket(domain, port)) {
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            sendHeader(writer, "GET /index.html HTTP/1.1");
            sendHeader(writer, "Host: " + domain);
            sendHeader(writer, "User-Agent: MyBrowser");
            sendHeader(writer, "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            sendHeader(writer, "Accept-Language: en-US,en;q=0.5");
            sendHeader(writer, "Accept-Encoding: gzip, deflate, br");
            sendHeader(writer, "Connection: keep-alive");
            sendHeader(writer, "Upgrade-Insecure-Requests: 1");
            sendHeader(writer, "Pragma: no-cache");
            sendHeader(writer, "Cache-Control: no-cache");

            writer.flush();

            String line;
            boolean headerEndFound = false;
            while ((line = reader.readLine()) != null) {
                if ("HTTP/1.1 200 OK".equalsIgnoreCase(line)) {
                    indexHtml = readBody(reader);
                    break;
                }

                if (!headerEndFound) {
                    if (line.isEmpty()) {
                        headerEndFound = true;
                    }
                } else {
                    indexHtml += line + "\n";
                }
            }
        } catch (UnknownHostException ex) {
            showErrorMessage(new Error("F600: Server not found.", ex));
        } catch (IOException ex) {
            showErrorMessage(new Error("F600: Connection refused.", ex));
        } finally {
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Display the retrieved index.html content in the JEditorPane
        ((JEditorPane) getComponentAt(1, 0)).setText(indexHtml);
    }

    /**
     * Reads the body part of the response.
     */
    private String readBody(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while (!(line = reader.readLine()).isBlank()) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    /**
     * Sends the given header value.
     */
    private void sendHeader(PrintWriter writer, String header) {
        writer.println(header);
    }

    /**
     * Displays an error dialog with the given Throwable.
     */
    private void showErrorMessage(Throwable error) {
        StringWriter sw = new StringWriter();
        error.printStackTrace(new PrintWriter(sw));
        JOptionPane.showMessageDialog(this, sw.toString(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
