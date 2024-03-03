import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.Objects;

public class DrakeEditor extends JFrame implements ActionListener, DocumentListener {

    private static final long serialVersionUID = -6596324328251L;
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;

    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JMenuBar menuBar;
    private JFileChooser fileChooser;
    private JButton createFileButton, openFileButton;
    private boolean isNewFile = true;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DrakeEditor().createAndShowGUI());
    }

    public void createAndShowGUI() {
        initComponents();
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void initComponents() {
        textArea = new JTextArea();
        scrollPane = new JScrollPane(textArea);
        createFileButton = new JButton("Create File");
        openFileButton = new JButton("Open File");

        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.addSeparator();
        fileMenu.add(createFileButton);
        fileMenu.add(openFileButton);
        menuBar.add(fileMenu);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(scrollPane, BorderLayout.CENTER);

        setJMenuBar(menuBar);

        createFileButton.addActionListener(this);
        openFileButton.addActionListener(this);

        textArea.getDocument().addDocumentListener(this);

        JPanel controlPanel = new JPanel();
        controlPanel.add(createFileButton);
        controlPanel.add(openFileButton);
        contentPane.add(controlPanel, BorderLayout.NORTH);

        setTitle("Drake Editor");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);

        fileChooser = new JFileChooser();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == createFileButton) {
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                saveFileAs(selectedFile);
            }
        } else if (src == openFileButton) {
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                openFile(selectedFile);
            }
        }
    }

    private void openFile(File file) {
        try {
            isNewFile = false;
            String contents = new String(Files.readAllBytes(file.toPath()));
            textArea.setText(contents);
            textArea.setCaretPosition(0);
            setTitle("Drake Editor - " + file.getName());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error opening file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveFileAs(File file) {
        if (!isNewFile && !Objects.equals(file.getAbsolutePath(), textArea.getC caretPosition().getDocument().getFileName())) {
            int result = JOptionPane.showConfirmDialog(this, "Do you want to overwrite existing file?", "Question", JOptionPane.YES_NO_OPTION);
            if (result != JOptionPane.YES_OPTION) {
                return;
            }
        }

        try {
            Files.write(file.toPath(), textArea.getText().getBytes());
            isNewFile = false;
            setTitle("Drake Editor - " + file.getName());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        handleSyntaxHighlighting();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        handleSyntaxHighlighting();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        handleSyntaxHighlighting();
    }

    private void handleSyntaxHighlighting() {
        String text = textArea.getText();
        if (isNewFile || (text.startsWith("drake function") && text.split("\\s+")[1].matches("[a-zA-Z]+"))) {
            TextRange range = new TextRange(0, textArea.getDocument().getLength());
            applySyntaxHighlighting(range);
        }
    }

    private void applySyntaxHighlighting(TextRange range) {
        String text = textArea.getText();
        try (Language langParser = Language.loadCached("lang/drake")) {
            Parser parser = new Parser();
            Node documentNode = parser.parse(new CharSequenceInput(text), langParser);
            Node selectionNode = documentNode.findDescendant(range.getStartOffset(), range.getEndOffsetInclusive());

            if (selectionNode != null && (selectionNode.kind() == NODE_TYPES.FUNCTION_DEF || selectionNode.kind() == NODE_TYPES.IDENTIFIER)) {
                Style style = getStyleForKind(NODE_TYPES.KEYWORD.getValue());
                textArea.setCharacterAttributes(range.getStartOffset(), range.length(), new SimpleAttributeSet() {{
                    setProperty(SimpleAttributeSet.CharacterAttribute.FOREGROUND, Color.decode("#1b82f7"));
                    addAttribute(style);
                }});

                style = getStyleForKind(NODE_TYPES.FUNCTION.getValue());
                textArea.setCharacterAttributes(range.getStartOffset() - 8, "drake function".length(), new SimpleAttributeSet() {{
                    setProperty(SimpleAttributeSet.CharacterAttribute.FOREGROUND, Color.decode("#db7e2c"));
                    addAttribute(style);
                }});

                style = getStyleForKind(NODE_ TYPES.IDENTIFIER.getValue());
                textArea.setCharacterAttributes(range.getStartOffset() - (isNewFile ? 0 : ("drake function ".length() + selectionNode.firstChild().text().length()) + 1), selectionNode.text().length(), new SimpleAttributeSet() {{
                    setProperty(SimpleAttributeSet.CharacterAttribute.FOREGROUND, Color.decode("#4d4744"));
                    addAttribute(style);
                }});
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Style getStyleForKind(int kind) {

        return null;
    }
}
