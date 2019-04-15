package ru.alxant.worker.view;


import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;
import ru.alxant.worker.control.ControlFieldFactory;
import ru.alxant.worker.control.PayButton;
import ru.alxant.worker.model.WorkXML;

import javax.swing.*;
import java.awt.*;

import java.util.logging.Logger;

import static ru.alxant.worker.StartClass.PROGRAM_VERSION;

/**
 * Created by Alxan on 02.02.2017.
 */


public class PayForm extends JFrame {
    private static Logger log = Logger.getLogger(PayForm.class.getName());

    private JPanel mainPanel;
    private JPanel clienInforamtionPanel;
    private JTextField personalAccount;
    private JTextField phone;
    private JTextField firstName;
    private JTextField secondName;
    private JComboBox streetBox;
    private JTextField adres;
    private JTextField home;
    private JPanel paymentPanel;
    private JTextField abonPay;
    private JTextField pena;
    private JComboBox serviceBox;
    private JTextField servicePay;
    private JTextArea prim;
    private JTextField primSum;
    private JButton button1;
    private JButton searchButton;
    private JButton cashOutButton;
    private JButton newAbonent;
    private JButton exitButton;
    private JScrollBar scrollBar1;
    private JMonthChooser jMonthChooser1;
    private JMonthChooser jMonthChooser2;
    private JYearChooser jYearChooser1;
    private JPanel superPanel;
    private JLabel atributeLabel;
    private JLabel payForLabel;
    private JYearChooser jYearChooser2;
    private JButton barsButton;
    private JLabel dolgJLabel;

    public JLabel getDolgJLabel() {
        return dolgJLabel;
    }

    public PayForm() {
        super("Работа с абонентами " + PROGRAM_VERSION);
        log.info("Start PayForm");

        String serviceBoxTmp[] = WorkXML.servicesWork("resources/Services.xml");
        for (int i = 0; i < serviceBoxTmp.length; i++) {
            serviceBox.addItem(serviceBoxTmp[i]);
        }
        serviceBox.setSelectedIndex(1);

        String homeBoxTmp[] = WorkXML.servicesWork("resources/Streets.xml");
        for (int i = 0; i < homeBoxTmp.length; i++) {
            streetBox.addItem(homeBoxTmp[i]);
        }
        setPreferredSize(new Dimension(mainPanel.getPreferredSize().width + 50, mainPanel.getPreferredSize().height + 35));

        pack();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(mainPanel);

        new PayButton(button1, searchButton, cashOutButton, this);

        exitButton.addActionListener(e -> System.exit(0));
        newAbonent.addActionListener(e -> NewAbonent.getInstance());
        focus();
        setVisible(true);
    }

    private void focus(){
        ControlFieldFactory.focusTo(streetBox);
        ControlFieldFactory.focusStartToEnd(streetBox, adres);
        ControlFieldFactory.focusStartToEnd(adres, home);
        ControlFieldFactory.focusStartToEnd(home, searchButton);

        ControlFieldFactory.focusStartToEnd(abonPay, (JComponent) jMonthChooser1.getComboBox());

        ControlFieldFactory.focusStartToEnd((JComponent) jMonthChooser1.getComboBox(), (JComponent) jYearChooser1.getSpinner());
        ControlFieldFactory.focusStartToEnd((JComponent) jYearChooser1.getSpinner(), (JComponent) jMonthChooser2.getComboBox());
        ControlFieldFactory.focusStartToEnd((JComponent) jMonthChooser2.getComboBox(), (JComponent) jYearChooser2.getSpinner());
        ControlFieldFactory.focusStartToEnd((JComponent) jYearChooser2.getSpinner(), button1);

        ControlFieldFactory.focusStartToEnd(serviceBox, pena);
        ControlFieldFactory.focusStartToEnd(pena, prim);
        ControlFieldFactory.focusStartToEnd(prim, primSum);
        ControlFieldFactory.focusStartToEnd(primSum, button1);
        ControlFieldFactory.focusStartToEnd(button1, cashOutButton);
        ControlFieldFactory.focusStartToEnd(cashOutButton, streetBox);

    }

    //========================================================

    // Getters

    public JTextField getPersonalAccount() {
        return personalAccount;
    }

    public JTextField getFirstName() {
        return firstName;
    }

    public JTextField getSecondName() {
        return secondName;
    }

    public JComboBox getStreetBox() {
        return streetBox;
    }

    public JTextField getAdres() {
        return adres;
    }

    public JTextField getHome() {
        return home;
    }

    public JTextField getPhone() {
        return phone;
    }

    public JTextField getAbonPay() {
        return abonPay;
    }

    public JTextField getPena() {
        return pena;
    }

    public JComboBox getServiceBox() {
        return serviceBox;
    }

    public JTextField getServicePay() {
        return servicePay;
    }

    public JTextArea getPrim() {
        return prim;
    }

    public JTextField getPrimSum() {
        return primSum;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public JButton getButton1() {
        return button1;
    }

    public JScrollBar getScrollBar1() {
        return scrollBar1;
    }

    public JMonthChooser getjMonthChooser1() {
        return jMonthChooser1;
    }

    public JMonthChooser getjMonthChooser2() {
        return jMonthChooser2;
    }

    public JYearChooser getjYearChooser1() {
        return jYearChooser1;
    }

    public JYearChooser getjYearChooser2() {
        return jYearChooser2;
    }

    public JLabel getAtributeLabel() {
        return atributeLabel;
    }

    public JLabel getPayForLabel() {
        return payForLabel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        jMonthChooser1 = new JMonthChooser();
//        jMonthChooser1.getSpinner().setVisible(false);
        jMonthChooser2 = new JMonthChooser();
//        jMonthChooser2.getSpinner().setVisible(false);
        jYearChooser1 = new JYearChooser();
        jYearChooser2 = new JYearChooser();

    }

    public JPanel getSuperPanel() {
        return superPanel;
    }

    // ===============================================
    // setters
}
