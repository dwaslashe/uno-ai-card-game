package xyz.dwaslashe.gui;

import com.google.common.io.Resources;
import xyz.dwaslashe.enums.CardSpecialType;
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
    private boolean player1Turn = true;
    private boolean gameOn = true;
    private boolean playerUno = false;

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
                    if (!player1Turn) {
                        JOptionPane.showMessageDialog(null, "Teraz trwa tura AI");
                    } else handleCardClick(card, cardLabel);
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

        JPanel middlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton unoButton = new JButton("UNO!");
        unoButton.setFont(new Font("Calibri", Font.PLAIN, 20));
        unoButton.setBackground(new Color(0x0956BF));
        unoButton.setForeground(Color.white);
        unoButton.setUI(new StyleButton());
        unoButton.addActionListener(e -> {
            if (cardObjectPlayerList.size() == 1) {
                JOptionPane.showMessageDialog(null, "UNO");
                playerUno = true;
            } else JOptionPane.showMessageDialog(null, "Masz za dużo kart żeby to zrobić!");
        });

        JButton submitButton = new JButton("Zatwierdź wybór");
        submitButton.setFont(new Font("Calibri", Font.PLAIN, 20));
        submitButton.setBackground(new Color(0x32CD32));
        submitButton.setForeground(Color.white);
        submitButton.setUI(new StyleButton());
        submitButton.addActionListener(e -> {
            if (!player1Turn) {
                JOptionPane.showMessageDialog(null, "Teraz trwa tura AI");
            } else {
                try {
                    if (cardObjectPlayerList.size() == 1) {
                        if (playerUno) {
                            handleSubmitAction(playerPanel, middlePanel, AIPanel);
                        } else {
                            JOptionPane.showMessageDialog(null, "Nie powiedziałeś UNO! Więc dobierasz trzy karty!");
                            handleDrawCard(playerPanel, middlePanel, AIPanel);
                            handleDrawCard(playerPanel, middlePanel, AIPanel);
                            handleDrawCard(playerPanel, middlePanel, AIPanel);
                        }
                    } else handleSubmitAction(playerPanel, middlePanel, AIPanel);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        JButton drawCard = new JButton("Dobierz kartę");
        drawCard.setFont(new Font("Calibri", Font.PLAIN, 20));
        drawCard.setBackground(new Color(0xf73f31));
        drawCard.setForeground(Color.white);
        drawCard.setUI(new StyleButton());
        drawCard.addActionListener(e -> {
            if (!player1Turn) {
                JOptionPane.showMessageDialog(null, "Teraz trwa tura AI");
            } else {
                try {
                    handleDrawCard(playerPanel, middlePanel, AIPanel);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        String middleCardImagePath = getCardImagePath(gameObject.middleCard);
        JLabel middleCardLabel = createCardLabel(middleCardImagePath);
        middlePanel.add(middleCardLabel, BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(unoButton, BorderLayout.NORTH);
        topPanel.add(playerPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
        add(AIPanel, BorderLayout.SOUTH);
        add(submitButton, BorderLayout.EAST);
        add(drawCard, BorderLayout.WEST);
    }

    private String getCardImagePath(CardObject card) {
        String cardType = String.valueOf(card.getCardColor());
        String cardNumber = String.valueOf(card.getCardNumber());

        if (card.getCardNumber() == 10) {
            if (card.getSpecialType() == CardSpecialType.BLOCK) {
                return "cards/" + cardType.toLowerCase()  + "/" + cardType.toLowerCase() + "b.png";
            } else if (card.getSpecialType() == CardSpecialType.SWITCH) {
                return "cards/" + cardType.toLowerCase()  + "/" + cardType.toLowerCase() + "s.png";
            } else if (card.getSpecialType() == CardSpecialType.PLUSTWO) {
                return "cards/" + cardType.toLowerCase()  + "/" + cardType.toLowerCase() + "+.png";
            } else if (card.getSpecialType() == CardSpecialType.PLUSFOUR) {
                return "cards/special/+4.png";
            } else if (card.getSpecialType() == CardSpecialType.CHANGECOLOR) {
                return "cards/special/change.png";
            }
        }
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
        if (selectedCards.contains(card)) {
            selectedCards.remove(card);
            cardLabel.setBorder(null);
        } else {
            selectedCards.add(card);
            cardLabel.setBorder(BorderFactory.createLineBorder(new Color(0xFF5733), 3));
        }
    }

    private void handleDrawCard(JPanel playerPanel, JPanel middlePanel, JPanel AIPanel) throws IOException, InterruptedException {
        if (gameObject.deck.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Deck jest pusty! Nie możesz dobrać karty!");
            return;
        }
        CardObject newCard = gameObject.deck.remove(0);
        gameObject.playerHand.add(newCard);

        playerPanel.removeAll();
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

        if (newCard.getCardColor() == gameObject.middleCard.getCardColor() || newCard.getCardNumber() == gameObject.middleCard.getCardNumber()) {
            JOptionPane.showMessageDialog(null, "Kartę, którą dobrałeś możesz wykorzystać na teraz ponieważ pasuje na środek! " + newCard.getCardColor() + " - " + newCard.getCardNumber());
            return;
        }

        JOptionPane.showMessageDialog(null, "Dobrałeś kartę: " + newCard.getCardColor() + " - " + newCard.getCardNumber());

        player1Turn = false;
        if (!player1Turn) handleAiTurn(middlePanel, AIPanel);
    }

    private void handleSubmitAction(JPanel playerPanel, JPanel middlePanel, JPanel AIPanel) throws IOException, InterruptedException {
        if (isValidMove(selectedCards)) {

            for (CardObject card : selectedCards) {
                gameObject.playerHand.remove(card);
                gameObject.middleCard = card;

                String cardImagePath = getCardImagePath(card);
                JLabel middleCard = createCardLabel(cardImagePath);

                middlePanel.removeAll();
                middlePanel.add(middleCard);
            }
            selectedCards.clear();

            JOptionPane.showMessageDialog(null, "Środkowa karta: " + gameObject.middleCard.getCardColor() + " " + gameObject.middleCard.getCardNumber());

            playerPanel.removeAll();
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

            middlePanel.revalidate();

            JOptionPane.showMessageDialog(null, "Poprawny wybór!");


            if (gameObject.playerHand.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Wygrałeś!");
                gameOn = false;
                return;
            }

            player1Turn = false;
            if (!player1Turn) handleAiTurn(middlePanel, AIPanel);
        } else {
            JOptionPane.showMessageDialog(null, "Niepoprawny wybór!");
        }
    }

    private void handleAiTurn(JPanel middlePanel, JPanel AIPanel) throws IOException, InterruptedException {
        JOptionPane.showMessageDialog(null, "Teraz trwa tura AI!");

        boolean validMove = false;

        while (!validMove) {
            String aiResponse = gameObject.getResponseAI(gameObject.artificialIntelligenceHand);
            validMove = gameObject.playTurnAI(gameObject.artificialIntelligenceHand, aiResponse);

            if (!validMove) {
                System.out.println("Próba ponownie...");
            } else {
                player1Turn = true;

                if (gameObject.artificialIntelligenceHand.size() == 1) {
                    JOptionPane.showMessageDialog(null, "AI: UNO!");
                }

                String cardImagePath = getCardImagePath(gameObject.middleCard);
                JLabel middleCard = createCardLabel(cardImagePath);

                middlePanel.removeAll();
                middlePanel.add(middleCard);
                middlePanel.revalidate();

                AIPanel.removeAll();
                for (CardObject card : gameObject.artificialIntelligenceHand) {
                    String cardImageAIPanelPath = getCardImagePath(card);
                    JLabel cardLabel = createCardLabel(cardImageAIPanelPath);
                    AIPanel.add(cardLabel);
                }
                AIPanel.revalidate();
            }
        }

        if (gameObject.artificialIntelligenceHand.isEmpty()) {
            JOptionPane.showMessageDialog(null, "AI wygrało!");
            gameOn = false;
        }
    }

    private boolean isValidMove(List<CardObject> selectedCards) {
        if (selectedCards.isEmpty()) {
            return false;
        }

        for (CardObject card : selectedCards) {
            if (card.getCardColor() == gameObject.middleCard.getCardColor() || card.getCardNumber() == gameObject.middleCard.getCardNumber()) {
                return true;
            } else return false;
        }
        return true;
    }
}
