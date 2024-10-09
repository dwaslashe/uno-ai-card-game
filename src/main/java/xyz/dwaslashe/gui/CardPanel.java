package xyz.dwaslashe.gui;

import com.google.common.io.Resources;
import xyz.dwaslashe.objects.CardObject;
import xyz.dwaslashe.objects.GameObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

public class CardPanel extends JPanel {

    public CardPanel() {
        setLayout(new BorderLayout());

        GameObject gameObject = new GameObject();
        List<CardObject> cardObjectPlayerList = gameObject.playerHand;
        List<CardObject> cardObjectAIList = gameObject.artificialIntelligenceHand;

        JPanel playerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        for (CardObject card : cardObjectPlayerList) {
            String cardImagePath = getCardImagePath(card);
            JLabel cardLabel = createCardLabel(cardImagePath);

            cardLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleCardClick(card);
                }
            });

            playerPanel.add(cardLabel);;
        }

        JPanel AIPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        for (CardObject card : cardObjectAIList) {
            String cardImagePath = getCardImagePath(card);
            JLabel cardLabel = createCardLabel(cardImagePath);

            AIPanel.add(cardLabel);;
        }

        JPanel middlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        String middleCardImagePath = getCardImagePath(gameObject.middleCard);
        JLabel middleCardLabel = createCardLabel(middleCardImagePath);
        middlePanel.add(middleCardLabel);

        add(playerPanel, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
        add(AIPanel, BorderLayout.SOUTH);
    }

    private String getCardImagePath(CardObject card) {
        String cardType = String.valueOf(card.getCardColor());
        String cardNumber = String.valueOf(card.getCardNumber());
        return "cards/" + cardType.toLowerCase()  + "/" + cardType.toLowerCase() + cardNumber + ".png";
    }

    private JLabel createCardLabel(String cardImagePath) {
        JLabel cardLabel = new JLabel();
        try {
            ImageIcon cardIcon = new ImageIcon(ImageIO.read(Resources.getResource(cardImagePath)));
            cardLabel.setIcon(cardIcon);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return cardLabel;
    }

    private void handleCardClick(CardObject card) {
        System.out.println("Karta: " + card.getCardColor() + " " + card.getCardNumber());
    }
}
