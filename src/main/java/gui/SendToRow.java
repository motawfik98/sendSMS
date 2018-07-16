package gui;

import database.Database;
import main.Employee;
import restapi.ApacheHttpPost;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SendToRow extends SearchRow {
    private Container container;
    private JButton btnSend;
    private JButton btnSelectAll;
    private JButton btnDeselectAll;
    private JPanel btnPanel;
    private Employee[] employees;
    private ArrayList<Integer> selected;
    private ArrayList<Integer> rowNumbers;


    public SendToRow(Object[][] data, String[] columns) {
        super(data, columns, 2);
        setCheckBoxLocation(super.getTable().getModel().getColumnCount() - 2);
        container = this.getContentPane();
        setTitle("Send to");

        btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));

        btnSend = new JButton("Send");
        btnPanel.add(btnSend);

        btnPanel.add(Box.createRigidArea(new Dimension(20, 0)));

        btnSelectAll = new JButton("Select All");
        btnPanel.add(btnSelectAll);

        btnPanel.add(Box.createRigidArea(new Dimension(20, 0)));

        btnDeselectAll = new JButton("Deselect All");
        btnDeselectAll.setSize(100, 100);
        btnPanel.add(btnDeselectAll);

        btnSend.setSize(100, 100);
        btnSelectAll.setSize(100, 100);

        container.add(btnPanel);

        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getSelectedData();
                selected = SendToRow.super.getSelected();
                rowNumbers = SendToRow.super.getRowNumbers();
                if (selected.size() == 0) {
                    JOptionPane.showConfirmDialog(null,
                            "You must select at least one row", "Error", JOptionPane.DEFAULT_OPTION);
                } else {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "Are you sure you want to send ?", "Question", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.NO_OPTION)
                        return;

                    int numberOfRows = selected.size();
                    employees = Database.getEmployeesByID(selected);
                    String[] salaries = new String[rowNumbers.size()];
                    if (!getSalary(salaries))
                        return;

                    ApacheHttpPost postRequest = new ApacheHttpPost(employees, salaries);
                    postRequest.sendMessages();
                    JOptionPane.showConfirmDialog(null, postRequest.getOutput(),
                            "Summary", JOptionPane.DEFAULT_OPTION);

//                    SendTwilioSMS sendTwilioSMS = new SendTwilioSMS(employees, salaries);
//                    int success = sendTwilioSMS.sendMessage();
//                    initFollowUp(numberOfRows, success, "sent");

                }
            }
        });

        btnSelectAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setCheckBoxesValue(Boolean.TRUE);
            }
        });

        btnDeselectAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setCheckBoxesValue(Boolean.FALSE);
            }
        });


    }

    private boolean getSalary(String[] salaries) {
        for (int i = 0; i < salaries.length; i++) {
            int row = rowNumbers.get(i);
            try {

                JTable table = super.getTable();
                Object salary = table.getValueAt(row, table.getModel().getColumnCount() - 1);
//                Object department = table.getValueAt(row, table.getModel().getColumnCount() - 1);
                int intSalary = Integer.parseInt(String.valueOf(salary));
//                String strDepartment = String.valueOf(department);
//                salaries[i] = new String[2];
                salaries[i] = String.valueOf(intSalary);
//                salaries[i][1] = strDepartment;
            } catch (NumberFormatException e) {
                JOptionPane.showConfirmDialog(null,
                        "You must enter a valid salary in the row number " + (row + 1),
                        "Error", JOptionPane.DEFAULT_OPTION);
                return false;
            }
        }
        return true;
    }

    private void setCheckBoxesValue(boolean value) {
        JTable baseTable = SendToRow.super.getTable();
        for (int i = 0; i < baseTable.getRowCount(); i++) {
            baseTable.getModel().setValueAt(value, i, getCheckBoxLocation());
        }
    }
}