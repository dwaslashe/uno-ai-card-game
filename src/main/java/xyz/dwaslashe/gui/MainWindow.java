package xyz.dwaslashe.gui;

import com.google.common.io.Resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("UNO");
        setSize(700, 400);
        ImageIcon icon = new ImageIcon(Resources.getResource("cards/special/change.png"));
        setIconImage(icon.getImage());
        CardPanel cardPanel = new CardPanel();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //setContentPane(new Background());
        add(cardPanel);

        pack();
        setLocationRelativeTo(null);

        setVisible(true);
    }
}