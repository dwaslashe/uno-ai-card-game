package xyz.dwaslashe.objects;

import xyz.dwaslashe.enums.CardType;

public class CardObject {

    private CardType cardColor;
    private int cardNumber;

    public CardObject(CardType cardColor, int cardNumber) {
        this.cardColor = cardColor;
        this.cardNumber = cardNumber;
    }

    public CardObject(CardType cardColor) {
        this.cardColor = cardColor;
    }



    public CardType getCardColor() {
        return cardColor;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardColor(CardType cardColor) {
        this.cardColor = cardColor;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }
}
