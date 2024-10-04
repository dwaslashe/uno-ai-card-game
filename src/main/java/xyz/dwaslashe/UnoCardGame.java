package xyz.dwaslashe;

import com.google.common.io.Resources;
import xyz.dwaslashe.groq.GroqApiClientImpl;
import xyz.dwaslashe.objects.CardObject;
import xyz.dwaslashe.objects.GameObject;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class UnoCardGame {


    public static void main(String[] args) throws IOException {
        GameObject game = new GameObject();
        game.startGame();

        if (1 == 0) {
            List<CardObject> player1Hand = game.drawCards(7);
            List<CardObject> player2Hand = game.drawCards(7);

            System.out.println("Karty gracza 1:");
            System.out.println("COUNT: " + player1Hand.stream().count());

            player1Hand.forEach(card -> {
                System.out.println(card.getCardColor() + ": " + card.getCardNumber());
            });

            System.out.println("\nKarty gracza 2:");
            player2Hand.forEach(card -> System.out.println(card.getCardColor() + ": " + card.getCardNumber()));
        }

        if (1 == 0) {
            URL url = Resources.getResource("license.txt");
            String licenseCode = Resources.toString(url, StandardCharsets.UTF_8);
            GroqApiClientImpl apiClient = new GroqApiClientImpl(licenseCode);
            JsonObject request = Json.createObjectBuilder()
                    .add("model", "llama3-8b-8192")
                    .add("messages", Json.createArrayBuilder()
                            .add(Json.createObjectBuilder()
                                    .add("role", "user")
                                    .add("content", "Hey, I'd like to play a game of UNO with you, here's your list <listCards> of cards and the card in the middle is <middleCard>")))
                    .build();
            System.out.println(request);
            apiClient.createChatCompletionAsync(request)
                    .subscribe(
                            response -> System.out.println("Otrzymano odpowiedź: " + response)
                    );
        }
    }
}
