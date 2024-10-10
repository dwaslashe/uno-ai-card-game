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
import java.util.*;
import java.util.List;

public class CardPanel extends JPanel {

    private GameObject gameObject = new GameObject();

    public CardPanel() {
        setLayout(new BorderLayout());

        List<CardObject> cardObjectPlayerList = gameObject.playerHand;
        List<CardObject> cardObjectAIList = gameObject.artificialIntelligenceHand;

        JPanel playerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        for (CardObject card : cardObjectPlayerList) {
            String cardImagePath = getCardImagePath(card);
            JLabel cardLabel = createCardLabel(cardImagePath);

            cardLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleCardClick(card, cardLabel);
                }
            });

            playerPanel.add(cardLabel);
        }

        JPanel AIPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        for (CardObject card : cardObjectAIList) {
            String cardImagePath = getCardImagePath(card);
            JLabel cardLabel = createCardLabel(cardImagePath);
            AIPanel.add(cardLabel);
        }

        JPanel middlePanel = new JPanel(new BorderLayout());

        JButton submitButton = new JButton("Zatwierdź");
        submitButton.addActionListener(e -> {
            handleSubmitAction(playerPanel, middlePanel);
        });

        String middleCardImagePath = getCardImagePath(gameObject.middleCard);
        JLabel middleCardLabel = createCardLabel(middleCardImagePath);

        middlePanel.add(middleCardLabel, BorderLayout.CENTER);

        add(playerPanel, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
        add(AIPanel, BorderLayout.SOUTH);

        add(submitButton, BorderLayout.EAST);
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

    private List<CardObject> selectedCards = new ArrayList<>();

    private void handleCardClick(CardObject card, JLabel cardLabel) {
        System.out.println("Karta: " + card.getCardColor() + " " + card.getCardNumber());

        if (selectedCards.contains(card)) {
            selectedCards.remove(card);
            cardLabel.setBorder(null);
        } else {
            selectedCards.add(card);
            cardLabel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
        }
    }

    private void handleSubmitAction(JPanel playerPanel, JPanel middlePanel) {
        if (isValidMove(selectedCards)) {
            for (CardObject card : selectedCards) {
                gameObject.playerHand.remove(card);

                gameObject.middleCard.setCardColor(card.getCardColor());
                gameObject.middleCard.setCardNumber(card.getCardNumber());

                String cardImagePath = getCardImagePath(card);
                JLabel middleCard = createCardLabel(cardImagePath);
                middlePanel.add(middleCard);
            }

            for (CardObject card : gameObject.playerHand) {
                String cardImagePath = getCardImagePath(card);
                JLabel cardLabel = createCardLabel(cardImagePath);

                cardLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleCardClick(card, cardLabel);
                    }
                });

                playerPanel.add(cardLabel);
            }
            playerPanel.revalidate();
            playerPanel.repaint();

            middlePanel.revalidate();
            middlePanel.repaint();

            System.out.println("Poprawny wybór!");

            if (gameObject.playerHand.isEmpty()) {
                System.out.println("Gracz 1 wygrał!");
                return;
            }

            selectedCards.clear();
        } else {
            System.out.println("Niepoprawny wybór spróbuj ponownie.");
        }
    }




    private boolean isValidMove(List<CardObject> selectedCards) {
        if (selectedCards.isEmpty()) {
            return false;
        }

        for (CardObject card : selectedCards) {
            if (card.getCardColor() == gameObject.middleCard.getCardColor() || card.getCardNumber() == gameObject.middleCard.getCardNumber()) {
                return true;
            }
        }
        return true;
    }
}
