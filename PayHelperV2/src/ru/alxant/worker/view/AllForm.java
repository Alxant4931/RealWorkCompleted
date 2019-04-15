package ru.alxant.worker.view;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Alxan on 08.03.2017.
 */

class Test{
    public static void main(String[] args) {
        new AllForm();
    }
}


public class AllForm extends JFrame {
    private JPanel panel1;
    private JTabbedPane tabbedPanel;
    private JPanel tabblePanel1;


    public AllForm() {
        super("");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        tabblePanel1 = new PayForm().getSuperPanel();


        setContentPane(panel1);
        setPreferredSize(new Dimension(panel1.getPreferredSize().width + 50, panel1.getPreferredSize().height + 50));
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        setVisible(true);

    }
}
