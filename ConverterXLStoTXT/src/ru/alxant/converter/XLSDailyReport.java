package ru.alxant.converter;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alxant on 10.03.2018.
 */
public class XLSDailyReport {


    private String fileName;
    private static Workbook workbook;
    private static String reportDate;

    public static String getReportDate() {
        return reportDate;
    }

    public XLSDailyReport(String fileName) {
        this.fileName = fileName;
        workbook = checkFile(fileName);
        if (workbook == null){
            try {
                throw new NullPointerException();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        reportDate = takeAndConvertDate(getDateReport());
    }

    /**
     *
     * @param fileName Имя файла в будущем через GUI
     * @return Вернет либо книгу либо null
     */
    private Workbook checkFile(String fileName){
        FileInputStream fileInputStream = null;
        Workbook workbook = null;
        try {
            fileInputStream = new FileInputStream(fileName);
            workbook = new HSSFWorkbook(new FileInputStream(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fileInputStream!=null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return workbook;
    }


    /**
     * Конвертирует дату из одного формата в другой

     */
    private String takeAndConvertDate(String dateIn){
        String stringFormatIn = "dd.M.yyyy";
        String stringFormatOut = "dd/MM/yyyy";

        String dateOut = "01/01/1991";

        Date date;
        DateFormat format1 = new SimpleDateFormat(stringFormatIn);
        DateFormat format2 = new SimpleDateFormat(stringFormatOut);

        try {
            date = format1.parse(dateIn);
            dateOut = format2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateOut;
    }
    /**
     * Структура файла
     * Sheet = 0
     * Row Start = 1
     * Row End - 1
     * Cell 0 = № +
     * Cell 1 = Street +
     * Cell 2 = Home +
     * Cell 3 = Apartment +
     * Cell 4 = FullName +
     * Cell 5 = amount +
     * Cell 6 = mount // Не нужно
     * Cell 7 = services // Куда вставить???
     * Cell 8 = note +
     */
    public ArrayList<Receipt> createReceiptsList(){
        ArrayList<Receipt> receipts = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0);
        for (int i=1;i<sheet.getLastRowNum();i++){
            Row row = sheet.getRow(i);
            Receipt receipt = new Receipt();
            receipt.setAddress(formatAddress(row));
            String personalAccount  = XLSBDAbonent.findPersonalAccount(receipt.getAddress());
//            System.out.println(receipt.getAddress() + "   " + personalAccount);
            String fullName = cellValue(row.getCell(4));
            String amount = cellValue(row.getCell(5));
//            reportDate
            String number = cellValue(row.getCell(0));
            String note = cellValue(row.getCell(8));

            receipt.setPersonalAccount(personalAccount);
            receipt.setFullName(fullName);
            // Надо будет поправить
            receipt.setAmount(Integer.parseInt(amount.replace(".0", "")));
            receipt.setPayNumber(Integer.parseInt(number.replace(".0", "")));
            //
            receipt.setNote(note);
            receipt.setDate(reportDate);
            receipts.add(receipt);
        }
        return receipts;
    }

    private String cellValue(Cell cell){
        CellType cellType = cell.getCellTypeEnum();
        switch (cellType){
            case STRING:

                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
        }
        return "Не коректное значение";
    }

    /**
     * Создает адрес квитанции для облегчения поиска
     * @param row
     * @return
     */
    private Address formatAddress(Row row){

        String street = row.getCell(1).getStringCellValue();
        System.out.println(street);
        String home = "";
        if(row.getCell(2).getCellTypeEnum() == CellType.STRING) {
            home = row.getCell(2).getStringCellValue();
        }else {
            home = row.getCell(2).getNumericCellValue() + "";
            System.out.println(row.getCell(3).getNumericCellValue());
        }
        String apartment = "";
        if(row.getCell(3).getCellTypeEnum() == CellType.STRING) {
            apartment = row.getCell(3).getStringCellValue();
        }else {
            home = row.getCell(3).getNumericCellValue() + "";
            System.out.println(row.getCell(3).getNumericCellValue());
        }

        return new Address(street, home, apartment);
    }

    /**
     * Sheet = 1
     * Row = 1
     * Cell = 3 - Date
     * @return
     */
    private String getDateReport(){
        return workbook.getSheetAt(1).getRow(1).getCell(3).getStringCellValue(); //
    }
}
