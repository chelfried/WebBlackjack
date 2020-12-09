package org.blackjack.core;

import java.util.ArrayList;
import java.util.List;

public class GameHands {

    private static List<Card> dealerHand = new ArrayList<>();

    private static List<Card> playerHand = new ArrayList<>();
    private static List<Card> splitPlayerHand1 = new ArrayList<>();
    private static List<Card> splitPlayerHand2 = new ArrayList<>();

    public static void resetAllHands(){
        dealerHand = new ArrayList<>();
        playerHand = new ArrayList<>();
        splitPlayerHand1 = new ArrayList<>();
        splitPlayerHand2 = new ArrayList<>();
    }

    public static List<Card> getDealerHand() {
        return dealerHand;
    }

    public static List<Card> getPlayerHand() {
        return playerHand;
    }

    public static List<Card> getSplitPlayerHand1() {
        return splitPlayerHand1;
    }

    public static List<Card> getSplitPlayerHand2() {
        return splitPlayerHand2;
    }

    private GameHands() {
    }

}