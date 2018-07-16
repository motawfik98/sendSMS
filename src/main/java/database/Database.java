package database;

import main.Employee;

import java.sql.*;
import java.util.ArrayList;

public abstract class Database {
    public static final String EMPLOYEES_TABLE = "EMPLOYEES";
    public static final String MESSAGES_TABLE = "MESSAGES";

    public static final String ID_COLUMN = "ID";
    private static final String TITLE_COLUMN = "TITLE";
    public static final String NAME_COLUMN = "Name";
    public static final String PHONE_NUMBER_COLUMN = "Phone_Number";

    public static final String DATE_COLUMN = "DATEREGISTERED";
    public static final String MESSAGE_COLUMN = "MESSAGE";
    public static final String SELECTED_COLUMN = "SELECTED";
    public static final String SALARY = "Salary";

    private static Connection connection;
    private static Statement statement;

    public static void getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
            connection.setAutoCommit(false);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public static String[] getColumnsNames(String name) {
        getConnection();
        String[] columns = null;
        try {
            statement = connection.createStatement();
            String sql = "SELECT * FROM " + name;
            ResultSet resultSet = statement.executeQuery(sql);

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            columns = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                String columnName = metaData.getColumnName(i + 1);
                columns[i] = columnName;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        close();
        return columns;
    }


    public static void createTable(String tableName, String[] columns) {
        try {
            getConnection();
            statement = connection.createStatement();
            StringBuilder sql = new StringBuilder();
            sql.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (");
            for (int i = 0; i < columns.length; i++) {
                sql.append(columns[i]).append(" ");
                if (i == 0)
                    sql.append("INTEGER PRIMARY KEY , ");
                else
                    sql.append("TEXT, ");
            }
            sql.replace(sql.length() - 2, sql.length(), "");
            sql.append(");");

            statement.executeUpdate(sql.toString());
            statement.close();
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        } finally {
            close();
        }
        System.out.println("Table created successfully");
    }

    public static void fillTable(String tableName, Object[][] values, String[] columns) {
        try {
            getConnection();
            statement = connection.createStatement();
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO ").append(tableName).append(" (");
            for (int i = 0; i < columns.length; i++) {
                sql.append(columns[i]).append(",");
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(") VALUES ");
            for (int i = 0; i < values.length; i++) {
                sql.append("(");
                for (int j = 0; j < values[i].length; j++) {
                    if (values[i][j] instanceof Integer)
                        sql.append(values[i][j]).append(",");
                    else
                        sql.append("\'").append(values[i][j]).append("\',");
                }
                sql.deleteCharAt(sql.length() - 1);
                sql.append("),");
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(";");
            statement.executeUpdate(sql.toString());
            statement.close();
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
            System.out.println("Added");
        }
    }

    public static int insert(String tableName, Object[] data) {
        String[] columns = getColumnsNames(tableName);
        StringBuilder sql = new StringBuilder();
        try {
            getConnection();
            statement = connection.createStatement();

            sql.append("INSERT INTO ").append(tableName).append(" ( ");
            for (String column : columns)
                sql.append(column).append(",");

            sql.deleteCharAt(sql.length() - 1);
            sql.append(") VALUES (");

            for (Object pieceOfData : data)
                if (pieceOfData instanceof Integer)
                    sql.append(pieceOfData).append(",");
                else
                    sql.append("\'").append(pieceOfData).append("\',");

            sql.deleteCharAt(sql.length() - 1);
            sql.append(");");

            statement.executeUpdate(sql.toString());
            statement.close();
            connection.commit();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            close();
        }
        return 1;
    }


    public static Object[][] selectEmployees(String[] columns) {
        ArrayList<Object[]> employees = select(columns);
        if (employees == null)
            return null;

        Object[][] data = new Object[employees.size()][];

        for (int i = 0; i < data.length; i++)
            data[i] = employees.remove(0);

        return data;
    }

    private static ArrayList<Object[]> select(String[] columns) {
        getConnection();
        ArrayList<Object[]> employees = new ArrayList<>();
        ArrayList<String> employee = new ArrayList<>();
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + Database.EMPLOYEES_TABLE + " ;");
            while (resultSet.next()) {
                for (String column : columns) {
//                    String value = resultSet.getString(columns[i]);
                    employee.add(resultSet.getString(column));
                }

                int size = employee.size();
                Object[] employeeArr = new Object[size + 3];
                for (int j = 0; j < size; j++) {
                    employeeArr[j] = employee.remove(0);
                }
                employeeArr[size] = Boolean.FALSE;
                employeeArr[size + 1] = "";
//                employeeArr[size + 2] = "";

                employees.add(employeeArr);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
//            System.err.println(e.getClass().getName() + ": " + e.getMessage());
//            System.exit(0);
            return null;
        } finally {
            close();
        }
//        System.out.println("Select done successfully");
        return employees;
    }


    public static Employee[] getEmployeesByID(ArrayList<Integer> ids) {
        getConnection();
        ArrayList<Employee> phoneNumbersAndName = new ArrayList<>();
        Employee[] employees = new Employee[0];
        try {
            for (int id : ids) {
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT ID, "
                        + TITLE_COLUMN + ", "
                        + NAME_COLUMN + ", "
                        + PHONE_NUMBER_COLUMN + " FROM EMPLOYEES WHERE "
                        + ID_COLUMN + " = \'" + id + "\';");
                while (resultSet.next()) {
                    String currentID = resultSet.getString(ID_COLUMN);
                    String title = resultSet.getString(TITLE_COLUMN);
                    String name = resultSet.getString(NAME_COLUMN);
                    String phoneNumber = resultSet.getString(PHONE_NUMBER_COLUMN);
                    Employee e = new Employee(Integer.parseInt(currentID),title, name, phoneNumber);
                    phoneNumbersAndName.add(e);
                }
            }
            int size = phoneNumbersAndName.size();
            employees = new Employee[size];
            for (int i = 0; i < size; i++)
                employees[i] = phoneNumbersAndName.remove(0);


        } catch (SQLException e) {
//            e.printStackTrace();
        } finally {
            close();
        }
        return employees;
    }


    public static void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }


    public static int delete(int id) {
        getConnection();
        try {
            statement = connection.createStatement();
            String sql = "DELETE from EMPLOYEES where " + ID_COLUMN + " = \'" + id + "\';";
            statement.executeUpdate(sql);
            connection.commit();
        } catch (SQLException e) {
//            System.err.println(e.getClass().getName() + ": " + e.getMessage());
//            System.exit(0);
            return 0;
        } finally {
            close();
        }
//        System.out.println("Delete done successfully");
        return 1;
    }

    public static void drop(String tableName) {
        try {
            getConnection();
            statement = connection.createStatement();
            String sql = "DROP TABLE IF EXISTS " + tableName + ";";
            statement.executeUpdate(sql);
            statement.close();
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Drop done successfully");
    }
}

