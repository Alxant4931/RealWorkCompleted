package ru.alxant.worker.model.support;

import org.apache.log4j.Logger;
import ru.alxant.worker.model.exeption.BadDateException;
import ru.alxant.worker.model.exeption.BadYearException;

import java.util.Calendar;
import java.util.Date;
import java.util.IllegalFormatException;

/**
 * Created by Alxan on 26.02.2017.
 */
public class FormatPayToDate {
    private static final Logger log = Logger.getLogger(FormatPayToDate.class);

    public static String formatPayDate(int startDate, int endDate, int startYear, int endYear) throws BadYearException, BadDateException {
        startDate = startDate + 1;
        endDate = endDate + 1;

        Calendar calendar = Calendar.getInstance();
        String result;

        if (endDate == startDate && startYear == endYear) {
            return String.valueOf(startDate);
        }else
        if (endDate != startDate && startYear == endYear) {
            if (endDate > startDate) {
                return startDate + " - " + endDate;
            }else {
                throw new BadDateException();
            }
        }else
        if(endDate == startDate && startYear != endYear){
            if(startYear>endYear){
                throw new BadYearException();
            }
            return startDate + "." +startYear + " - " + endDate + "." + endYear;
        }else
        if(endDate != startDate && startYear != endYear){
            if(startYear>endYear){
                throw new BadYearException();
            }
            return startDate + "." +startYear + " - " + endDate + "." + endYear;
        }

        return "";
    }

    public static String formMountPayTo(int mountDiff) {
        String[] mount = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
        Calendar calendar = Calendar.getInstance();
        int m = calendar.get(Calendar.MONTH);

        if (m - mountDiff < 0) {
            m += 12;
        }
        return mount[(m - mountDiff) % 12];
    }


}
