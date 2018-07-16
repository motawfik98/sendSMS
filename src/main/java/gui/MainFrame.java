package gui;

import database.Database;
import main.Employee;
import readFrom.ReadFromExcel;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private JMenuBar menuBar = new JMenuBar();
    private JMenu mnuEdit = new JMenu("Edit");
    private JMenu mnuShow = new JMenu("Show");
    private JMenu mnuFile = new JMenu("File");
    private JMenuItem addEmployee = new JMenuItem("Add Employee");
    private JMenuItem getEmployees = new JMenuItem("Show Employees");
    private JMenuItem deleteEmployee = new JMenuItem("Delete Employee");
    private JMenuItem selectEmployee = new JMenuItem("Select Employee");
    private JMenuItem openExcel = new JMenuItem("Import Table...");
    private JMenuItem sendFromExcel = new JMenuItem("Send...");
    private JMenuItem addEmptyTable = new JMenuItem("Add Empty Table...");
    private File file;
    private String tableName;
    private String[] columns;
    private Object[][] data;


    public MainFrame() {
        init();
    }

    private void init() {
        setTitle("Main Page");
        setLocation(500, 150);
        setSize(500, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        addEmployee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddRow addRow = new AddRow();
                addRow.setVisible(true);
            }
        });

        getEmployees.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableDisplay tableDisplay = new TableDisplay("show");
                MainFrame.this.setContentPane(tableDisplay);
            }
        });

        deleteEmployee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableDisplay tableDisplay = new TableDisplay("delete");
                MainFrame.this.setContentPane(tableDisplay);
            }
        });

        selectEmployee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableDisplay tableDisplay = new TableDisplay("select");
                MainFrame.this.setContentPane(tableDisplay);
            }
        });

        openExcel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getSelectedFile();
                try {
                    getTableInfo();
                    createEmptyTable();
                    Database.fillTable(tableName, data, columns);

                } catch (NullPointerException ignored) {

                }
            }
        });
        addEmptyTable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getSelectedFile();
                getTableInfo();
                createEmptyTable();
            }
        });

        sendFromExcel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getSelectedFile();
                try {
                    ReadFromExcel readFromExcel = new ReadFromExcel(file.getPath());
                    ArrayList<Integer> ids = readFromExcel.getCellsAtColumn(0);
                    Employee[] employees = Database.getEmployeesByID(ids);
                    ArrayList<Integer> salariesList = readFromExcel.getCellsAtColumn(1);
                    String[] salaries = new String[salariesList.size()];
                    for (int i = 0; i < salaries.length; i++)
                        salaries[i] = String.valueOf(salariesList.get(i));



                    Object[][] data = new Object[employees.length][];
                    for (int i = 0; i < data.length; i++) {
                        data[i] = new Object[]{employees[i].getID(), employees[i].getTitle(),
                                employees[i].getName(), employees[i].getPhoneNumber(),  salaries[i]};

                    }

                    String[] columns = {"ID", "Title", "Name", "Phone_Number", "Salary"};

                    VerifySend verifySend = new VerifySend(data, columns, employees, salaries);
                    verifySend.setVisible(true);


                } catch (NullPointerException ignored) {
                    System.out.println("hello");
                }
            }
        });


        mnuEdit.add(addEmployee);
        mnuEdit.add(deleteEmployee);
        mnuShow.add(getEmployees);
        mnuShow.add(selectEmployee);
        mnuFile.add(openExcel);
        mnuFile.add(sendFromExcel);
        mnuFile.add(addEmptyTable);

        menuBar.add(mnuFile);
        menuBar.add(mnuEdit);
        menuBar.add(mnuShow);

        setJMenuBar(menuBar);

    }

    private void createEmptyTable() {
        Database.drop(tableName);
        Database.createTable(tableName, columns);
    }

    private void getTableInfo() {
        ReadFromExcel readFromExcel = new ReadFromExcel(file.getPath());
        tableName = readFromExcel.getSheetName();
        columns = readFromExcel.getColumnNames();
        data = readFromExcel.getTableInfo();
    }


    private void getSelectedFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(MainFrame.this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            FileFilter filter = new FileNameExtensionFilter("Excel file", "xls", "xlsx");
            fileChooser.setFileFilter(filter);
            try {
                file = fileChooser.getSelectedFile();
            } catch (NullPointerException e1) {
                JOptionPane.showConfirmDialog(null,
                        "You must select Excel file", "Error", JOptionPane.DEFAULT_OPTION);
                file = null;
            }
        }
    }


}

