package gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;

public class SearchRow extends ShowTableFrame {
    private JLabel lblFilter;
    private JTextField txtFilter;
    private JPanel filterPanel;
    private ArrayList<Integer> selected;
    private ArrayList<Integer> rowNumbers;
    private int checkBoxLocation;

    public SearchRow(Object[][] data, String[] columns, int editableColumns) {
        super(data, columns, editableColumns);
        Container container = getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.X_AXIS));
        lblFilter = new JLabel("Name: ");

        txtFilter = new JTextField();
        txtFilter.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                newFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                newFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                newFilter();
            }
        });

        filterPanel.add(lblFilter);
        filterPanel.add(txtFilter);

        container.add(filterPanel);
    }

    private void newFilter() {
        RowFilter<MyTable, Object> rowFilter;
        try {
            rowFilter = RowFilter.regexFilter("(?i)" + txtFilter.getText(), 2);
        } catch (PatternSyntaxException e) {
            return;
        }
        super.getSorter().setRowFilter(rowFilter);
    }

    public void getSelectedData() {
        selected = new ArrayList<>();
        rowNumbers = new ArrayList<>();
        JTable baseTable = super.getTable();
        for (int i = 0; i < baseTable.getRowCount(); i++) {
            Boolean isSelected = Boolean.valueOf(baseTable.getValueAt(i, checkBoxLocation).toString());
            if (isSelected) {
                int rowID = Integer.parseInt(baseTable.getValueAt(i, 0).toString());
                selected.add(rowID);
                rowNumbers.add(i);
            }
        }
    }

    public void initFollowUp(int numberOfRows, int success, String operation) {
        if (success != numberOfRows) {
            JOptionPane.showConfirmDialog(null,
                    success + " out of " + numberOfRows + " were " + operation, "Error", JOptionPane.DEFAULT_OPTION);
        } else {
            JOptionPane.showConfirmDialog(null,
                    success + " out of " + numberOfRows + " were " + operation, "Success", JOptionPane.DEFAULT_OPTION);
        }
    }

    public ArrayList<Integer> getSelected() {
        return selected;
    }

    public ArrayList<Integer> getRowNumbers() {
        return rowNumbers;
    }

    public void setCheckBoxLocation(int checkBoxLocation) {
        this.checkBoxLocation = checkBoxLocation;
    }

    public int getCheckBoxLocation() {
        return checkBoxLocation;
    }
}