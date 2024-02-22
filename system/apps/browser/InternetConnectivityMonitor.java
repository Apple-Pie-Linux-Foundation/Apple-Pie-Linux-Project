package com.applepielinux.browser.internetconnectivitymonitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class InternetConnectivityMonitor extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_URL = "http://www.google.com";
    private static final String GOOGLE_FONT_ICON_PATH = "/images/google_font_icon_error_96x96.png";

    private JTextField addressField;
    private JSplitPane splitter;
    private JEditorPane view;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Browser frame = new Browser();
                frame.setTitle("Browser");
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setExtendedState(MAXIMIZED_BOTH);
                frame.initComponents();
                frame.validate();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Browser() throws IOException, InterruptedException {
        super();
    }

    private void initComponents() throws IOException, InterruptedException {
        setupMenuBar();
        setLayout(new BorderLayout());

        addressField = new JTextField(DEFAULT_URL);
        addressField.setPreferredSize(new Dimension(600, 32));
        addressField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    navigateToAddress(addressField.getText());
                }
            }
        });

        view = new JEditorPane();
        view.setEditable(false);

        splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(view), addressField);
        splitter.setDividerLocation(0.8);

        add(splitter, BorderLayout.CENTER);

        pack();
        setResizable(true);

        initializeListeners();

        navigateToAddress(DEFAULT_URL);
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenuItem goItem = new JMenuItem("Go");
        goItem.addActionListener(e -> navigateToAddress(addressField.getText()));
        JMenu menu = new JMenu("Options");
        menu.add(goItem);

        menuBar.add(menu);

        setJMenuBar(menuBar);
    }

    private void navigateToAddress(String address) {
        Thread thread = new Thread(() -> {
            try {
                StringBuilder buffer = new StringBuilder();
                String line;

                try (Socket socket = new Socket(address.split(":")[0], 80);
                     PrintWriter wr = new PrintWriter(socket.getOutputStream());
                     BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                    wr.println("GET /index.html HTTP/1.1");
                    wr.println("Host: " + address);
                    wr.println("Connection: close");
                    wr.println();
                    wr.flush();

                    while ((line = rd.readLine()) != null) {
                        buffer.append(line).append("\n");
                    }
                }

                SwingUtilities.invokeLater(() -> {
                    view.setText(buffer.toString());
                });
            } catch (UnknownHostException e) {
                System.out.println("Error: F600 (Server Not Found)");
            } catch (IOException e) {
                System.out.println("Error: F60C (Cannot Connect to Server)");
            }
        });

        thread.start();
    }

    private void initializeListeners() {
        final Timer timer = new Timer(5000, e -> {
            boolean connected = InternetConnectivityMonitor.getInstance().pingGoogleCom();
            if (connected) {
                hideOfflineScreen();
            } else if (!offlineContainer.isShowing()) {
                displayOfflineScreen();
            }
        });
        timer.start();
    }

    private void displayOfflineScreen() {
        if (offlineContainer == null) {
            offlineContainer = new JPanel();
            offlineContainer.setBackground(Color.WHITE);
            offlineContainer.setOpaque(true);
            offlineContainer.setBounds(0, 0, getWidth(), getHeight());

            offlineImage = new JLabel(new ImageIcon(GOOGLE_FONT_ICON_PATH));
            offlineImage.setHorizontalAlignment(SwingConstants.CENTER);
            offlineImage.setVerticalAlignment(SwingConstants.CENTER);
            offlineContainer.add(offlineImage);

            offlineMessage = new JLabel("Error: F6C9 (Not Connected To Internet)", SwingConstants.CENTER);
            offlineMessage.setForeground(new Color(249, 118, 116));
            offlineMessage.setFont(new Font("Roboto", Font.BOLD, 18));
            offlineContainer.add(offlineMessage);
        }

        SwingUtilities.invokeLater(() -> {
            offlineContainer.setVisible(true);
            validate();
            repaint();
        });
    }

    private void hideOfflineScreen() {
        SwingUtilities.invokeLater(() -> {
            if (offlineContainer != null && offlineContainer.isShowing()) {
                offlineContainer.setVisible(false);
                offlineContainer = null;
                validate();
                repaint();
            }
        });
    }

    private static class InternetConnectivityMonitor {
        private static volatile InternetConnectivityMonitor INSTANCE;

        private InternetConnectivityMonitor() {}

        public static synchronized InternetConnectivityMonitor getInstance() {
            if (INSTANCE == null) {
                INSTANCE = new InternetConnectivityMonitor();
            }
            return INSTANCE;
        }

        public boolean pingGoogleCom() {
            try {
                final URL url = new URL("https://www.google.com");
                final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(3000);
                conn.setRequestMethod("HEAD");
                final int responseCode = conn.getResponseCode();
                return responseCode >= 200 && responseCode <= 399;
            } catch (IOException ignore) {
                return false;
            }
        }
    }
}
