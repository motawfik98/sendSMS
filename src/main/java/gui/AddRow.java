package gui;

import database.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class AddRow extends MainFrame {
    private String[] columns;
    private JPanel lblTxtPanel;
    private JLabel[] lbls;
    private JTextField[] txts;
    private JButton btnOk;
    private int size;
    private Container container;
    private Object[] data;

    public AddRow() {
        setSize(500, 250);
        columns = Database.getColumnsNames("EMPLOYEES");
        size = columns.length;
        lbls = new JLabel[size];
        txts = new JTextField[size];
        btnOk = new JButton("Save");
        lblTxtPanel = new JPanel(new SpringLayout());


        for (int i = 0; i < size; i++) {
            lbls[i] = new JLabel(columns[i], JLabel.TRAILING);
            lblTxtPanel.add(lbls[i]);

            txts[i] = new JTextField(20);
            lbls[i].setLabelFor(txts[i]);

            lblTxtPanel.add(txts[i]);


        }
        SpringUtilities.makeCompactGrid(lblTxtPanel,
                size, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad


        container = getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));


        container.add(lblTxtPanel);
        container.add(btnOk);


        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setTitle("Add Row");

        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int length = columns.length;
                if (emptyField())
                    return;

                for (int i = 0; i < length; i++) {
                    if (lbls[i].getText().equals("ID")) {
                        if (wrongIntegerFormat(i, "You must enter a valid integer for the ID"))
                            return;
                        int id = Integer.parseInt(txts[i].getText());

                        if (idFoundInDatabase(id))
                            return;

                    } else if (lbls[i].getText().equals("PHONE_NUMBER")) {
                        if (wrongIntegerFormat(i, "You must insert a valid phone number"))
                            return;
                        String phoneNumber = txts[i].getText();

                        if (longOrShortPhoneNumber(phoneNumber))
                            return;
                    }
                }

                int confirm = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to add this row ?", "Question", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.NO_OPTION)
                    return;

                data = new Object[length];
                for (int i = 0; i < length; i++)
                    if (lbls[i].getText().equals("ID"))
                        data[i] = Integer.parseInt(txts[i].getText());
                    else
                        data[i] = txts[i].getText();

                int success = Database.insert("EMPLOYEES", data);
                if (success == 1) {
                    JOptionPane.showConfirmDialog(null,
                            "One row was added to the database", "Success", JOptionPane.DEFAULT_OPTION);
                    clearTextFields();
                } else {
                    JOptionPane.showConfirmDialog(null,
                            "Something went wrong with the database", "Error", JOptionPane.DEFAULT_OPTION);
                }
            }
        });

    }

    private void clearTextFields() {
        for (JTextField textField : txts)
            textField.setText("");
    }

    private boolean longOrShortPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() < 11) {
            return initFollowUpFrame("You entered a short phone number");
        } else if (phoneNumber.length() > 11) {
            return initFollowUpFrame("You entered a long phone number");
        }
        return false;
    }

    private boolean wrongIntegerFormat(int i, String message) {
        try {
            Integer.parseInt(txts[i].getText());
        } catch (NumberFormatException e2) {
            return initFollowUpFrame(message);
        }
        return false;
    }

    private boolean idFoundInDatabase(int id) {
        if (Database.getEmployeesByID(new ArrayList<>(Collections.singletonList(id))).length != 0) {
            return initFollowUpFrame("You must enter unique value for ID column");
        }
        return false;
    }

    private boolean emptyField() {
        for (int i = 0; i < columns.length; i++) {
            if (txts[i].getText().equals("")) {
                return initFollowUpFrame("You must enter values in all the fields");
            }
        }
        return false;
    }

    private boolean initFollowUpFrame(String message) {
        JOptionPane.showConfirmDialog(null, message, "Error", JOptionPane.DEFAULT_OPTION);
        return true;
    }

}
