package ru.alxant.worker.model;

import org.apache.log4j.Logger;
import ru.alxant.worker.model.support.StartDaysWork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alxant on 02.02.2017.
 */
public class Payment {
    private static final Logger log = Logger.getLogger(Payment.class);

    private String number;
    private String firstName;
    private String secondName;
    private String street;
    private String adres;
    private String home;
    private String phone;

    private int abonPay;
    private int pena;
    private String service;

    private String prim;
    private int unicleSumm;

    private String datePayTo;

    public Payment(String number, String firstName, String secondName, String street, String adres, String home, String phone, int abonPay, int pena, String service, String prim, int unicleSumm, String datePayTo) {
        this.number = number;
        this.firstName = firstName;
        this.secondName = secondName;
        this.street = street;
        this.adres = adres;
        this.home = home;
        this.phone = phone;
        this.abonPay = abonPay;
        this.pena = pena;
        this.service = service;
        this.datePayTo = datePayTo;

        this.prim = "";
        // формирование примечания
        //================================
        if (pena != 0) {
            this.prim += service;
            this.prim += " " + pena + " р.";
        }
        if (!prim.equalsIgnoreCase("")) {
            this.prim += " " + prim;
        }
        if (unicleSumm != 0) {
            this.prim += " " + unicleSumm + " р.";
        }
        //=================================
        this.unicleSumm = unicleSumm;
    }

    public  ArrayList<String[]> formAccessOut() {
        ArrayList<String[]> list = new ArrayList<>();
        listAdd("Лицевой счет", number, list);
        listAdd("Фамилия", firstName, list);
        listAdd("Инициалы", secondName, list);
        listAdd("Номер телефона", phone, list);
        listAdd("Название улицы", street, list);
        listAdd("Номер улицы", adres, list);
        listAdd("Номер квартиры", home, list);
        listAdd("Сумма к оплате", abonPay + "р.", list);
        listAdd("Месяцы оплаты", datePayTo, list);
        listAdd("Оплата услуг", pena + unicleSumm + "р.", list);
        listAdd("Примечание", (service + pena) + "р." + prim + (unicleSumm) + "р", list);
        System.out.println("AccessListSize = " + list.size());
        return list;
    }

    private void listAdd(String a, String b, ArrayList<String[]> list){
        list.add(new String[]{a, b});
    }


    @Override
    public String toString() {
        return "Провести оплату " + "\n" +
                "Номер = '" + number + '\'' +
                ", Фамилия = '" + firstName + '\'' +
                ", Инициалы = '" + secondName + '\'' + "" +
                ", Телефон = '" + phone + '\'' + "\n" +
                "Улица = '" + street + '\'' +
                ", Номер дома = '" + adres + '\'' +
                ", Квартира = '" + home + '\'' + "\n" +
                "Сумма к оплате = " + abonPay +
                ", услуга = '" + service + '\'' +
                ", стоимость услуги = " + pena + "\n" +
                "Примечание = '" + prim + '\'' +
                ", Дополнительная сумма =" + unicleSumm +
                '}';
    }

    public String getNumber() {
        return number;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getStreet() {
        return street;
    }

    public String getAdres() {
        return adres;
    }

    public String getHome() {
        return home;
    }

    public String getPhone() {
        return phone;
    }

    public int getAbonPay() {
        return abonPay;
    }

    public int getPena() {
        return pena;
    }

    public String getService() {
        return service;
    }

    public String getPrim() {
        return prim;
    }

    public int getUnicleSumm() {
        return unicleSumm;
    }

    public String getDatePayTo() {
        return datePayTo;
    }

    public void setPrim(String prim) {
        this.prim = prim;
    }
}
