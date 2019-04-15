package ru.alxant.worker.view;

import org.apache.log4j.Logger;
import ru.alxant.worker.control.ControlFieldFactory;
import ru.alxant.worker.model.support.CorrectParceInt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * Created by Alxan on 14.03.2017.
 */
public class StartDayPage extends JFrame {
    private static final Logger log = Logger.getLogger(StartDayPage.class);

    private JPanel panel1;
    private JButton okButton;
    private JButton exitB;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;

    private String cassaFio;
    private int cash;
    private String pay300;
    private String pay600;
    private String services;
    private boolean allOk = false;

    private File file;

    public StartDayPage(File file) {
        super("Начало дня");
        log.info("StartDayPage");
        this.file = file;
        setPreferredSize(new Dimension(panel1.getPreferredSize().width + 50, panel1.getPreferredSize().height + 35));
        pack();
//        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(panel1);
        textField1.requestFocus();
        setVisible(true);

        focus();
        buttonListeners();
    }

    /**
     * ***********************************************************************
     */

    /**
     * Проверка корректности введенных данных
     */
    private void checkFields() {
        cassaFio = textField1.getText();
        if (cassaFio.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Введите адекватную фамилию");
            log.info("Incorrect FIO");
            return;
        }
        try {
            cash = Integer.parseInt(textField2.getText());
        } catch (NumberFormatException e1) {
            JOptionPane.showMessageDialog(null, "Введите правильную сумму");
            log.info("Incorrect sum");
            return;
        }
        if (cash < 0) {
            JOptionPane.showMessageDialog(null, "У вас сумма меньше 0");
            log.info("Incorrect sum < 0");
            return;
        }
        pay300 = textField3.getText();
        pay600 = textField4.getText();
        services = textField5.getText();
        if (CorrectParceInt.goodPay(pay300) && CorrectParceInt.goodPay(pay600) && CorrectParceInt.goodPay(services)) {
            log.info("Correct Data Write");
            allOk = true;
            dispose();
        } else {
            log.info("Incorrect pay300 - payService");
            JOptionPane.showMessageDialog(null, "У вас неверный формат квитанций должны быть только 6 цыфр.");
        }
    }

    public boolean startInfoOk() {
        return allOk;
    }

    private void focus() {
        ControlFieldFactory.focusStartToEnd(textField1, textField2);
        ControlFieldFactory.focusStartToEnd(textField2, textField3);
        ControlFieldFactory.focusStartToEnd(textField3, textField4);
        ControlFieldFactory.focusStartToEnd(textField4, textField5);
        ControlFieldFactory.focusStartToEnd(textField5, okButton);
    }

    private void buttonListeners() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                file.delete();
                System.exit(0);
            }
        });


        exitB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                file.delete();
                System.exit(0);
            }
        });

        okButton.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    checkFields();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //FIO
                checkFields();
            }
        });
    }

    public String getCassaFio() {
        return cassaFio;
    }

    public int getCash() {
        return cash;
    }

    public String getPay300() {
        return pay300;
    }

    public String getPay600() {
        return pay600;
    }

    public String getServices() {
        return services;
    }

    public boolean isAllOk() {
        return allOk;
    }


}
