package ru.alxant.noprogram;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DeleteSimbols {
    public static void main(String[] args) throws IOException {
        delete("-");
    }




    private static void delete(String symbols) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("QWE.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        fileInputStream.close();
        Sheet sheet = workbook.getSheetAt(1);
        for(int i=0; i <=sheet.getLastRowNum(); i++){
            if(sheet.getRow(i).getCell(2).getCellTypeEnum() == CellType.STRING) {
                String s = sheet.getRow(i).getCell(2).getStringCellValue();
                s = s.replaceAll(symbols,"");
                String[] sArr = s.split(",");
                StringBuilder tmp = new StringBuilder();
                for(int j=0;j<sArr.length;j++){
                    sArr[j] = sArr[j].trim();
                    if(sArr[j].length()<6){
                        sArr[j] = "";
                    }
                    tmp.append(sArr[j]).append(" ");
                }
                s = tmp.toString();
                s = s.trim();
                System.out.println(s);
                sheet.getRow(i).getCell(2).setCellValue(s);
            }

        }

        FileOutputStream fileOutputStream = new FileOutputStream("QWE1.xlsx");
        workbook.write(fileOutputStream);
        fileOutputStream.close();
    }
}
