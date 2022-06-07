package com.courseWork.GUI;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class MainWindow extends JFrame{
    public static JPanel mainPanel;

    private JButton addNewOrder_Button;
    private JButton readBook_Button;
    private JButton useAsAStand_Button;
    private JButton deleteOrder_Button;

    private JTable ordersTable;
    private JScrollPane jScrollPane_table;
    private MyTableModel myTableModel;

    public MainWindow () {
        super ("Заказы в кондитерской");
        this.setPreferredSize(new Dimension(760, 320));
        this.setMinimumSize(new Dimension(730, 300));
        this.setLocationByPlatform(true);
        this.setLayout(new GridBagLayout());
        this.init();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void init() {
    }
}
