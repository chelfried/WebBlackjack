package org.blackjack.core;

public class Card {

    String cardName;
    int cardValue;

    public Card(String name, int cardValue) {
        this.cardName = name;
        this.cardValue = cardValue;
    }

    public String getCardName() {
        return cardName;
    }

    public int getCardValue() {
        return cardValue;
    }

    public void setCardValue(int cardValue) {
        this.cardValue = cardValue;
    }
}
