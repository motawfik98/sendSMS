package gui;

import javax.swing.table.AbstractTableModel;

public class MyTable extends AbstractTableModel {
    private String[] columns;
    private Object[][] employees;

    private int editableColumns;

    public MyTable(Object[][] employees, String[] columns, int editableColumns) {
        this.columns = columns;
        this.employees = employees;
        this.editableColumns = editableColumns;
    }

    @Override
    public int getRowCount() {
        return employees.length;
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return employees[rowIndex][columnIndex];
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (editableColumns == 0)
            return false;
        else if (editableColumns == 1)
            return columnIndex == getColumnCount() - 1;
        else // if editableColumns == 2
            return columnIndex == getColumnCount() - 1 || columnIndex == getColumnCount() - 2;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        employees[rowIndex][columnIndex] = aValue;
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}