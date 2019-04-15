package ru.alxant.worker.model.support;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import ru.alxant.worker.StartClass;
import ru.alxant.worker.model.data.Cassa;
import ru.alxant.worker.view.StartDayPage;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Формирование файла на начало дня и внос стартовых параметров
 */
public class StartDaysWork {

    private static final Logger log = Logger.getLogger(StartDaysWork.class);

    /**
     * Должен создавать файл с текущей датой или идти дальше если такой файл есть
     */
    public static File createFile() throws IOException {
        StartClass.tmpFileName = "reports/" + StartClass.DATE_FORMAT.format(StartClass.CUR_DATE) + "/" + StartClass.DATE_FORMAT_DAY.format(StartClass.CUR_DATE) + "tmp.xls";
        File dir = new File("reports/" + StartClass.DATE_FORMAT.format(StartClass.CUR_DATE));
        if (dir.mkdirs()) {
            log.info("Создана новая директория " + "reports/" + StartClass.DATE_FORMAT.format(StartClass.CUR_DATE));
        }
        return new File(StartClass.tmpFileName);
    }

    /**
     * Ждем пока пользователь введет входные данные
     *
     * @param file new File
     * @throws IOException
     */
    public static void waitStartDayPage(File file) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        if (!file.isFile()) {
            workbook.write(file);
            workbook.close();

            StartDayPage startDayPage = new StartDayPage(file);
            log.info("Wait when user write 5 fields FIO, cash, t3,6,s");
            while (true) {
                if (startDayPage.startInfoOk()) {
                    break;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                    e.printStackTrace();
                }
            }
            log.info("Data Correct");

            // Проверка на адекватность проходит на StartDayPage
            Cassa newDayCassa = new Cassa(startDayPage.getCassaFio(), startDayPage.getCash(), startDayPage.getPay300(),
                    startDayPage.getPay600(), startDayPage.getServices());

            createHeadXLSFile(newDayCassa);


        }
    }

    /**
     * Создание шапки
     */
    private static SimpleDateFormat simpleDateFormat_dd_M_yyyy = new SimpleDateFormat("dd.M.yyyy");

    public static void createHeadXLSFile(Cassa cassa) throws IOException {
        log.info("createHeadXLSFile");
        HSSFWorkbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("cashbox");
        String list[] = {"№", "Улица", "Дом", "Кв", "ФИО", "АбП", "Месяц", "Услуги", "Примечание"};

        for (int i = 0; i < list.length; i++) {
            CellsWorker.writeCell(sheet, 0, i, list[i]);
        }
        for (int i = 0; i < list.length; i++) {
            sheet.autoSizeColumn(i);
        }
        //================================================================
        Sheet sheet1 = workbook.createSheet("cash");

        String list1[] = {"Остаток на начало дня", "Итого в кассе", "Выдано из кассы", "Кассир", "Сумма Абонплаты", "Пеня",
                "1-е подключение", "П/подключение", "Доп. услуги", "Объявление", "Продажа оборудования", "Итого собрано", "",
                "Сдано", "Остаток в кассе", "", "", "Квитанции начало", "Квитанции конец", "Итого"};

        for (int i = 0; i < list1.length; i++) {
            for (int j = 1; j < 5; j++) {
                CellsWorker.writeCell(sheet1, i + 1, j, "");
            }
            CellsWorker.writeCell(sheet1, i + 1, 1, list1[i]);

            if (i == 0) {
                CellsWorker.writeCell(sheet1, i + 1, 3, simpleDateFormat_dd_M_yyyy.format(new Date()));
                CellsWorker.writeCell(sheet1, i + 1, 4, cassa.getStartDaySum());
            }

            if (i == 3) {
                CellsWorker.writeCell(sheet1, i + 1, 3, cassa.getFIO());
                CellsWorker.writeCell(sheet1, i + 1, 5, 0);
                CellsWorker.writeCell(sheet1, i + 1, 6, "Зарплата кассира");
            }

            if (i == 13) {
                CellsWorker.writeCell(sheet1, i + 1, 4, 0);
            }
        }

        //====================================

        CellsWorker.writeCellFormula(sheet1, 12, 4, "SUM(E6:E12)");
        CellsWorker.writeCellFormula(sheet1, 2, 3, "SUM(E2,F4,F5,E13)");
        CellsWorker.writeCellFormula(sheet1, 15, 4, "SUM(D3-E15)");

        CellsWorker.writeCell(sheet1, 17, 3, "\"300\"");
        CellsWorker.writeCellFormula(sheet1, 20, 3, "IF(D20=D19,0,D20-D19+1)");

        CellsWorker.writeCell(sheet1, 17, 4, "\"600\"");
        CellsWorker.writeCellFormula(sheet1, 20, 4, "IF(E20=E19,0,E20-E19+1)");

        CellsWorker.writeCell(sheet1, 17, 5, "\"Услуги\"");
        CellsWorker.writeCellFormula(sheet1, 20, 5, "IF(F20=F19,0,F20-F19+1)");

        CellsWorker.writeCell(sheet1, 18, 3, cassa.getPay300());
        CellsWorker.writeCell(sheet1, 18, 4, cassa.getPay600());
        CellsWorker.writeCell(sheet1, 18, 5, cassa.getPayServices());

        CellsWorker.writeCell(sheet1, 19, 3, cassa.getPay300());
        CellsWorker.writeCell(sheet1, 19, 4, cassa.getPay600());
        CellsWorker.writeCell(sheet1, 19, 5, cassa.getPayServices());

        //=====================================

        for (int i = 0; i < list1.length; i++) {
            sheet1.autoSizeColumn(i);
        }

        FileOutputStream fileOutputStream = new FileOutputStream(StartClass.tmpFileName);
        workbook.write(fileOutputStream);
        workbook.close();
        fileOutputStream.close();
        log.info("Head create ok");
    }


}

