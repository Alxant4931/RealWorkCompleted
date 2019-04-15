package ru.alxant.worker.model;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import ru.alxant.worker.StartClass;
import ru.alxant.worker.model.support.StartDaysWork;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Alxan on 20.02.2017.
 */
public class XLSToTable {

    private static final Logger log = Logger.getLogger(XLSToTable.class);

    public static ArrayList<String[]> formAbonTable() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(StartClass.tmpFileName);
        HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
        fileInputStream.close();

        ArrayList<String[]> strings = new ArrayList<String[]>();

        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            String[] arrayString = new String[9];
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            int count = 0;
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    arrayString[count] = cell.getStringCellValue();

                } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    arrayString[count] = cell.getNumericCellValue() + "";
                }
                count++;

            }
            strings.add(arrayString);
        }

        return strings;
    }

    public static ArrayList<String[]> formCassaTable() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(StartClass.tmpFileName);
        HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
        fileInputStream.close();

        ArrayList<String[]> strings = new ArrayList<String[]>();

        Sheet sheet = workbook.getSheetAt(1);
        Iterator<Row> rowIterator = sheet.iterator();
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        evaluator.evaluateAll();
        while (rowIterator.hasNext()) {
            String[] arrayString = new String[6];
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            int count = 0;
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    arrayString[count] = cell.getStringCellValue();
                } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    arrayString[count] = cell.getNumericCellValue() + "";
                } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                    arrayString[count] = String.valueOf(cell.getNumericCellValue());
                } else {
                    arrayString[count] = "";
                }
                count++;
            }
            strings.add(arrayString);
        }

        return strings;
    }

    private static String formStringFromCell(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            return cell.getStringCellValue();
        }
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return cell.getNumericCellValue() + " ";
        }
        return "";
    }



    public static ArrayList<String[]> formBars(String street, String adres) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("resources/АБОНЕНТЫ.XLS");
        HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
        fileInputStream.close();

        ArrayList<String[]> strings = new ArrayList<String[]>();

        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        evaluator.evaluateAll();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getCell(6) == null || row.getCell(7) == null) {
                return strings;
            }
            Cell cellAddress = row.getCell(6);
            Cell cellHome = row.getCell(7);

            String addr = cellAddress.getStringCellValue();
            if (addr.equalsIgnoreCase(street)) {
                String home = "";
                if (cellHome.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    home = String.valueOf(cellHome.getNumericCellValue());
                } else if (cellHome.getCellType() == Cell.CELL_TYPE_STRING) {
                    home = cellHome.getStringCellValue();
                }
                if (home.equalsIgnoreCase("")) {
                    continue;
                }
                if (home.equalsIgnoreCase(adres)) {
                    String[] tmp = new String[6];
                    tmp[0] = row.getCell(2).getStringCellValue() + " " + row.getCell(3).getStringCellValue();
                    tmp[1] = formStringFromCell(row.getCell(8));

                    // 14 месяцев


                    tmp[2] = formStringFromCell(row.getCell(9));
                    tmp[3] = formStringFromCell(row.getCell(10));
                    tmp[4] = formStringFromCell(row.getCell(11));
                    tmp[5] = formStringFromCell(row.getCell(12));
                    strings.add(tmp);
                    continue;
                }
            } else {
                continue;
            }

        }
        return strings;
    }


}
