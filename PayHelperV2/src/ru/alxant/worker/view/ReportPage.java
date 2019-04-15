package ru.alxant.worker.view;

import org.apache.log4j.Logger;
import ru.alxant.worker.control.ControlFieldFactory;
import ru.alxant.worker.model.WorkXLS;
import ru.alxant.worker.model.XLSToTable;
import ru.alxant.worker.model.support.CorrectParceInt;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Alxan on 20.02.2017.
 */
public class ReportPage extends JFrame {
    private static final Logger log = Logger.getLogger(ReportPage.class);


    private JPanel panel1;
    private JTable abonPayTable;
    private JTable cassaTable;
    private JTextField payment;
    private JTextField outSum;
    private JButton dayOff;
    private JButton refreshButton;
    private JButton printButton;
    private JSpinner text300;
    private JSpinner text600;
    private JSpinner textServe;
    private JPanel abonPayPanel;


    private ArrayList<String[]> myData = null;
    private ArrayList<String[]> myCassa = null;

    private TableModel modelAbonPay;
    private TableModel modelCassa;

    private static ReportPage ourInstance = new ReportPage();

    public static ReportPage getInstance() {
        if (ourInstance == null) {
            ourInstance = new ReportPage();
            ourInstance.setVisible(true);
            ourInstance.formTable();
            return ourInstance;
        }
        ourInstance.setVisible(true);
        ourInstance.focus();
        return ourInstance;
    }

    private ReportPage() {
        super("Отчет за день");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formTable();

        text300.setEditor(new JSpinner.NumberEditor(text300, "000000"));
        text600.setEditor(new JSpinner.NumberEditor(text600, "000000"));
        textServe.setEditor(new JSpinner.NumberEditor(textServe, "000000"));

//        for (int i = 0; i < myCassa.size(); i++) {
//            System.out.println(Arrays.toString(myCassa.get(i)));
//        }

        int rowCassa = 0;
        for (int i = myCassa.size() - 1; i >= 0; i--) {
            if (myCassa.get(i)[0].equalsIgnoreCase("Квитанции начало")) {
                rowCassa = i;
                break;
            }
        }

        text300.setValue(Integer.parseInt(myCassa.get(rowCassa)[2]));
        text600.setValue(Integer.parseInt(myCassa.get(rowCassa)[3]));
        textServe.setValue(Integer.parseInt(myCassa.get(rowCassa)[4]));

        getContentPane().add(panel1);

        setPreferredSize(new Dimension(panel1.getPreferredSize().width + 250, panel1.getPreferredSize().height + 65));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        focus();

        buttonListeners();
    }

    private static boolean checkPayBox(JSpinner spinner) {
        DecimalFormat decimalFormat = new DecimalFormat("000000");
        return CorrectParceInt.goodPay(decimalFormat.format(Integer.parseInt(spinner.getValue().toString())));
    }

    private static String payBoxValue(JSpinner spinner) {
        DecimalFormat decimalFormat = new DecimalFormat("000000");
        return decimalFormat.format(Integer.parseInt(spinner.getValue().toString()));
    }

