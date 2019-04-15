package ru.alxant.worker.model;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import ru.alxant.worker.StartClass;
import ru.alxant.worker.model.support.CellsWorker;
import ru.alxant.worker.model.support.MyCellStyle;

import javax.swing.*;
import java.io.*;

public class WriteData {

    private static final Logger log = Logger.getLogger(WriteData.class);
    private static HSSFWorkbook workbook;
    private static Sheet sheetFirstPage;
    private static Sheet sheetSecondPage;
    private static CellStyle basicStyle;
    private static Payment payment;
    private static Row rowFirstPage;

    private static HSSFWorkbook workbookSubscribers;
    private static Sheet sheetSubscribers;
    private static Row rowUserInBase;

    /**
     *
     * @param workbook книга где хранятся оплаты
     * @param workbookSubscribers книга где бд абонентов
     */
    public WriteData(HSSFWorkbook workbook, HSSFWorkbook workbookSubscribers) {
        sheetFirstPage = workbook.getSheetAt(0);
        sheetSecondPage = workbook.getSheetAt(1);
        basicStyle = MyCellStyle.basicCellStyle(workbook.createCellStyle());
        payment = WorkXLS.getPaymentStatic();
        rowFirstPage = sheetFirstPage.createRow(sheetFirstPage.getLastRowNum() + 1);
        // А если платеж не состоялся не предусмотрен возврат
        // ===========================
        sheetSubscribers = workbookSubscribers.getSheetAt(0);

        WriteData.workbook = workbook;
        WriteData.workbookSubscribers = workbookSubscribers;
        // Потом переделать желательно
        rowUserInBase = workbookSubscribers.getSheetAt(0).getRow(WorkXLS.getRowUserInBaseStatic().getRowNum());
    }

    public WriteData(HSSFWorkbook workbook) {
        sheetFirstPage = workbook.getSheetAt(0);
        sheetSecondPage = workbook.getSheetAt(1);
        basicStyle = MyCellStyle.basicCellStyle(workbook.createCellStyle());
        payment = WorkXLS.getPaymentStatic();
        rowFirstPage = sheetFirstPage.createRow(sheetFirstPage.getLastRowNum() + 1);
        // А если платеж не состоялся не предусмотрен возврат
        // ===========================
        WriteData.workbook = workbook;
    }

    /**
     * Заполняет данными первую страницу
     */
    public static void writeFirstPage() {
        // подсчет занятых строк в файле отчете за день и нахождение пустой строки
        int count = rowFirstPage.getRowNum();


        CellsWorker.writeCell(sheetFirstPage, count, 0, count, basicStyle);
        CellsWorker.writeCell(sheetFirstPage, count, 1, payment.getStreet(), basicStyle);
        CellsWorker.writeCell(sheetFirstPage, count, 2, payment.getAdres(), basicStyle);
        CellsWorker.writeCell(sheetFirstPage, count, 3, payment.getHome(), basicStyle);
        CellsWorker.writeCell(sheetFirstPage, count, 4, payment.getFirstName() + " " + payment.getSecondName(), basicStyle);
        CellsWorker.writeCell(sheetFirstPage, count, 5, payment.getAbonPay(), basicStyle);


        if (payment.getService().equalsIgnoreCase("Пеня")) {
            CellsWorker.writeCell(sheetFirstPage, count, 6, payment.getDatePayTo(), basicStyle);
            CellsWorker.writeCell(sheetFirstPage, count, 7, payment.getPena(), basicStyle);
        } else {
            CellsWorker.writeCell(sheetFirstPage, count, 6, payment.getDatePayTo(), basicStyle);
            CellsWorker.writeCell(sheetFirstPage, count, 7, payment.getPena(), basicStyle);
        }

//        CellsWorker.writeCell(sheet1FirstPage, count, 7, String.valueOf(row.getCell(7).getNumericCellValue() + payment.getUnicleSumm()), basicStyle);
        // Вроде как и не косяк
        CellsWorker.addCellValue(rowFirstPage, 7, payment.getUnicleSumm(), basicStyle);
        //------------------
        CellsWorker.writeCell(sheetFirstPage, count, 8, payment.getPrim(), basicStyle);

        for(int i=0;i<9;i++) {
            sheetFirstPage.autoSizeColumn(i);
        }


    }

