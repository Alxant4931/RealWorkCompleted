package ru.alxant.noprogram;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Alxant on 19.02.2018.
 */
public class CutUnNumbers {
    private String fileName = "C:\\Users\\User\\OneDrive\\Документы\\IdeaProjects\\2017--\\PayHelperV2\\src\\ru\\alxant\\noprogram\\test.xlsx";
    private String fileNameOut = "C:\\Users\\User\\OneDrive\\Документы\\IdeaProjects\\2017--\\PayHelperV2\\src\\ru\\alxant\\noprogram\\test987.xlsx";
    private XSSFWorkbook workbook;

    public static void main(String[] args) {
        CutUnNumbers cutUnNumbers = new CutUnNumbers();

        try {
            cutUnNumbers.readXSSFFromFile(cutUnNumbers.fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Sheet sheet = cutUnNumbers.takeSheet(cutUnNumbers.workbook, 0);
        //cutUnNumbers.printRow(sheet, 1);
        cutUnNumbers.deleteInfo(sheet);
    }

    private void readXSSFFromFile(String fileName) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(fileName);
        workbook = new XSSFWorkbook(fileInputStream);
        fileInputStream.close();
    }

    /**
     * Взять страницу
     */
    private Sheet takeSheet(XSSFWorkbook workbook, int sheetIndex) {
        return workbook.getSheetAt(sheetIndex);
    }

    private void printRow(Sheet sheet, int rowIndex) {
        if (sheet.getLastRowNum() < rowIndex) {
            System.out.println("Закончилисть строчки");
        } else {
            System.out.println(sheet.getRow(rowIndex).getCell(6));
        }
    }
    /**
     * 6 ячейка адреса с квартирами
     * @param sheet
     */
    private void deleteInfo(Sheet sheet) {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Cell cell = sheet.getRow(i).getCell(6);
//            System.out.println(cell);

            if (cell.getCellTypeEnum() == CellType.STRING) {
                String s = "";
                s = cell.getStringCellValue();
                s = s.split(" ")[0];
                s = s.trim();
                System.out.println(s);
                cell.setCellType(CellType.BLANK);
                cell.setCellValue(s);
            }
        }

        // write Data
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileNameOut);
            workbook.write(fileOutputStream);
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
