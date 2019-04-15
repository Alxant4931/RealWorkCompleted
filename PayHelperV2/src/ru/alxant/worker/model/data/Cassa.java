package ru.alxant.worker.model.data;

import org.apache.log4j.Logger;
import ru.alxant.worker.model.support.StartDaysWork;

/**
 * В данном классе хранятся данные на старт дня которые должен ввести кассир
 */

public class Cassa {
    private static final Logger log = Logger.getLogger(Cassa.class);

    private String FIO;
    private int startDaySum;
    private String pay300;
    private String pay600;
    private String payServices;

    public Cassa(String FIO, int startDaySum, String pay300, String pay600, String payServices) {
        this.FIO = FIO;
        this.startDaySum = startDaySum;
        this.pay300 = pay300;
        this.pay600 = pay600;
        this.payServices = payServices;
    }


    public String getFIO() {
        return FIO;
    }

    public int getStartDaySum() {
        return startDaySum;
    }

    public String getPay300() {
        return pay300;
    }

    public String getPay600() {
        return pay600;
    }

    public String getPayServices() {
        return payServices;
    }
}
