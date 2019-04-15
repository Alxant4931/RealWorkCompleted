package ru.alxant.worker.model.support;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;

/**
 * Created by Alxan on 06.04.2017.
 */
public class CellsWorker {

    private static final Logger log = Logger.getLogger(CellsWorker.class);

    public static String getCellValue(Row row, int cellNum){
        Cell cell = row.getCell(cellNum);
        if(cell == null){
            return "Пустое значение";
        }
        if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
            return cell.getNumericCellValue()+"";
        }if(cell.getCellType() == Cell.CELL_TYPE_STRING){
            return cell.getStringCellValue();
        }
        return "Неподдерживаемый формат";
    }

    public static void writeCell(Sheet sheet, int row, int cell, Object value) {
        Row r = sheet.getRow(row);
        if (r == null) {
            r = sheet.createRow(row);
        }
        if (r.getCell(cell) == null) {
            r.createCell(cell);
        }
        if (value instanceof Integer) {
            r.getCell(cell).setCellValue((Integer) value);
        } else {
            r.getCell(cell).setCellValue((String) value);
        }
    }

    public static void createCell(Row row, int cell, Object value){
        if(row.getCell(cell) == null) {
            row.createCell(cell);
            if (value instanceof Integer) {
                row.getCell(cell).setCellValue((Integer) value);
            } else {
                row.getCell(cell).setCellValue((String) value);
            }
        }
    }

    public static void writeCell(Sheet sheet, int row, int cell, Object value, CellStyle cellStyle) {
        Row r = sheet.getRow(row);
        if (r == null) {
            r = sheet.createRow(row);
        }
        if (r.getCell(cell) == null) {
            r.createCell(cell);
        }
        if (value instanceof Integer) {
            r.getCell(cell).setCellValue((Integer) value);
        } else {
            r.getCell(cell).setCellValue((String) value);
        }
        r.getCell(cell).setCellStyle(cellStyle);

    }

    public static void writeCellFormula(Sheet sheet, int row, int cell, String value) {
        Row r = sheet.getRow(row);
        if (r == null) {
            r = sheet.createRow(row);
        }
        if (r.getCell(cell) == null) {
            r.createCell(cell);
        }
        r.getCell(cell).setCellFormula(value);
    }

    public static void addCellValue(Row row, int cell, int value) {
        createCell(row, cell, value);
        if (row.getCell(cell).getCellTypeEnum() == CellType.NUMERIC) {
            row.getCell(cell).setCellValue(row.getCell(cell).getNumericCellValue() + value);
        } else if (row.getCell(cell).getCellTypeEnum() == CellType.STRING) {
            int a;
            int b;
            if (row.getCell(cell).getStringCellValue().equalsIgnoreCase("")) {
                a = 0;
            } else {
                a = Integer.parseInt(row.getCell(cell).getStringCellValue());
            }
            b = (Integer) value;

            row.getCell(cell).setCellValue(a + b);
        } else {
            System.out.println(row.getCell(cell).getCellTypeEnum());
        }
    }

    public static void addCellValue(Row row, int cell, int value, CellStyle cellStyle) {
        addCellValue(row, cell , value);
        row.getCell(cell).setCellStyle(cellStyle);
    }

    public static void addCellValue(Row row, int cell, String value) {
        createCell(row, cell, value);
        if (row.getCell(cell).getCellTypeEnum() == CellType.NUMERIC) {
            row.getCell(cell).setCellValue(row.getCell(cell).getNumericCellValue() + value);
        } else if (row.getCell(cell).getCellTypeEnum() == CellType.STRING) {
            String a = row.getCell(cell).getStringCellValue();
            row.getCell(cell).setCellValue(a + " ; " + value);
        } else {
            System.out.println(row.getCell(cell).getCellTypeEnum());
        }
    }

    public static void setCellStyleRowCell(Row row, int cell, CellStyle cellStyle) {
        row.getCell(cell).setCellStyle(cellStyle);
    }

    public static CellStyle getCellStyleRowCell(Row row, int cell) {
        return row.getCell(cell).getCellStyle();
    }
}
