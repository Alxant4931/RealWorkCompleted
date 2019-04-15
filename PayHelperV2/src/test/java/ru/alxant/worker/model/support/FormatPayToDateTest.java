package ru.alxant.worker.model.support;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Created by Alxan on 14.03.2017.
 */
public class FormatPayToDateTest {


    @Test
    public void formatPayDate() throws Exception {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);

        int mount = calendar.get(Calendar.MONTH);
        System.out.println("year = " + year + ", mount = " + mount);

        assertEquals(String.valueOf(mount + 1), FormatPayToDate.formatPayDate(mount, mount, year, year)); // Все совпадает
        int mountStart = calendar.get(Calendar.MONTH);
        int mountEnd = calendar.get(Calendar.MONTH);

        assertEquals(String.valueOf((mountStart + 1) + " - " + (mountEnd + 2)), FormatPayToDate.formatPayDate(mountStart, mountEnd + 1, year, year)); // Месяцы 1 < 2 , годы одинаковые

        int yearStart = calendar.get(Calendar.YEAR);
        int yearEnd = calendar.get(Calendar.YEAR);
        assertEquals(String.valueOf((mount + 1) + "." + yearStart + " - " + (mount + 1) + "." + (yearEnd + 1)), FormatPayToDate.formatPayDate(mount, mount, yearStart, yearEnd + 1)); // Месяцы одинаковые , годы разные
        assertEquals(String.valueOf((mountStart + 1) + "." + yearStart + " - " + (mountEnd + 2) + "." + (yearEnd + 1)), FormatPayToDate.formatPayDate(mount, mount + 1, yearStart, yearEnd + 1)); // Месяцы разные , годы разные


    }


    @Test
    public void formMountPayTo() throws Exception {
        assertEquals("Март", FormatPayToDate.formMountPayTo(0));
        assertEquals("Апрель", FormatPayToDate.formMountPayTo(-1));
        assertEquals("Май", FormatPayToDate.formMountPayTo(-2));
        assertEquals("Январь", FormatPayToDate.formMountPayTo(-10));
        assertEquals("Февраль", FormatPayToDate.formMountPayTo(-11));


        assertEquals("Февраль", FormatPayToDate.formMountPayTo(1));
        assertEquals("Январь", FormatPayToDate.formMountPayTo(2));
        assertEquals("Декабрь", FormatPayToDate.formMountPayTo(3));
        assertEquals("Ноябрь", FormatPayToDate.formMountPayTo(4));
    }


}