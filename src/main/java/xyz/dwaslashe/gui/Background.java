package xyz.dwaslashe.gui;

import com.google.common.io.Resources;

import javax.swing.*;
import java.awt.*;

public class Background extends JPanel {

    public void paint(Graphics graphics) {
        ImageIcon icon = new ImageIcon(Resources.getResource("others/table.png"));
        graphics.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), null);
    }


}
