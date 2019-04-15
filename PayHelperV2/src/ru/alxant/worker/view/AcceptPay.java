package ru.alxant.worker.view;

import org.apache.log4j.Logger;
import ru.alxant.worker.model.WorkXLS;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Alxan on 28.02.2017.
 */
public class AcceptPay extends JFrame {

    private static final Logger log = Logger.getLogger(AcceptPay.class);
    private JPanel panel1;
    private JTable table1;
    private JButton okButton;
    private JButton exitButton;
    private static ArrayList<String[]> map = new ArrayList<String[]>();

    private static AcceptPay ourInstance = new AcceptPay();

    public static AcceptPay getInstance(ArrayList<String[]> mapa) {
        if (ourInstance == null) {
            map = mapa;
            ourInstance = new AcceptPay();
            ourInstance.formTable();
            ourInstance.toFront();
            ourInstance.setState(JFrame.NORMAL);
            ourInstance.requestFocus();


            ourInstance.okButton.requestFocus();
            ourInstance.setVisible(true);
            return ourInstance;
        }
        map = mapa;
        ourInstance.toFront();
        ourInstance.setState(JFrame.NORMAL);
        ourInstance.requestFocus();


        ourInstance.okButton.requestFocus();
        ourInstance.formTable();
        ourInstance.setVisible(true);
        return ourInstance;
    }

    private AcceptPay() {
        super("Окно подтверждения");
        formTable();
        setContentPane(panel1);
        setPreferredSize(new Dimension(panel1.getPreferredSize().width + 50, panel1.getPreferredSize().height + 50));
        pack();

        setLocationRelativeTo(null);
        setVisible(true);

        okButton.addActionListener(e -> okButtonListener());

        okButton.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    okButtonListener();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        exitButton.addActionListener(e -> {
            try {
                ourInstance.setVisible(false);
                WorkXLS.confirmT(false);
            } catch (IOException e1) {
                log.error(e1.getMessage(), e1);
                JOptionPane.showMessageDialog(null, "Критическая ошибка при проведении оплаты");
                ourInstance.setVisible(false);
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    ourInstance.setVisible(false);
                    WorkXLS.confirmT(false);
                } catch (IOException e1) {
                    log.error(e1.getMessage(), e1);
                    JOptionPane.showMessageDialog(null, "Критическая ошибка при проведении оплаты");
                    ourInstance.setVisible(false);
                }
            }
        });

    }

    public void okButtonListener() {
        try {
            ourInstance.setVisible(false);
            WorkXLS.confirmT(true);
        } catch (IOException e1) {
            log.error(e1.getMessage(), e1);
            e1.printStackTrace();
            JOptionPane.showMessageDialog(null, "Критическая ошибка при проведении оплаты");
            ourInstance.setVisible(false);
        }catch (NullPointerException e2){
            log.error(e2.getMessage(), e2);
            JOptionPane.showMessageDialog(null, "Критическая ошибка при проведении оплаты");
            ourInstance.setVisible(false);
        }
    }

    private void formTable() {
        TableModel tableModel = new TableModelAccess(map);
        table1.setModel(tableModel);
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

    public JButton getOkButton() {
        return okButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }
}


class TableModelAccess implements TableModel {

    ArrayList<String[]> map;

    TableModelAccess(ArrayList<String[]> map) {
        this.map = map;
    }

    @Override
    public int getRowCount() {
        return map.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {

        if (columnIndex == 0) {
            return "Наименование";
        } else {
            return "Параметр";
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
        return map.get(rowIndex)[columnIndex];
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
