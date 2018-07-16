package gui;

import database.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ColumnsToDisplay extends MainFrame {
    private String[] columns;
    private String[] outputColumns;
    private int numberOfColumns;
    private JButton btnOK;
    private JLabel[] lblColumns;
    private JCheckBox[] checBoxes;
    private JPanel mainPanel;
    private JPanel lblCheckBoxPanel;
    private String nextScreen;
    private Object[][] data;

    public ColumnsToDisplay(String nextScreen) {
        this.nextScreen = nextScreen;
        columns = Database.getColumnsNames("EMPLOYEES");
        outputColumns = null;
        numberOfColumns = columns.length;
        lblColumns = new JLabel[numberOfColumns];
        checBoxes = new JCheckBox[numberOfColumns];
        btnOK = new JButton("OK");

        mainPanel = new JPanel(new BorderLayout());
        lblCheckBoxPanel = new JPanel(new GridLayout(numberOfColumns, 2));
        for (int i = 0; i < numberOfColumns; i++) {
            lblColumns[i] = new JLabel(columns[i]);
            lblColumns[i].setHorizontalAlignment(SwingConstants.CENTER);
            checBoxes[i] = new JCheckBox();
            lblCheckBoxPanel.add(lblColumns[i]);
            lblCheckBoxPanel.add(checBoxes[i]);
        }

        setTitle("Select Columns");
        add(mainPanel);

        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int numberOfChecks = 0;
                ArrayList<String> checked = new ArrayList<>();
                for (int i = 0; i < numberOfColumns; i++)
                    if (checBoxes[i].isSelected()) {
                        checked.add(columns[i]);
                        numberOfChecks++;
                    }
                int size = checked.size();
                outputColumns = new String[size];

                for (int i = 0; i < size; i++)
                    outputColumns[i] = checked.remove(0);

                if (numberOfChecks < 2) {
                    FollowUpFrame followUpFrame = new FollowUpFrame("Error", "You must select at least 2 fields");
                    followUpFrame.setVisible(true);
                    return;
                }

                data = Database.selectEmployees(outputColumns);
                ColumnsToDisplay.this.setVisible(false);

                String screen = ColumnsToDisplay.this.nextScreen;
                switch (screen) {
                    case "show":
                        ShowTableFrame showTableFrame = new ShowTableFrame(data, outputColumns, 0);
                        showTableFrame.setVisible(true);
                        break;
                    case "delete": {
                        String[] modifiedOutputColumns = new String[outputColumns.length + 1];
                        System.arraycopy(outputColumns, 0, modifiedOutputColumns, 0, outputColumns.length);
                        modifiedOutputColumns[modifiedOutputColumns.length - 1] = "CHECKED";

                        DeleteRow deleteRow = new DeleteRow(data, modifiedOutputColumns);
                        deleteRow.setVisible(true);

                        break;
                    }
                    case "select": {
                        String[] modifiedOutputColumns = new String[outputColumns.length + 3];
                        System.arraycopy(outputColumns, 0, modifiedOutputColumns, 0, outputColumns.length);
                        modifiedOutputColumns[modifiedOutputColumns.length - 3] = "CHECKED";
                        modifiedOutputColumns[modifiedOutputColumns.length - 2] = "SALARY";
                        modifiedOutputColumns[modifiedOutputColumns.length - 1] = "DEPARTMENT";

                        SendToRow sendToRow = new SendToRow(data, modifiedOutputColumns);
                        sendToRow.setVisible(true);
                        break;
                    }
                }

            }
        });

        mainPanel.add(lblCheckBoxPanel);
        mainPanel.add(btnOK, BorderLayout.SOUTH);

        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }
}
