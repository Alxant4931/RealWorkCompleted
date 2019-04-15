package ru.alxant.worker.model.support;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alxant on 20.02.2017.
 */


public class CorrectParceInt {
    private static final Logger log = Logger.getLogger(CorrectParceInt.class);

    public static int giveMeInt(String stringInt){
        if(stringInt.contains(".")) {
            stringInt = stringInt.substring(0,stringInt.indexOf("."));
        }
        if(stringInt.equals("")){return 0;}
        try {
            return Integer.parseInt(stringInt);
        }catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Обратите внимание, что вы пытаетесь ввести в виде суммы - " + stringInt);
            return 0;
        }

    }

    public static int giveMeIntRetMinus(String stringInt){
        if(stringInt.equals("")){return 0;}
        try {
            int a = Integer.parseInt(stringInt);
            a = Math.abs(a);
            a=-a;

            return a;
        }catch (NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Обратите внимание, что вы пытаетесь ввести в виде суммы - " + stringInt);
            return 0;
        }

    }

    public static boolean goodPay(String payment) {
        Pattern p = Pattern.compile("^\\d{6}");
        Matcher m = p.matcher(payment);
        return m.matches();
    }

    public static boolean onlyNum(String payment) {
        Pattern p = Pattern.compile("^\\d+");
        Matcher m = p.matcher(payment);
        return m.matches();
    }



}
