package gui;

import database.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TableDisplay extends JPanel {
    private String nextScreen;

    public TableDisplay(String nextScreen) {
        this.nextScreen = nextScreen;
        String[] columns = Database.getColumnsNames("EMPLOYEES");

        Object[][] data = Database.selectEmployees(columns);

        String screen = TableDisplay.this.nextScreen;
        switch (screen) {
            case "show":
                ShowTableFrame showTableFrame = new ShowTableFrame(data, columns, 0);
                showTableFrame.setVisible(true);
                break;
            case "delete": {
                String[] modifiedOutputColumns = new String[columns.length + 1];
                System.arraycopy(columns, 0, modifiedOutputColumns, 0, columns.length);
                modifiedOutputColumns[modifiedOutputColumns.length - 1] = "CHECKED";

                DeleteRow deleteRow = new DeleteRow(data, modifiedOutputColumns);
                deleteRow.setVisible(true);

                break;
            }
            case "select": {
                String[] modifiedOutputColumns = new String[columns.length + 2];
                System.arraycopy(columns, 0, modifiedOutputColumns, 0, columns.length);
                modifiedOutputColumns[modifiedOutputColumns.length - 2] = "CHECKED";
                modifiedOutputColumns[modifiedOutputColumns.length - 1] = "SALARY";
//                modifiedOutputColumns[modifiedOutputColumns.length - 1] = "DEPARTMENT";

                SendToRow sendToRow = new SendToRow(data, modifiedOutputColumns);
                sendToRow.setVisible(true);
                break;
            }
        }
    }
}
