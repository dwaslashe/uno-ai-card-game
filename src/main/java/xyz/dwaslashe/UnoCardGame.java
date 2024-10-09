package xyz.dwaslashe;

import xyz.dwaslashe.gui.MainWindow;
import xyz.dwaslashe.objects.GameObject;

import javax.swing.*;
import java.io.IOException;

public class UnoCardGame {


    public static void main(String[] args) throws IOException, InterruptedException {
        //GameObject game = new GameObject();
        //game.startGame();

        SwingUtilities.invokeLater(MainWindow::new);
    }
}
