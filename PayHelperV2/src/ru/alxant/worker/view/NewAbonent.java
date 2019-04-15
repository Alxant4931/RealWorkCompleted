package ru.alxant.worker.view;


import com.toedter.calendar.JDateChooser;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import ru.alxant.worker.control.ControlFieldFactory;
import ru.alxant.worker.model.Payment;
import ru.alxant.worker.model.WorkXLS;
import ru.alxant.worker.model.WorkXML;
import ru.alxant.worker.model.support.CellsWorker;
import ru.alxant.worker.model.support.CorrectParceInt;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Alxan on 20.02.2017.
 */
public class NewAbonent extends JFrame {
    private static final Logger log = Logger.getLogger(NewAbonent.class);
    private JPanel panel1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JButton appButton;
    private JButton outButton;
    private JComboBox comboBox1;
    private JPanel datePanel;
    private JDateChooser jDateChooser1;
    private JButton clearFieldButton;
    private JTextField textFieldPayOnMount;

    private static NewAbonent ourInstance = new NewAbonent();

    public static NewAbonent getInstance() {
        if (ourInstance == null) {
            ourInstance = new NewAbonent();
            ourInstance.setVisible(true);
            ourInstance.clearField();
            return ourInstance;
        }
        ourInstance.setVisible(true);
        ourInstance.clearField();
        return ourInstance;
    }

    private void clearField() {
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField5.setText("");
        textField6.setText("");
        textField7.setText("");
        textFieldPayOnMount.setText("300");
    }


    private NewAbonent() {

        super("Новый абонент");
        String homeBoxTmp[] = WorkXML.servicesWork("resources/Streets.xml");
        for (int i = 0; i < homeBoxTmp.length; i++) {
            comboBox1.addItem(homeBoxTmp[i]);
        }
        //        add(datePicker);
        setPreferredSize(new Dimension(panel1.getPreferredSize().width + 50, panel1.getPreferredSize().height + 50));
        pack();
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setContentPane(panel1);

        setLocationRelativeTo(null);
        setVisible(true);

        outButton.addActionListener(e -> out());

        appButton.addActionListener(e -> {
            try {
                addUser();
            } catch (IOException e1) {
                log.error(e1.getMessage(), e1);
                e1.printStackTrace();
                JOptionPane.showMessageDialog(null, "Что-то пошло не так обратитесь к администратору!!!");
            }
        });

        clearFieldButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearField();
            }
        });

        //* focusField
        ControlFieldFactory.focusTo(textField1);
        ControlFieldFactory.focusStartToEnd(textField1, textField2);
        ControlFieldFactory.focusStartToEnd(textField2, textField3);
        ControlFieldFactory.focusStartToEnd(textField3, comboBox1);
        ControlFieldFactory.focusStartToEnd(comboBox1, textField5);
        ControlFieldFactory.focusStartToEnd(textField5, textField6);
        ControlFieldFactory.focusStartToEnd(textField6, textFieldPayOnMount);
        ControlFieldFactory.workJDateChooser(textFieldPayOnMount, jDateChooser1, textField7);
        ControlFieldFactory.focusStartToEnd(jDateChooser1.getJCalendar(), textField7);
        ControlFieldFactory.workJDateChooser(jDateChooser1, textField7);
        ControlFieldFactory.focusStartToEnd(textField7, appButton);
        ControlFieldFactory.focusStartToEnd(appButton, textField1);

        ControlFieldFactory.focusStartToEnd(clearFieldButton, textField1);
    }

    private void out() {
        setVisible(false);
    }

    private void addUser() throws IOException {
        Row tmpRow;
        tmpRow = WorkXLS.findUser(String.valueOf(comboBox1.getSelectedItem()), textField5.getText(), textField6.getText());

        if (tmpRow != null) {
            JOptionPane.showMessageDialog(null, "Такой адрес уже есть в БД, просто проведите замену полей в главном окне");
            return;
        }

        String fileName = "resources/АБОНЕНТЫ.XLS";
        FileInputStream fileInputStream = new FileInputStream(fileName);
        HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
        fileInputStream.close();
        Sheet sheet = workbook.getSheetAt(0);
        int lastRow = sheet.getLastRowNum();

        // запись
        /*
         * 0 Лиц Счет
         * 1 Код
         * 2 Фамилия
         * 3 ИО
         * 4 Телефон
         * 5 Адрес пр
         * 6 Адрес Улица
         * 7 Адрес Домэ
         * 8 Адрес кв
         * 9 Долг
         * 10 Статус
         * 11 Примечание
         * 12 Атрибуты
         * 13 Договор по
         * 14 Кр. Статус
         */


        Row row = sheet.createRow(lastRow + 1);
        row.createCell(0).setCellValue("Временный");
        row.createCell(1).setCellValue("Код");
        row.createCell(2).setCellValue(textField1.getText());
        row.createCell(3).setCellValue(textField2.getText());
        row.createCell(4).setCellValue(textField3.getText());
        row.createCell(5).setCellValue("ул.");
        row.createCell(6).setCellValue((String) comboBox1.getSelectedItem());
        row.createCell(7).setCellValue(textField5.getText());
        row.createCell(8).setCellValue(textField6.getText());
        row.createCell(9).setCellValue(0);
        // dataFormat
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
        //
        String date = simpleDateFormat.format(jDateChooser1.getDate());

        //
        row.createCell(10).setCellValue("А. Подключение(" + date + ")");
        row.createCell(11).setCellValue(textField6.getText());

        int abonplata = CorrectParceInt.giveMeInt(textFieldPayOnMount.getText());
        if(abonplata!= 300 && abonplata != 0) {
            CellsWorker.writeCell(row.getSheet(), row.getRowNum(), 12, abonplata);
        }
        // внесение абонента в происшествее за день
        //---------------------------------------------

        //---------------------------------------------


        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        workbook.close();
        JOptionPane.showMessageDialog(null, "Абонент успешно внесен");
        out();
    }

    private void createUIComponents() {
        jDateChooser1 = new JDateChooser();
        jDateChooser1.setDate(new Date());
        // TODO: place custom component creation code here
    }

    private void $$$setupUI$$$() {
        createUIComponents();
    }
}