    private void buttonListeners() {
        dayOff.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (checkPayBox(text300) && checkPayBox(text600) && checkPayBox(textServe)) {
                    cashPay(CorrectParceInt.giveMeIntRetMinus(payment.getText()), CorrectParceInt.giveMeInt(outSum.getText()), payBoxValue(text300), payBoxValue(text600), payBoxValue(textServe));
                    formTable();
                } else {
                    JOptionPane.showMessageDialog(null, "У вас неверный формат квитанций должны быть только цыфры");
                }
            }
        });

        dayOff.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                    if (checkPayBox(text300) && checkPayBox(text600) && checkPayBox(textServe)) {
                        cashPay(CorrectParceInt.giveMeIntRetMinus(payment.getText()), CorrectParceInt.giveMeInt(outSum.getText()), payBoxValue(text300), payBoxValue(text600), payBoxValue(textServe));
                        formTable();
                    } else {
                        JOptionPane.showMessageDialog(null, "У вас неверный формат квитанций должны быть только цыфры");
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                formTable();
            }
        });

        refreshButton.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    formTable();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        printButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    abonPayTable.print(JTable.PrintMode.FIT_WIDTH);
                    cassaTable.print(JTable.PrintMode.FIT_WIDTH);
                } catch (PrinterException e1) {
                    log.error(e1.getMessage(), e1);
                    e1.printStackTrace();
                }
            }
        });

        printButton.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        abonPayTable.print(JTable.PrintMode.FIT_WIDTH);
                        cassaTable.print(JTable.PrintMode.FIT_WIDTH);
                    } catch (PrinterException e1) {
                        log.error(e1.getMessage(), e1);
                        e1.printStackTrace();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    private void formTable() {
        try {
            myData = XLSToTable.formAbonTable();
            myCassa = XLSToTable.formCassaTable();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }

        modelAbonPay = new TableModelAbonPay(myData);
        modelCassa = new TableModelCassa(myCassa);

        abonPayTable.setModel(modelAbonPay);
        cassaTable.setModel(modelCassa);

        resizeColumWidth(abonPayTable, 25);
        resizeColumWidth(cassaTable);
    }

    private static void cashPay(int cassaPay, int moneyOut, String text300, String text600, String textServe) {
        try {
            WorkXLS.endDay(cassaPay, moneyOut, text300, text600, textServe);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private void resizeColumWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 15;//min
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component component = table.prepareRenderer(renderer, row, column);
                width = Math.max(component.getPreferredSize().width + 10, width);
            }
            if (width > 300) {
                width = 350;
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    private void resizeColumWidth(JTable table, int minWidth) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = minWidth;//min
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component component = table.prepareRenderer(renderer, row, column);
                width = Math.max(component.getPreferredSize().width + 10, width);
            }
            if (width > 300) {
                width = 350;
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    private void focus() {
        ControlFieldFactory.focusTo(payment);
        ControlFieldFactory.focusStartToEnd(payment, outSum);
        ControlFieldFactory.focusStartToEnd(outSum, text300);
        ControlFieldFactory.focusStartToEnd(text300, text600);
        ControlFieldFactory.focusStartToEnd(text600, textServe);
        ControlFieldFactory.focusStartToEnd(textServe, dayOff);
        ControlFieldFactory.focusStartToEnd(dayOff, refreshButton);
        ControlFieldFactory.focusStartToEnd(refreshButton, printButton);
    }


}


class TableModelAbonPay implements TableModel {

    ArrayList<String[]> myList;

    TableModelAbonPay(ArrayList<String[]> myList) {
        this.myList = myList;


    }

    public int getRowCount() {
        return myList.size() - 1;
    }

    public int getColumnCount() {
        return myList.get(0).length;
    }

    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return myList.get(0)[columnIndex];
            case 1:
                return myList.get(0)[columnIndex];
            case 2:
                return myList.get(0)[columnIndex];
            case 3:
                return myList.get(0)[columnIndex];
            case 4:
                return myList.get(0)[columnIndex];
            case 5:
                return myList.get(0)[columnIndex];
            case 6:
                return myList.get(0)[columnIndex];
            case 7:
                return myList.get(0)[columnIndex];
            case 8:
                return myList.get(0)[columnIndex];
        }
        return "";
    }

    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
//        try{

            if (myList.get(rowIndex + 1)[columnIndex].equals("0.0")) {
                return "";
            }
//        }

//        }catch (NullPointerException e){
//            return "";
//        }
        return myList.get(rowIndex + 1)[columnIndex];
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    public void addTableModelListener(TableModelListener l) {

    }

    public void removeTableModelListener(TableModelListener l) {

    }
}

class TableModelCassa implements TableModel {

    ArrayList<String[]> myList;

    TableModelCassa(ArrayList<String[]> myList) {
        this.myList = myList;
    }

    public int getRowCount() {
        return myList.size();
    }

    public int getColumnCount() {
        return myList.get(0).length;
    }

    public String getColumnName(int columnIndex) {

        switch (columnIndex) {
            case 0:
                return "Наименование";
            case 1:
                return "";
            case 2:
                return "Дополнительно";
            case 3:
                return "Приход";
            case 4:
                return "Расход";
            case 5:
                return "Примечание";

        }
        return "";
    }

    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return myList.get(rowIndex)[columnIndex];
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    public void addTableModelListener(TableModelListener l) {

    }

    public void removeTableModelListener(TableModelListener l) {

    }


}
