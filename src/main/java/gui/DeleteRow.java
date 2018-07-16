package gui;

import database.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DeleteRow extends SearchRow {
    private JButton btnDelete = new JButton("Delete");
    private JPanel deletePanel;
    ArrayList<Integer> selected;

    public DeleteRow(Object[][] data, String[] columns) {
        super(data, columns, 1);
        setCheckBoxLocation(super.getTable().getColumnCount() - 1);
        setTitle("Delete");
        deletePanel = new JPanel();
        Container container = getContentPane();
        deletePanel.add(btnDelete);
        container.add(deletePanel);

        btnDelete.setSize(100, 100);

        addListenerToDeleteButton();
    }

    private void addListenerToDeleteButton() {
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getSelectedData();
                selected = DeleteRow.super.getSelected();

                if (selected.size() == 0) {
                    JOptionPane.showConfirmDialog(null,
                            "You must select at least 1 row!", "Error", JOptionPane.DEFAULT_OPTION);
                } else {
                    int confirm = JOptionPane.showConfirmDialog(null,
                            "Are you sure you want to delete ?", "Question", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.NO_OPTION)
                        return;

                    int numberOfRows = selected.size();
                    int success = 0;
                    for (int selectedID : selected) {
                        if (Database.delete(selectedID) == 1)
                            success++;
                    }

                    initFollowUp(numberOfRows, success, "deleted");
                }
            }
        });
    }






}
