package ru.alxant.converter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Alxant on 10.03.2018.
 */
public class MainForm extends JFrame {


    private JPanel mainPanel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JButton convrsionButton;
    private JButton outButton;

    public MainForm() {
        setSize(750, 250);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("title");
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            System.out.println("getCurrentDirectory(): "
                    + chooser.getCurrentDirectory());
            System.out.println("getSelectedFile() : "
                    + chooser.getSelectedFile());
            textField1.setText(String.valueOf(chooser.getSelectedFile()));
        } else {
            System.out.println("No Selection ");
        }

        convrsionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                XLSBDAbonent xlsbdAbonent = new XLSBDAbonent(textField2.getText());
                XLSDailyReport xlsDailyReport = new XLSDailyReport(textField1.getText());
                ArrayList<Receipt> receipts = xlsDailyReport.createReceiptsList();
                TXTFileFormer txtFileFormer = new TXTFileFormer(textField3.getText() + "/");
                if(txtFileFormer.tryToWrite(receipts)){
                    JOptionPane.showMessageDialog(null,"Конвертация завершена успершно");
                }else {
                    JOptionPane.showMessageDialog(null,"ОШИБКА");
                }
                System.exit(0);
            }
        });

        outButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}
