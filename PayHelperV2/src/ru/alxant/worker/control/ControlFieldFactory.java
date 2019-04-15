package ru.alxant.worker.control;


import com.toedter.calendar.JDateChooser;
import org.apache.log4j.Logger;
import ru.alxant.worker.model.support.StartDaysWork;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Alxan on 26.02.2017.
 */
public class ControlFieldFactory {

    private static final Logger log = Logger.getLogger(ControlFieldFactory.class);

    public static void focusTo(JComponent component) {
        component.requestFocus();
    }

    public static void focusStartToEnd(final JComponent startComponent, final JComponent endComponent) {
        startComponent.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {

            }

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == (KeyEvent.VK_ENTER)) {


                    endComponent.requestFocus();
                    if (endComponent instanceof JTextField) {
                        ((JTextField) endComponent).selectAll();
                    }

                }
            }

            public void keyReleased(KeyEvent e) {

            }
        });
    }

    public static void workJDateChooser(JComponent startComponent, JDateChooser jDateChooser, JComponent endComponent) {
        startComponent.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {

            }

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == (KeyEvent.VK_ENTER)) {
                    endComponent.requestFocus();
                    jDateChooser.requestFocus();
                    jDateChooser.getCalendarButton().doClick();


                }
            }

            public void keyReleased(KeyEvent e) {

            }
        });


    }

    public static void workJDateChooser(JDateChooser jDateChooser, JComponent endComponent) {
        jDateChooser.getJCalendar().getMonthChooser().getComboBox().addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {

            }

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == (KeyEvent.VK_ENTER)) {

                    endComponent.requestFocus();
                    if (endComponent instanceof JTextField) {
                        ((JTextField) endComponent).selectAll();
                    }
                }
            }

            public void keyReleased(KeyEvent e) {

            }
        });


    }


}
