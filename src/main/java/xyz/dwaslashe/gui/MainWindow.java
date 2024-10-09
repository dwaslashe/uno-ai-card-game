package xyz.dwaslashe.gui;

import javax.swing.*;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("UNO");
        setSize(500,350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CardPanel cardPanel = new CardPanel();
        add(cardPanel);

        setVisible(true);
    }
}
