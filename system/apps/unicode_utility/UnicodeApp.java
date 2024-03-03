package com.applepielinuxproject.apps.unicodeutil;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Map;

public class UnicodeApp {

    private static final String[] columns = {"Unicode", "Font Family", "Character Code", "U+Code"};
    private static JFrame frame;
    private static JComboBox<String> fontChooser;
    private static JList<Integer> charList;
    private static JTable table;
    private static DefaultTableModel model;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UnicodeApp app = new UnicodeApp();
                app.initializeGUI();
                app.frame.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void initializeGUI() throws HeadlessException {
        frame = new JFrame("Unicode Characters");
        frame.setSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout());
        contentPane.add(topPanel, BorderLayout.NORTH);

        JLabel label = new JLabel("Select a Font:");
        topPanel.add(label);

        fontChooser = new JComboBox<>();
        topPanel.add(fontChooser);
        initFontChooser();

        JButton refreshButton = new JButton("Refresh List");
        topPanel.add(refreshButton);
        refreshButton.addActionListener((ActionEvent e) -> repopulateCharList(0, Character.MAX_VALUE));

        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);

        charList = new JList<>(createCharArray(0, Character.MAX_VALUE));
        charList.setFixedCellWidth(30);
        charList.setCellRenderer(new CustomListCellRenderer());
        charList.addListSelectionListener(this::charSelected);
        scrollPane.setViewportView(charList);

        table = new JTable(model = new DefaultTableModel(null, columns)) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.getColumnModel().getColumn(2).setPreferredWidth(50);
        table.getColumnModel().getColumn(3).setPreferredWidth(50);

        JScrollPane tableScrollPane = new JScrollPane(table);
        contentPane.add(tableScrollPane, BorderLayout.SOUTH);

        frame.pack();
    }

    private void initFontChooser() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Map<String, Font> availableFonts = null;
        try {
            availableFonts = Arrays.stream(ge.getAllFont Families())
                    .collect(Collectors.toMap(Function.identity(), Function.identity()));
        } catch (HeadlessException he) {
            System.err.println("Error loading available fonts.");
        }

        fontChooser.setModel(new DefaultComboBoxModel<>(availableFonts.keySet().toArray(new String[0])));
        fontChooser.setSelectedItem(null);
    }

    private Object[] createCharArray(int minValue, int maxValue) {
        Object[] result = new Object[maxValue - minValue + 1];
        for (int i = 0; i < result.length; ++i) {
            result[i] = Integer.toString((minValue + i).unicodeChar());
        }
        return result;
    }

    private void populateCharList(int firstIndex, int lastIndex) {
        charList.setListData(createCharArray(firstIndex, lastIndex));
    }

    private void charSelected(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int selectedIndex = charList.getSelectedIndices()[0];
            updateTableCells(selectedIndex);
        }
    }

    private void updateTableCells(int index) {
        Character c = Character.toChars(index)[0];
        String uPlusCode = getUplusCode(c);
        String fontFamily = fontChooser.getItemAt(fontChooser.getSelectedIndex()).toString();
        int decimalCodePoint = (int) Character.codePointAt(new StringBuilder().append(c).toString(), 0);

        Object[] rowData = {"" + index, fontFamily, "" + decimalCodePoint, uPlusCode};
        model.fireTableRowsUpdated(model.getRowCount() - 1, model.getRowCount() - 1, null);
        model.setValueAt(rowData, model.getRowCount() - 1, 0);
        model.setValueAt(fontFamily, model.getRowCount() - 1, 1);
        model.setValueAt(decimalCodePoint, model.getRowCount() - 1, 2);
        model.setValueAt(uPlusCode, model.getRowCount() - 1, 3);
        model.addRow(rowData);
    }

    private String getUplusCode(Character c) {
        Formatter formatter = new Formatter();
        formatter.format("%#x", Character.codePointAt(new StringBuilder().append(c).toString(), 0));
        return formatter.toString();
    }
}

class CustomListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Number n) {
            setText(n.toString());
        } else {
            setText("");
            ImageIcon icon = new ImageIcon(getClass().getResource("/icons/" + Normalizer.normalForm(((String) value).substring(0, 1), Normalizer.Form.NFKD) + ".png"));
            if (icon != null) {
                setIcon(icon);
                setOpaque(false);
                setBackground(Color.TRANSPARENT);
            }
        }
        return this;
    }
}
