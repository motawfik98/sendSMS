package readFrom;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ReadFromExcel {
    private String SAMPLE_XLSX_FILE_PATH = "example.xlsx";
    private Workbook workbook;
    private Sheet sheet;

    public ReadFromExcel(String SAMPLE_XLSX_FILE_PATH) {
        this.SAMPLE_XLSX_FILE_PATH = SAMPLE_XLSX_FILE_PATH;
        initWorkbook();
    }

    public void initWorkbook() {
        try {
            workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
//        System.out.println("Workbook has: " + workbook.getNumberOfSheets() + " sheets.");
    }

    public void printEachSheet() {
        for (Sheet aSheet : workbook)
            System.out.println(aSheet.getSheetName());

    }

    public void getSheetAt(int position) {
        sheet = workbook.getSheetAt(position);
        DataFormatter dataFormatter = new DataFormatter();
        for (Row row : sheet) {
            for (Cell cell : row) {
                String cellValue = dataFormatter.formatCellValue(cell);
                System.out.print(cellValue + "\t\t");
            }
            System.out.println();
        }
    }


    public ArrayList<Integer> getCellsAtColumn(int column) {
        ArrayList<Integer> values = new ArrayList<>();
        this.sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        int i = 0;
        for (Row row : this.sheet) {
            if (i == 0) {
                i++;
                continue;
            }
            Cell cell = row.getCell(column);
            String cellValue = dataFormatter.formatCellValue(cell);
            if (!cellValue.equals(""))
                values.add(Integer.parseInt(cellValue));
            i++;
        }
//        String[] stringValues = new String[values.size()];
//        stringValues = values.toArray(stringValues);
//        return stringValues;
        return values;
    }

    public String[][] getCellsAtColumns(int[] columns) {
        ArrayList<String[]> values = new ArrayList<>();
        sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();

        int j = 0;
        for (Row row : sheet) {
            if (j == 0) {
                j++;
                continue;
            }
            String[] stringValues = new String[columns.length];
            for (int i = 0; i < columns.length; i++) {
                Cell cell = row.getCell(columns[i]);
                String cellValue = dataFormatter.formatCellValue(cell);
                if (!cellValue.equals(""))
                    stringValues[i] = cellValue;
            }
            if (!allNull(stringValues))
                values.add(stringValues);
            j++;
        }
        String[][] finalValues = new String[values.size()][];
        int size = values.size();
        for (int i = 0; i < size; i++)
            finalValues[i] = values.remove(0);
        return finalValues;
    }

    public String[] getColumnNames() {
        ArrayList<String> columns = new ArrayList<>();
        sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();

        Row firstRow = sheet.getRow(0);

        Iterator<Cell> cellIterator = firstRow.cellIterator();
        while (cellIterator.hasNext()) {
            columns.add(dataFormatter.formatCellValue(cellIterator.next()));
        }
        int size = columns.size();
        String[] stringCols = new String[size];
        for (int i = 0; i < size; i++)
            stringCols[i] = columns.remove(0).replace(" ", "_");

        return stringCols;
    }

    private boolean allNull(String[] values) {
        for (String value : values)
            if (value != null)
                return false;
        return true;
    }


    public Object[][] getTableInfo() {
        ArrayList<Object[]> data = new ArrayList<>();
        String[] columns = getColumnNames();
        sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();

        int j = 0;
        for (Row row : sheet) {
            if (j == 0) {
                j++;
                continue;
            }
            String[] stringValues = new String[columns.length];
            for (int i = 0; i < columns.length; i++) {
                Cell cell = row.getCell(i);
                String cellValue = dataFormatter.formatCellValue(cell);
                if (!cellValue.equals(""))
                    stringValues[i] = cellValue;
            }
            if (!allNull(stringValues))
                data.add(stringValues);
            j++;
        }
        int size = data.size();
        Object[][] finalData = new Object[size][];
        for (int i = 0; i < size; i++) {
            finalData[i] = data.remove(0);
        }
        return finalData;
    }

    public String getSheetName() {
        return workbook.getSheetAt(0).getSheetName().toUpperCase();
    }
}