    // Переделать
    public static void writeSecondPage() {
        // прибавление абонплат
        if (payment.getAbonPay() != 0) { // чтобы не плюсовало при 0-й абонплата
            Row row = sheetSecondPage.getRow(5);
            WorkXLS.payWithCount(row, payment.getAbonPay(), true);
        }
        boolean flag = false; // Определяет была ли записанна сумма по типичной ситуации или нет
        if (payment.getPena() != 0) {
            flag = WorkXLS.extraPayments(payment, sheetSecondPage, "Пеня", 6, false);
            if (!flag) {
                flag = WorkXLS.extraPayments(payment, sheetSecondPage, "Первичное подключение", 7, true);
            }
            if (!flag) {
                flag = WorkXLS.extraPayments(payment, sheetSecondPage, "Повторное подключение", 8, true);
            }
            if (!flag) {
                flag = WorkXLS.extraPayments(payment, sheetSecondPage, "Объявление", 10, true);
            }
            if (!flag) {
                flag = WorkXLS.extraPayments(payment, sheetSecondPage, "Продажа оборудования", 11, true);
            }

            // Если выдано из кассы в расход
            if (!flag) {
                flag = WorkXLS.extraPaymentsMinus(payment, sheetSecondPage, "Выдано на покупку", 3, false);
            }

            if (!flag) {
                Row row = sheetSecondPage.getRow(9);
                WorkXLS.payWithCount(row, payment.getPena(), false);
            }
        }

        // нетипичные доп услуги
        if (payment.getUnicleSumm() != 0) {
            Row row = sheetSecondPage.getRow(9);
            WorkXLS.payWithCount(row, payment.getUnicleSumm(), false);
        }

        //Формируем формулу суммы перенести
        // Итого собрано
        //====================================

        // А зачем ее каждый раз формировать
        // ===============
        Row row = sheetSecondPage.getRow(12);
        Cell sumAbCell = row.createCell(4);
        sumAbCell.setCellFormula("SUM(E6:E12)");
        //====================================

        row = sheetSecondPage.getRow(2);
        Cell cell;
        if (row.getCell(3) == null) {
            row.createCell(3);
        }
        cell = row.getCell(3);
        cell.setCellFormula("SUM(E2,F4,F5,E13)");

        row = sheetSecondPage.getRow(15);
        if (row.getCell(4) == null) {
            row.createCell(4);
        }
        row.getCell(4).setCellFormula("SUM(D3-E15)");
        // ===============
    }

    public static void writeChangeData() throws IOException {

        // Проверить головную станцию
        // Эта проверка нахрен не нужна
        if (!payment.getStreet().equalsIgnoreCase("Головная станция")) {//головная станция

            // Добавляет абоненту сумму в долг
//            sheetSubscribers.getRow(rowUserInBaseStatic.getRowNum()).getCell(9).setCellValue(sheet1.getRow(rowUserInBaseStatic.getRowNum()).getCell(9).getNumericCellValue() - paymentStatic.getAbonPay());

            // Проверить работу данной строки
            CellsWorker.addCellValue(rowUserInBase, 9, -payment.getAbonPay());
            if (
                    !(CellsWorker.getCellValue(rowUserInBase, 2).equalsIgnoreCase(payment.getFirstName())) ||
                            !(CellsWorker.getCellValue(rowUserInBase, 4).equalsIgnoreCase(payment.getPhone())) ||
                            !(CellsWorker.getCellValue(rowUserInBase, 3).equalsIgnoreCase(payment.getSecondName()))
                    ) {

                int allOk = JOptionPane.showConfirmDialog(null, "Вы вносите изменения в данные абонента, подтвердить?");
                log.info("Change user data in BD all = " + allOk);
                if (allOk == 0) {
                    // Формируем грамотно строку проверяя изменения в данных
                    String changeData = "";
                    if (!CellsWorker.getCellValue(rowUserInBase, 2).trim().equalsIgnoreCase(payment.getFirstName().trim())) {
                        changeData += CellsWorker.getCellValue(rowUserInBase, 2) + " -> " + payment.getFirstName() + " ; ";
                    }
                    if (!CellsWorker.getCellValue(rowUserInBase, 4).trim().equalsIgnoreCase(payment.getPhone().trim())) {
                        changeData += CellsWorker.getCellValue(rowUserInBase, 4) + " -> " + payment.getPhone() + " ; ";
                    }
                    if (!CellsWorker.getCellValue(rowUserInBase, 3).trim().equalsIgnoreCase(payment.getSecondName().trim())) {
                        changeData += CellsWorker.getCellValue(rowUserInBase, 3) + " -> " + payment.getSecondName() + " ; ";
                    }
                    // end
                    CellsWorker.addCellValue(rowFirstPage, 8, "Изменения/БД : " + changeData);

                    CellsWorker.writeCell(sheetSubscribers, rowUserInBase.getRowNum(), 2, payment.getFirstName());
                    CellsWorker.writeCell(sheetSubscribers, rowUserInBase.getRowNum(), 4, payment.getPhone());
                    CellsWorker.writeCell(sheetSubscribers, rowUserInBase.getRowNum(), 3, payment.getSecondName());

                }
            }
        }
        for(int i=0;i<9;i++) {
            sheetFirstPage.autoSizeColumn(i);
        }
    }

    public static void outputWriteData(String fileName, HSSFWorkbook workbook) throws IOException {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileName);
            workbook.write(fileOutputStream);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Ошибка при записи в файл");
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }



    public static Row getRowFirstPage() {
        return rowFirstPage;
    }
}
