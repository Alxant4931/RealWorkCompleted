package ru.alxant.worker.control;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import ru.alxant.worker.StartClass;
import ru.alxant.worker.model.*;
import ru.alxant.worker.model.exeption.BadDateException;
import ru.alxant.worker.model.exeption.BadYearException;
import ru.alxant.worker.model.support.CellsWorker;
import ru.alxant.worker.model.support.CorrectParceInt;
import ru.alxant.worker.model.support.FormatPayToDate;
import ru.alxant.worker.view.PayForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Alxan on 02.02.2017.
 */
public class PayButton {
    private static final Logger log = Logger.getLogger(PayButton.class);
    private static Row rowUserInBase = null;  // Строка в которой лежит адресс АБОНЕНТЫ.XLS


    public PayButton(final JButton payButton, JButton searchButton, JButton cashOutButton, final PayForm payForm) {
        pay(payButton, payForm, rowUserInBase);
        search(searchButton, payForm);
        cashOutButton(cashOutButton, payForm);
    }

    private void search(final JButton searchButton, final PayForm payForm) {

        searchButton.addActionListener(
                e -> {
//                    log.info(searchButton.getName() + " keyClicked");
                    searchB(payForm);
                });

        searchButton.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {

            }

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//                    log.info(searchButton.getName() + " keyPressed");
                    searchB(payForm);

                }
            }

            public void keyReleased(KeyEvent e) {

            }
        });
    }

    private void searchB(final PayForm payForm) {
        rowUserInBase = null;
        String street = (String) payForm.getStreetBox().getSelectedItem();
        if (street.equalsIgnoreCase("Головная станция")) {
            payForm.getPersonalAccount().setText("");
            payForm.getFirstName().setText("");
            payForm.getSecondName().setText("");
            payForm.getPhone().setText("");
            payForm.getAbonPay().setText("");
            payForm.getPena().setText("");
            payForm.getPrimSum().setText("");
            ControlFieldFactory.focusTo(payForm.getAbonPay());
            return;
        }
        String home = payForm.getAdres().getText();
        String adres = payForm.getHome().getText();

        try {
            rowUserInBase = WorkXLS.findUser(street, home, adres);
            if (rowUserInBase != null) {
                payForm.getPersonalAccount().setText(CellsWorker.getCellValue(rowUserInBase, 0));
                payForm.getFirstName().setText(CellsWorker.getCellValue(rowUserInBase, 2));
                payForm.getSecondName().setText(CellsWorker.getCellValue(rowUserInBase, 3));
                payForm.getPhone().setText(CellsWorker.getCellValue(rowUserInBase, 4));

                int dolg = CorrectParceInt.giveMeInt(CellsWorker.getCellValue(rowUserInBase, 9));
                payForm.getDolgJLabel().setText("Сумма долга : " + dolg + "р.");


                //
                //Attribute
                int attribute;
                if (rowUserInBase.getCell(12) == null) {
                    attribute = 300;
                } else {
                    attribute = CorrectParceInt.giveMeInt(CellsWorker.getCellValue(rowUserInBase, 12));
//                    attribute = Integer.parseInt(row.getCell(12).getStringCellValue());
                }
                if (attribute == 0) {
                    attribute = 300;
                }
                payForm.getAtributeLabel().setText("Оплата в месяц : " + attribute + "р.");
                String mountFormat = FormatPayToDate.formMountPayTo(dolg / attribute);
                payForm.getPayForLabel().setText("Оплачено по : 1-е " + mountFormat);

                ControlFieldFactory.focusTo(payForm.getAbonPay());

            } else {
                payForm.getPersonalAccount().setText("NULL");
                payForm.getFirstName().setText("NULL");
                payForm.getSecondName().setText("NULL");
                payForm.getPhone().setText("NULL");

                ControlFieldFactory.focusTo(payForm.getStreetBox());

                JOptionPane.showMessageDialog(null, "Данный клиент не найден в базе!\n" +
                        "Проверьте данные или заведите нового клиента.");

            }
        } catch (IOException e1) {
            log.error(e1.getMessage(), e1);
            e1.printStackTrace();
        }
    }

    private void pay(final JButton payButton, final PayForm payForm, Row baseRow) {
        payButton.addActionListener(e -> {
//            log.info("pay click");
            payment(payForm);
        });


        payButton.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {

            }

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//                    log.info("pay Press");
                    payment(payForm);
                }
            }

            public void keyReleased(KeyEvent e) {

            }
        });
    }

    private void payment(PayForm payForm) {
        String tmp1 = payForm.getPersonalAccount().getText();
        String tmp2 = payForm.getFirstName().getText();
        String tmp3 = payForm.getSecondName().getText();
        String tmp4 = (String) payForm.getStreetBox().getSelectedItem();
        String tmp5 = payForm.getAdres().getText();
        String tmp6 = payForm.getHome().getText();
        String tmp7 = payForm.getPhone().getText();

        int abSum = CorrectParceInt.giveMeInt(payForm.getAbonPay().getText());
        int penaSum = CorrectParceInt.giveMeInt(payForm.getPena().getText());
        int primSum = CorrectParceInt.giveMeInt(payForm.getPrimSum().getText());

        String tmp10 = (String) payForm.getServiceBox().getSelectedItem();
        String tmp12 = payForm.getPrim().getText();

        String date;
        try {
            date = FormatPayToDate.formatPayDate(payForm.getjMonthChooser1().getMonth(), payForm.getjMonthChooser2().getMonth(), payForm.getjYearChooser1().getYear(), payForm.getjYearChooser2().getYear());
        } catch (BadDateException e) {
            JOptionPane.showMessageDialog(null, "МЕСЯЦЫ... Внимательно проверьте дату которую вводите у вас месяц оплаты конечный меньше стартового месяца а годы одинаковые");
            log.warn(e, e);
            return;
        } catch (BadYearException e) {
            JOptionPane.showMessageDialog(null, "ГОДЫ... Внимательно проверьте дату которую вводите у вас год оплаты от меньше года оплаты до");
            log.warn(e, e);
            return;
        }

        try {
            if (!tmp1.equalsIgnoreCase("NULL".trim())) {
                Payment payment = new Payment(tmp1, tmp2, tmp3, tmp4, tmp5, tmp6, tmp7, abSum, penaSum,
                        tmp10, tmp12, primSum, date);
                WorkXLS.newPay(payment, rowUserInBase);
                log.info("All OK");
            } else {
                JOptionPane.showMessageDialog(null, "Оплата не может быть проведена, поскольку не выбран абонент!");
                log.info("No pay, not find user");
            }
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(null, "Произошла критическая ошибка, закройте exel или обратитесь к администратору.");
            log.error(e1.getMessage() + "Close EXEL?", e1);
            e1.printStackTrace();
        } finally {
            clearAllField(payForm);
        }
    }

    /**
     * Очищает все поля от данных
     */
    private void clearAllField(PayForm payForm) {

        payForm.getPersonalAccount().setText("NULL");
        payForm.getPhone().setText("NULL");
        payForm.getFirstName().setText("NULL");
        payForm.getSecondName().setText("NULL");

        payForm.getAbonPay().setText("");
        payForm.getServiceBox().setSelectedIndex(1);
        payForm.getPena().setText("");
        payForm.getPrim().setText("");
        payForm.getPrimSum().setText("");

        payForm.getDolgJLabel().setText("Сумма долга : " + "-р.");
        payForm.getAtributeLabel().setText("Оплата в месяц : " + "-р.");
        payForm.getPayForLabel().setText("Оплачено по : " + "-");

        payForm.getStreetBox().requestFocus();

        Calendar calendar = new GregorianCalendar();

        payForm.getjMonthChooser1().setMonth(calendar.get(Calendar.MONTH));
        payForm.getjMonthChooser2().setMonth(calendar.get(Calendar.MONTH));
        payForm.getjYearChooser1().setYear(calendar.get(Calendar.YEAR));
        payForm.getjYearChooser2().setYear(calendar.get(Calendar.YEAR));
    }

    private void cashOutButton(JButton cashOutButton, PayForm payForm) {
        cashOutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OutForUser.start();
            }
        });
    }


}
