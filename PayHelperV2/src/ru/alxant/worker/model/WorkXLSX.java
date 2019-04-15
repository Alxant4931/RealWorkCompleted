package ru.alxant.worker.model;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.*;
import ru.alxant.worker.model.support.StartDaysWork;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Alxan on 07.02.2017.
 */
public class WorkXLSX {

    private static final Logger log = Logger.getLogger(WorkXLSX.class);

    private static int findSheet(XSSFWorkbook workbook, String adress) {
        int sheetNum = workbook.getNumberOfSheets();
        for (int i = 0; i < sheetNum; i++) {
            String resultArr[] = workbook.getSheetAt(i).getSheetName().split(" ");
            String result = "";
            if (resultArr.length == 1) {
                result = resultArr[0].split("")[1];
            } else {
                result = resultArr[1];
            }
            if (result.equalsIgnoreCase(adress)) {
                return i; // sheetNumber
            }
        }
        return -1;
    }

    private static int findRow(XSSFSheet sheet, String home) {
        int rowNumber = sheet.getPhysicalNumberOfRows();
        boolean isKvCell = false;
        int kvCell = 0;
        for (int i = 0; i < rowNumber; i++) {
            if (sheet.getRow(i) == null) {
                continue;
            }
            XSSFRow row = sheet.getRow(i);

            if (isKvCell) {
                XSSFCell cell = row.getCell(kvCell);
                if (cell.getCellType() == 1) {
                    String cellValue = cell.getStringCellValue();
                    String test = cellValue.trim().split(" ")[0];
                    if (test.equalsIgnoreCase(home)) {
                        return i;
                    }
                }
            } else {
                for (int j = 0; j < 3; j++) {
                    if (row.getCell(j) == null) {
                        continue;
                    }
                    XSSFCell cell = row.getCell(j);
                    if (cell.getCellType() == 1) {
                        String cellValue = cell.getStringCellValue();
                        if (cellValue.contains("квар") || cellValue.equalsIgnoreCase("квар") || cellValue.equalsIgnoreCase("кварт")) {
//                            System.out.println(cell);
                            isKvCell = true;
                            kvCell = j;
                            break;
                        }
                    }
                }
            }
        }
        return -1;
    }

    private static int findCell(XSSFRow row) {
        /**
         * Потом переработать для возаврата массива
         */
        int cells = row.getPhysicalNumberOfCells();
        for (int i = 0; i < cells; i++) {
            if (row.getCell(i) == null) {
                continue;
            }
            XSSFCell cell = row.getCell(i);
            if (cell.getCellType() == 0) {
                int value = (int) cell.getNumericCellValue();
//                System.out.println(value);
                if (value == 0) {
                    return i;
                }
            }
        }
        return cells+1;
    }

    private static void payToTable(XSSFWorkbook workbook, int sheetNum, int rowNum, int cellNum, String fileName, int abonPay) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);

        XSSFCellStyle style = workbook.createCellStyle();

        XSSFFont font = workbook.createFont();
        font.setColor(Font.COLOR_RED);
        font.setFontHeightInPoints((short) 14);

        style.setFont(font);

        if(workbook.getSheetAt(sheetNum).getRow(rowNum).getCell(cellNum) == null){
            workbook.getSheetAt(sheetNum).getRow(rowNum).createCell(cellNum);
        }

        workbook.getSheetAt(sheetNum).getRow(rowNum).getCell(cellNum).setCellValue(abonPay);
        workbook.getSheetAt(sheetNum).getRow(rowNum).getCell(cellNum).setCellStyle(style);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
    }

    public static void addInfoToTable(Payment payment) throws IOException {
        // а в дальнейшем надо смотреть
        String fileName = "решетки/" + payment.getStreet() + "-17.xlsx";
        fileName = fileName.replaceAll(" ", "");

        FileInputStream fileInputStream = new FileInputStream(fileName);
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        fileInputStream.close();

        int sheetNum = findSheet(workbook, payment.getAdres());
//            System.out.println(sheetNum + " sheetNum");
        if (sheetNum == -1) {
            throw new IOException();
        }

        int rowNum = findRow(workbook.getSheetAt(sheetNum), payment.getHome());
//            System.out.println(rowNum + " rowNum");
        if (rowNum == -1) {
            throw new IOException();
        }
        int cellNum = findCell(workbook.getSheetAt(sheetNum).getRow(rowNum));
//            System.out.println(cellNum + " cellNum");
        if (cellNum == -1) {
            throw new IOException();
        }

        payToTable(workbook, sheetNum, rowNum, cellNum, fileName, payment.getAbonPay());

//        workbook.close();

    }
}
