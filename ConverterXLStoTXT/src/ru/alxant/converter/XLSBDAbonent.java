package ru.alxant.converter;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Alxant on 10.03.2018.
 */
public class XLSBDAbonent {
    private String fileName;
    private static Workbook workbook;

    public XLSBDAbonent(String fileName) {
        this.fileName = fileName;
        workbook = createWorkBook();
    }

    private Workbook createWorkBook(){
        Workbook workbook = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            workbook = new HSSFWorkbook(fileInputStream);

            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workbook;
    }

    /**
     * Cell 6 = Address
     * Cell 7 = Home
     * Cell 8 = Apartment
     */
    public static String findPersonalAccount(Address address){
        Sheet sheet = workbook.getSheetAt(0);
        for (int i=0;i<sheet.getLastRowNum()+1;i++){
            String addressCell = sheet.getRow(i).getCell(6).getStringCellValue();
            String homeCell = sheet.getRow(i).getCell(7).getStringCellValue();
            String apartmentCell = sheet.getRow(i).getCell(8).getStringCellValue().split(" ")[0];
            apartmentCell = apartmentCell.replaceFirst("^0+(?!$)", "");

            if(addressCell.equalsIgnoreCase(address.getStreet())){
                if(homeCell.equalsIgnoreCase(address.getHome())){
                    if(apartmentCell.equalsIgnoreCase(address.getApartment())){
                        if(sheet.getRow(i).getCell(0).getCellTypeEnum() == CellType.STRING) {
                            return sheet.getRow(i).getCell(0).getStringCellValue();
                        }if (sheet.getRow(i).getCell(0).getCellTypeEnum() == CellType.NUMERIC){
                            int number = (int) sheet.getRow(i).getCell(0).getNumericCellValue();
                            return String.valueOf(number).trim();
                        }else {
                            return "ЛИЦЕВОЙ_СЧЕТ_НЕ_СТРАННЫЙ";
                        }
                    }
                }
            }

        }
        return "ЛИЦЕВОЙ_СЧЕТ_НЕ_НАЙДЕН";
    }



}
