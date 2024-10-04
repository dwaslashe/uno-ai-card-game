package xyz.dwaslashe.objects;

import xyz.dwaslashe.enums.CardType;

import java.util.*;

public class GameObject {

    private List<CardObject> deck = new ArrayList<>();
    private List<CardObject> player1Hand = new ArrayList<>();
    private List<CardObject> player2Hand = new ArrayList<>();
    private CardObject middleCard;

    public GameObject() {
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

            if (gameOn) {
                player1Turn = !player1Turn;
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

            System.out.println("Wybierz kartę/karty lub wpisz 0, aby dobrać kartę:");

            String cardIndex = scanner.nextLine();
            if (cardIndex.isEmpty()) {
                break;
            }
            
            String[] cardIndexArguments = cardIndex.trim().split("\\s+");

            if (cardIndexArguments.length == 1) {
                int finalCardIndex = Integer.parseInt(cardIndex) - 1;

                if (finalCardIndex == -1) {
                    if (!deck.isEmpty()) {
                        CardObject newCard = deck.remove(0);
                        hand.add(newCard);
                        System.out.println("Dobrałeś kartę: " + newCard.getCardColor() + " - " + newCard.getCardNumber());

                        validMove = true;
                    } else {
                        System.out.println("Talia jest pusta, nie możesz dobrać więcej kart.");
                    }
                } else if (finalCardIndex >= 0 && finalCardIndex < hand.size()) {
                    CardObject selectedCard = hand.get(finalCardIndex);

                    if (selectedCard.getCardColor() == middleCard.getCardColor() || selectedCard.getCardNumber() == middleCard.getCardNumber()) {
                        hand.remove(finalCardIndex);
                        System.out.println("Zagrałeś kartę: " + selectedCard.getCardColor() + " - " + selectedCard.getCardNumber());

                        middleCard = selectedCard;

                        validMove = true;
                    } else {
                        System.out.println("Niepoprawna karta. Musisz zagrać kartę o tym samym kolorze lub numerze co środkowa karta. Spróbuj ponownie.");
                    }
                } else {
                    System.out.println("Niepoprawny wybór. Spróbuj ponownie.");
                }
            } else if (cardIndexArguments.length > 1) {

                int[] sortedCardIndex = new int[cardIndexArguments.length];

                for (int i = 0; i < cardIndexArguments.length; i++) {
                    sortedCardIndex[i] = Integer.parseInt(cardIndexArguments[i]) - 1;
                }
                Arrays.sort(sortedCardIndex);

                for (int i = sortedCardIndex.length - 1; i >= 0; i--) {
                    int finalCardIndex = sortedCardIndex[i];

                    if (finalCardIndex >= 0 && finalCardIndex < hand.size()) {
                        CardObject selectedCard = hand.get(finalCardIndex);

                        if (selectedCard.getCardColor() == middleCard.getCardColor() || selectedCard.getCardNumber() == middleCard.getCardNumber()) {
                            hand.remove(finalCardIndex);
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
    }

}
