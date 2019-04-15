package ru.alxant.worker.model;


import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import ru.alxant.worker.StartClass;
import ru.alxant.worker.model.support.CellsWorker;
import ru.alxant.worker.model.support.CorrectParceInt;
import ru.alxant.worker.model.support.MyCellStyle;
import ru.alxant.worker.view.AcceptPay;
import ru.alxant.worker.view.StartDayPage;

import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;


/**
 * Created by Alxan on 02.02.2017.
 */
public class WorkXLS {
    private static final Logger log = Logger.getLogger(WorkXLS.class);
    private static Payment paymentStatic = null;

    public static Payment getPaymentStatic() {
        return paymentStatic;
    }

    public static Row getRowUserInBaseStatic() {
        return rowUserInBaseStatic;
    }

    // в БД
    private static Row rowUserInBaseStatic = null;
    private static HSSFWorkbook workbookStatic = null;
    // в отчете
    private static Row currentRow = null;


    public static Row findUser(String street, String home, String adres) throws IOException {
        String fileName = StartClass.ABONENTY_XLS;
        FileInputStream fileInputStream = new FileInputStream(fileName);
        HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Cell cellStreet = row.getCell(6); //
            if (cellStreet.getStringCellValue().trim().equalsIgnoreCase(street.trim())) {
                Cell cellHome = row.getCell(7);
                if (cellHome.getStringCellValue().trim().equalsIgnoreCase(home.trim())) {
                    Cell cellAdres = row.getCell(8);
                    String cAdress = cellAdres.getStringCellValue().trim().split(" ")[0];
                    if(cAdress.startsWith("0")){
                        cAdress = cAdress.replaceFirst("^0+(?!$)", "");
                    }
                    if (cAdress.equalsIgnoreCase(adres.trim())) {
                        return row;

                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            } else {
                continue;
            }
        }
        workbook.close();
        fileInputStream.close();

        return null; // плохо но временно
    }

    /**
     * Шаг 1 в платеже
     *
     * @param payment
     * @param rowUser
     * @throws IOException
     */
    public static void newPay(Payment payment, Row rowUser) throws IOException {
        rowUserInBaseStatic = rowUser;
        paymentStatic = payment;

        // __________________________________________________


        AcceptPay.getInstance(payment.formAccessOut());
    }

    /**
     * Подтверждение данных и надо убрать занос
     *
     * @param buttonOk
     * @throws IOException
     */
    public static void confirmT(boolean buttonOk) throws IOException {
        if (buttonOk) {
            // надо в payment сделать проверку на головную
            if (paymentStatic.getStreet().equalsIgnoreCase("Головная станция")) {
                HSSFWorkbook workbook;
                try (InputStream inputStream = new FileInputStream(StartClass.tmpFileName)) {
                    workbook = new HSSFWorkbook(inputStream);
                    inputStream.close();
                }

                new WriteData(workbook);
                WriteData.writeFirstPage();
                WriteData.writeSecondPage();
                WriteData.outputWriteData(StartClass.tmpFileName, workbook);
            } else {
                HSSFWorkbook workbook;
                try (InputStream inputStream = new FileInputStream(StartClass.tmpFileName)) {
                    workbook = new HSSFWorkbook(inputStream);
                    inputStream.close();
                }

                HSSFWorkbook workbookSubscribers;
                try (InputStream inputStream = new FileInputStream(StartClass.ABONENTY_XLS)) {
                    workbookSubscribers = new HSSFWorkbook(inputStream);
                    inputStream.close();
                }

                new WriteData(workbook, workbookSubscribers);// Класс внесет данные в таблицу ексель

                WriteData.writeFirstPage();
                WriteData.writeSecondPage();
                WriteData.writeChangeData();
                WriteData.outputWriteData(StartClass.tmpFileName, workbook);
                WriteData.outputWriteData(StartClass.ABONENTY_XLS, workbookSubscribers);
            }

            // Как вариан надо добавить разыменование
            JOptionPane.showMessageDialog(null, "Оплата успешно проведена!");
        } else {
            JOptionPane.showMessageDialog(null, "Оплата была отменена");
        }
    }

    public static void endDay(int cashBox, int outMoney, String text300, String text600, String textServe) throws IOException {
        InputStream inputStream = new FileInputStream(StartClass.tmpFileName);
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        inputStream.close();
        Sheet sheet = workbook.getSheetAt(1);

        CellsWorker.writeCell(sheet, 4, 5, cashBox);
        CellsWorker.writeCell(sheet, 14, 4, outMoney);
        CellsWorker.writeCell(sheet, 19, 3, text300);
        CellsWorker.writeCell(sheet, 19, 4, text600);
        CellsWorker.writeCell(sheet, 19, 5, textServe);

        // делаем перенос по словам
        Sheet sheetFirstPage = workbook.getSheetAt(0);
        Iterator<Row> rows = sheetFirstPage.rowIterator();
        while (rows.hasNext()) {
            Row row = rows.next();
            CellStyle cs = CellsWorker.getCellStyleRowCell(row, 8);
            cs.setWrapText(true);
            CellsWorker.setCellStyleRowCell(row, 8, cs);
        }

        //----------------


        //Создание формул внимание при удалении убрать счетчик count на кол-во строк
//        int lastRowNumber = sheetFirstPage.getLastRowNum();
//        for (int i = 0; i < 9; i++) {
//            CellsWorker.writeCell(sheetFirstPage, lastRowNumber + 1, i, "");
//        }
//        CellsWorker.writeCellFormula(sheetFirstPage, lastRowNumber + 1, 5, "SUM(F2:F" + (lastRowNumber + 1) + ")");
        //---------------


        FileOutputStream fileOutputStream = new FileOutputStream(StartClass.tmpFileName);
        workbook.write(fileOutputStream);
        workbook.close();
        fileOutputStream.close();
    }


    // Это надо переделать
    public static void payWithCount(Row row, int sum, boolean isCount) {
        if (isCount) {
            // Если ячейка была пуста до данные создаются в ином случае данные добавляются
            if (row.getCell(3) == null) {
                Cell countAbCell = row.createCell(3);
                countAbCell.setCellValue(0);
            } else {
                CellsWorker.addCellValue(row, 3, 1);
            }
            if (row.getCell(4) == null) {
                Cell sumAbCell = row.createCell(4);
                sumAbCell.setCellValue(sum);
            } else {
                CellsWorker.addCellValue(row, 4, sum);
            }
        } else {
            if (row.getCell(4) == null) {
                Cell sumAbCell = row.createCell(4);
                sumAbCell.setCellValue(sum);
            } else {
                CellsWorker.addCellValue(row, 4, sum);
            }
        }
    }

    public static void payWithCountMinus(Row row, int sum, boolean isCount) {
        if (isCount) {
            // Если ячейка была пуста до данные создаются в ином случае данные добавляются
            if (row.getCell(3) == null) {
                Cell countAbCell = row.createCell(3);
                countAbCell.setCellValue(0);
            } else {
                CellsWorker.addCellValue(row, 3, 1);
            }
            if (row.getCell(5) == null) {
                Cell sumAbCell = row.createCell(5);
                sumAbCell.setCellValue(sum);
            } else {
                CellsWorker.addCellValue(row, 5, sum);
            }
        } else {
            if (row.getCell(5) == null) {
                Cell sumAbCell = row.createCell(5);
                sumAbCell.setCellValue(sum);
            } else {
                CellsWorker.addCellValue(row, 5, sum);
            }
        }
    }

    /**
     * Метод определяет подходит ли слово и вносит данные в нужные строки второй страницы оплат
     *
     * @param text
     * @param row
     * @param isCount
     */
    public static boolean extraPayments(Payment payment, Sheet sheetSecPage, String text, int row, boolean isCount) {
        if (payment.getService().equalsIgnoreCase(text)) {
            payWithCount(sheetSecPage.getRow(row), payment.getPena(), isCount);
            return true;
        }
        return false;
    }

    public static boolean extraPaymentsMinus(Payment payment, Sheet sheetSecPage, String text, int row, boolean isCount) {
        if (payment.getService().equalsIgnoreCase(text)) {
            payWithCountMinus(sheetSecPage.getRow(row), CorrectParceInt.giveMeIntRetMinus(payment.getPena() + ""), isCount);
            return true;
        }
        return false;
    }


}
