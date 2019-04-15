package ru.alxant.worker;

import org.apache.log4j.Logger;
import ru.alxant.worker.model.WorkXLS;
import ru.alxant.worker.model.support.StartDaysWork;
import ru.alxant.worker.view.PayForm;


import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Alxan on 02.02.2017.
 */
public class StartClass {
    private static final Logger log = Logger.getLogger(StartClass.class);

    public static final Date CUR_DATE = new Date();
//    public static final Date CUR_DATE = new Date(2017, 8,1);
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMMM_yy", Locale.ENGLISH);
    public static final SimpleDateFormat DATE_FORMAT_DAY = new SimpleDateFormat("dd");
    public static final String PROGRAM_VERSION = "0.8.16";
    public static final String ABONENTY_XLS = "resources/АБОНЕНТЫ.XLS";
    public static String tmpFileName;



    public static void main(String[] args) {

            log.info("Start program work");

        try {
            File myFile = StartDaysWork.createFile();
            StartDaysWork.waitStartDayPage(myFile);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Критическая ошибка обратитесь к администратору.");
            e.printStackTrace();
            log.error(e.getMessage(), e);
            System.exit(1);
        }

        try {
            new PayForm();
        }catch (Exception e){
//            JOptionPane.showMessageDialog(null, "Критическая ошибка обратитесь к администратору.");
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
    }
}
