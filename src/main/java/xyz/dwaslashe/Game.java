package xyz.dwaslashe;

import xyz.dwaslashe.enums.CardType;
import xyz.dwaslashe.objects.CardObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Game {

    private List<CardObject> deck = new ArrayList<>();
    private List<CardObject> player1Hand = new ArrayList<>();
    private List<CardObject> player2Hand = new ArrayList<>();
    private CardObject middleCard;

    public Game() {
        initializeDeck();
        player1Hand = drawCards(7);
        player2Hand = drawCards(7);

        middleCard = deck.get(0);
    }

    private void initializeDeck() {
        for (CardType cardType : CardType.values()) {

            for (int i = 0; i <= 9; i++) {
                deck.add(new CardObject(cardType, i));
            }
            for (int i = 1; i <= 9; i++) {
                deck.add(new CardObject(cardType, i));
            }
        }
        Collections.shuffle(deck);
    }

    public List<CardObject> drawCards(int numberOfCards) {
        List<CardObject> hand = new ArrayList<>();
        for (int i = 0; i < numberOfCards; i++) {
            if (!deck.isEmpty()) {
                hand.add(deck.get(0));//hand.add(deck.remove(0))
                deck.remove(0);
            }
        }
        return hand;
    }

    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        boolean gameOn = true;
        boolean player1Turn = true;

        System.out.println("Początkowa środkowa karta: " + middleCard.getCardColor() + " - " + middleCard.getCardNumber());

        while (gameOn) {
            if (player1Turn) {
                System.out.println("Tura gracza 1:");
                playTurn(player1Hand, scanner);
                if (player1Hand.isEmpty()) {
                    System.out.println("Gracz 1 wygrał!");
                    gameOn = false;
                }
            } else {
                System.out.println("Tura gracza 2:");
                playTurn(player2Hand, scanner);
                if (player2Hand.isEmpty()) {
                    System.out.println("Gracz 2 wygrał!");
                    gameOn = false;
                }
            }
        }
        scanner.close();
    }

    private void playTurn(List<CardObject> hand, Scanner scanner) {
        boolean validMove = false;

        while (!validMove) {
            System.out.println("Środkowa karta: " + middleCard.getCardColor() + " - " + middleCard.getCardNumber());

            System.out.println("Twoje karty:");
            for (int i = 0; i < hand.size(); i++) {
                CardObject card = hand.get(i);
                System.out.println((i + 1) + ": " + card.getCardColor() + " - " + card.getCardNumber());
            }

            System.out.println("Wybierz kartę lub wpisz 0, aby dobrać kartę:");
            int cardIndex = scanner.nextInt() - 1;

            if (cardIndex == -1) {
                if (!deck.isEmpty()) {
                    CardObject newCard = deck.remove(0);
                    hand.add(newCard);
                    System.out.println("Dobrałeś kartę: " + newCard.getCardColor() + " - " + newCard.getCardNumber());
                    return;
                } else {
                    System.out.println("Talia jest pusta, nie możesz dobrać więcej kart.");
                }
            } else if (cardIndex >= 0 && cardIndex < hand.size()) {
                CardObject selectedCard = hand.get(cardIndex);

                if (selectedCard.getCardColor() == middleCard.getCardColor() || selectedCard.getCardNumber() == middleCard.getCardNumber()) {
                    hand.remove(cardIndex);
                    System.out.println("Zagrałeś kartę: " + selectedCard.getCardColor() + " - " + selectedCard.getCardNumber());

                    middleCard = selectedCard;

                    validMove = true;
                } else {
                    System.out.println("Niepoprawna karta. Musisz zagrać kartę o tym samym kolorze lub numerze co środkowa karta. Spróbuj ponownie.");
                }
            } else {
                System.out.println("Niepoprawny wybór. Spróbuj ponownie.");
            }
        }
    }

}
