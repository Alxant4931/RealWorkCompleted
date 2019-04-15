package ru.alxant.worker.view;

import org.apache.log4j.Logger;
import ru.alxant.worker.control.ControlFieldFactory;
import ru.alxant.worker.control.PayButton;
import ru.alxant.worker.model.WorkXML;
import ru.alxant.worker.model.XLSToTable;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Alxan on 19.03.2017.
 */
public class BarsForm extends JFrame{
    private static final Logger log = Logger.getLogger(BarsForm.class);

    private static BarsForm ourInstance = new BarsForm();
    private JPanel panel1;
    private JTextField textField2;
    private JComboBox comboBox1;
    private JButton findButton;
    private JTable table1;

    private ArrayList<String[]> data;

    private TableModel modelBars;

    public static BarsForm getInstance() {
        if (ourInstance == null) {
            ourInstance = new BarsForm();
            ourInstance.setVisible(true);
            ourInstance.formTable();
            return ourInstance;
        }
        ourInstance.setVisible(true);
        ourInstance.focus();
        return ourInstance;
    }

    private BarsForm() {
        super("Формирование решеток");
        log.info("Start BarsForm");
        formTable();

        String homeBoxTmp[] = WorkXML.servicesWork("resources/Streets.xml");
        for (int i = 0; i < homeBoxTmp.length; i++) {
            comboBox1.addItem(homeBoxTmp[i]);
        }
        setPreferredSize(new Dimension(panel1.getPreferredSize().width + 50, panel1.getPreferredSize().height + 35));
        pack();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setContentPane(panel1);

        focus();
        setVisible(true);
    }

    private void focus() {
        ControlFieldFactory.focusTo(comboBox1);
        ControlFieldFactory.focusStartToEnd(comboBox1, textField2);
    }

    private void formTable() {
        try {
            data = XLSToTable.formBars(String.valueOf(comboBox1.getSelectedItem()), textField2.getText());
        }catch (IOException e){
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        modelBars = new BarsTableModel(data);
        table1.setModel(modelBars);
        resizeColumWidth(table1);


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




    private class BarsTableModel implements TableModel {
        int money;
        int attribute;
        ArrayList<String[]> data;

        BarsTableModel(ArrayList<String[]> data){
            this.data = data;
        }

        @Override
        public int getRowCount() {
            return data.size()-1;
        }

        @Override
        public int getColumnCount() {
            return 17;
        }

        @Override
        public String getColumnName(int columnIndex) {
            switch (columnIndex){
                case 0 : return "0";
                case 1 : return "1";
                case 2 : return "2";
                case 3 : return "3";
                case 4 : return "4";
                case 5 : return "5";
                case 6 : return "6";
                case 7 : return "7";
                case 8 : return "8";
                case 9 : return "9";
                case 10 : return "10";
                case 11 : return "11";
                case 12 : return "12";
                case 13 : return "13";
                case 14 : return "14";
                case 15 : return "15";
                case 16 : return "16";
                default:return "D";
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data.get(rowIndex)[columnIndex];
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        }

        @Override
        public void addTableModelListener(TableModelListener l) {

        }

        @Override
        public void removeTableModelListener(TableModelListener l) {

        }
    }
}
