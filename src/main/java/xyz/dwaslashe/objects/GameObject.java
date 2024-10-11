package xyz.dwaslashe.objects;

import com.google.common.io.Resources;
import xyz.dwaslashe.enums.CardSpecialType;
import xyz.dwaslashe.enums.CardType;
import xyz.dwaslashe.groq.GroqApiClientImpl;

import javax.json.Json;
import javax.json.JsonObject;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class GameObject {

    public List<CardObject> deck = new ArrayList<>();
    public List<CardObject> playerHand = new ArrayList<>();
    public List<CardObject> artificialIntelligenceHand = new ArrayList<>();
    public CardObject middleCard;

    public GameObject() {
        initializeDeck();
        playerHand = drawCards(7);
        artificialIntelligenceHand = drawCards(7);

        middleCard = deck.get(0);
    }

    private void initializeDeck() {
        for (CardType cardType : CardType.values()) {

            for (int i = 0; i <= 9; i++) {
                deck.add(new CardObject(cardType, i, CardSpecialType.NUMBER));
            }
            for (int i = 1; i <= 9; i++) {
                deck.add(new CardObject(cardType, i, CardSpecialType.NUMBER));
            }

            for (int i = 1; i <= 8; i++) {
                deck.add(new CardObject(cardType, 10, CardSpecialType.BLOCK));
            }
            //for (int i = 1; i <= 8; i++) {
            //    deck.add(new CardObject(cardType, 10, CardSpecialType.SWITCH));
            //}
            //for (int i = 1; i <= 8; i++) {
            //    deck.add(new CardObject(cardType, 10, CardSpecialType.PLUSTWO));
            //}
            //for (int i = 1; i <= 4; i++) {
            //    deck.add(new CardObject(CardType.NULL, 10, CardSpecialType.PLUSFOUR));
            //}
            //for (int i = 1; i <= 4; i++) {
            //    deck.add(new CardObject(CardType.NULL, 10, CardSpecialType.CHANGECOLOR));
            //}
        }
        Collections.shuffle(deck);
    }

    public List<CardObject> drawCards(int numberOfCards) {
        List<CardObject> hand = new ArrayList<>();
        for (int i = 0; i < numberOfCards; i++) {
            if (!deck.isEmpty()) {
                hand.add(deck.remove(0));
            }
        }
        return hand;
    }

    public void startGame() throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        boolean gameOn = true;
        boolean player1Turn = true;

        System.out.println("Początkowa środkowa karta: " + middleCard.getCardColor() + " - " + middleCard.getCardNumber());

        while (gameOn) {
            if (player1Turn) {
                System.out.println("Tura gracza 1:");
                playTurn(playerHand, scanner);
                if (playerHand.isEmpty()) {
                    System.out.println("Gracz 1 wygrał!");
                    gameOn = false;
                }
            } else {
                System.out.println("Tura gracza 2:");

                boolean validMove = false;

                while (!validMove) {
                    String aiResponse = getResponseAI(artificialIntelligenceHand);
                    validMove = playTurnAI(artificialIntelligenceHand, aiResponse);

                    if (!validMove) {
                        System.out.println("Próba ponownie...");
                    }
                }

                if (artificialIntelligenceHand.isEmpty()) {
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

    public String getResponseAI(List<CardObject> hand) throws IOException, InterruptedException {

        StringBuilder cardsBuilder = new StringBuilder();

        AtomicReference<String> finalPassword = new AtomicReference<>();

        for (int i = 0; i < hand.size(); i++) {
            CardObject card = hand.get(i);
            cardsBuilder.append("\n" + (i + 1) + ": " + card.getCardColor() + " - " + card.getCardNumber());
        }

        URL url = Resources.getResource("license.txt");
        String licenseCode = Resources.toString(url, StandardCharsets.UTF_8);
        GroqApiClientImpl apiClient = new GroqApiClientImpl(licenseCode);

        while (finalPassword.get() == null || finalPassword.get() == "" || finalPassword.get() == " " || finalPassword.get().isEmpty()) {
            JsonObject request = Json.createObjectBuilder()
                    .add("model", "llama3-8b-8192")
                    .add("messages", Json.createArrayBuilder()
                            .add(Json.createObjectBuilder()
                                    .add("role", "user")
                                    .add("content", "Hej chciałbym abyś zagrał ze mną w UNO, aby móć wziąć udział w interakcji gry musisz na KOŃCU TWOJEJ ODPOWIEDZI napisać w 'Hasło: numery liczb' czyli liczbe bądź liczby, które są ponumerowane do kart, które posiadasz. Wpisując 0 dobierasz karte możesz również dawać więcej kart niż jedna pisząc '1 4 5'. Tutaj są twoje karty, które masz: " + cardsBuilder.toString() + ", Środkowa karta: " + middleCard.getCardColor() + " - " + middleCard.getCardNumber())))
                    .build();

            CountDownLatch latch = new CountDownLatch(1);

            apiClient.createChatCompletionAsync(request)
                    .subscribe(
                            response -> {
                                try {
                                    String finalResponse = response.getJsonArray("choices").getJsonObject(0).getJsonObject("message").getString("content");
                                    //System.out.println("finalResponse: " + finalResponse);
                                    String[] parts = finalResponse.split("Hasło:");
                                    if (parts.length > 1) {
                                        String password = parts[1].trim();
                                        //System.out.println("password: " + password);
                                        finalPassword.set(password.replaceAll("[^0-9 ]", "").replace("'", ""));
                                        //System.out.println("finalPassword: " + finalPassword.get());
                                    } else {
                                        System.out.println("Nie znaleziono hasła.");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    System.out.println("Wystąpił błąd podczas przetwarzania odpowiedzi JSON.");
                                } finally {
                                    latch.countDown();
                                }
                            }
                    );
            latch.await();
        }
        return finalPassword.get();
    }

    public boolean playTurnAI(List<CardObject> hand, String response) {
        String finalKeyResponse = response;

        String[] cardIndexArguments = finalKeyResponse.trim().split("\\s+");

        if (cardIndexArguments.length == 1) {
            if (finalKeyResponse.isEmpty() || finalKeyResponse == null) {
                return false;
            }

            String noSpaceKeyResponse = finalKeyResponse.replace(" ", "");


            System.out.println("noSpaceKeyResponse: " + noSpaceKeyResponse);
            System.out.println("finalCardIndex: " + (Integer.parseInt(noSpaceKeyResponse) - 1));
            int finalCardIndex = (Integer.parseInt(noSpaceKeyResponse) - 1);

            if (finalCardIndex == -1) {
                if (!deck.isEmpty()) {
                    CardObject newCard = deck.remove(0);
                    hand.add(newCard);
                    System.out.println("AI dobrał kartę: " + newCard.getCardColor() + " - " + newCard.getCardNumber());
                    JOptionPane.showMessageDialog(null, "AI dobrał kartę: " + newCard.getCardColor() + " - " + newCard.getCardNumber());

                    return true;
                } else {
                    System.out.println("AI ma talie pustą.");
                    return false;
                }
            } else if (finalCardIndex >= 0 && finalCardIndex < hand.size()) {
                CardObject selectedCard = hand.get(finalCardIndex);

                if (selectedCard.getCardColor() == middleCard.getCardColor() || selectedCard.getCardNumber() == middleCard.getCardNumber()) {
                    hand.remove(finalCardIndex);
                    System.out.println("AI zagrał kartę: " + selectedCard.getCardColor() + " - " + selectedCard.getCardNumber());
                    JOptionPane.showMessageDialog(null, "AI zagrał kartę: " + selectedCard.getCardColor() + " - " + selectedCard.getCardNumber());

                    middleCard = selectedCard;

                    return true;
                } else {
                    System.out.println("AI niepoprawna karta.");
                    return false;
                }
            } else {
                System.out.println("AI niepoprawny wybór.");
                return false;
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
                        JOptionPane.showMessageDialog(null, "AI zagrał kartę: " + selectedCard.getCardColor() + " - " + selectedCard.getCardNumber());
                        System.out.println("AI zagrał kartę: " + selectedCard.getCardColor() + " - " + selectedCard.getCardNumber());

                        middleCard = selectedCard;

                        return true;
                    } else {
                        System.out.println("AI niepoprawna karta.");
                        return false;
                    }
                } else {
                    System.out.println("AI niepoprawny wybór.");
                    return false;
                }
            }
        }
        return false;
    }
}
