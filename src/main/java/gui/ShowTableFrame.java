package gui;

import javax.swing.*;
import javax.swing.table.TableRowSorter;

public class ShowTableFrame extends MainFrame {

    private JTable table;
    private JScrollPane scrollPane;
    private TableRowSorter<MyTable> sorter;
    public ShowTableFrame(Object[][] data, String[] columns, int editableColumns) {
        MyTable model = new MyTable(data, columns, editableColumns);
        table = new JTable(model);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        setTitle("Employees");
        scrollPane = new JScrollPane(table);
        add(scrollPane);
        setSize(500, 400);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

    }

    public TableRowSorter<MyTable> getSorter() {
        return sorter;
    }

    public JTable getTable() {
        return table;
    }
}
