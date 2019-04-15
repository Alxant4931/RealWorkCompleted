package ru.alxant.worker.admin;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import ru.alxant.worker.StartClass;
import ru.alxant.worker.model.WriteData;
import ru.alxant.worker.model.support.CellsWorker;
import ru.alxant.worker.model.support.CorrectParceInt;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class AdminWork {

    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(StartClass.ABONENTY_XLS);
        HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
        fileInputStream.close();
        mouthAddSumToSubscribers(workbook);
    }
    private static final Logger log = Logger.getLogger(AdminWork.class);



    /**
     * Добавляет каждому абоненту сумму в долг
     * 9 сумма
     * 12 атрибуты
     * это работает нормально заменить надо начало и конец
     */
    public static void mouthAddSumToSubscribers(HSSFWorkbook workbook) throws IOException {
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.rowIterator();
        rows.next();
        while (rows.hasNext()) {
            Row row = rows.next();
            String tmp = CellsWorker.getCellValue(row, 12);
            int attribute;
            if (tmp.equalsIgnoreCase("Пустое значение")) {
                attribute = 0;
            } else {
                attribute = CorrectParceInt.giveMeInt(tmp);
            }
            if (attribute == 0){attribute = 300;}
            CellsWorker.addCellValue(row, 9, attribute);
        }
        WriteData.outputWriteData("resources/АБОНЕНТЫ111.XLS", workbook);
    }
}
