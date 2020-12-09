package org.blackjack.core;

public class GameStates {

    private static boolean waitingForPlayerBet = true;
    private static int playerPlayingSplitHand = 0;
    private static boolean playerAlreadyHit = false;
    private static boolean dealerRevealedHand = false;
    private static boolean hitAllowed = false;
    private static boolean standAllowed = false;
    private static boolean doubleAllowed = false;
    private static boolean splitAllowed = false;
    private static boolean hintAllowed = false;

    public static void resetStates() {
        playerPlayingSplitHand = 0;
        playerAlreadyHit = false;
        dealerRevealedHand = false;
        hitAllowed = false;
        standAllowed = false;
        doubleAllowed = false;
        splitAllowed = false;
        hintAllowed = false;
    }

    public static void resetPlayerActions() {
        hitAllowed = false;
        standAllowed = false;
        doubleAllowed = false;
        splitAllowed = false;
        hintAllowed = false;
    }

    public static int getPlayerPlayingSplitHand() {
        return playerPlayingSplitHand;
    }

    public static void setPlayerPlayingSplitHand(int playerPlayingSplitHand) {
        GameStates.playerPlayingSplitHand = playerPlayingSplitHand;
    }

    public static boolean isPlayerAlreadyHit() {
        return playerAlreadyHit;
    }

    public static void setPlayerAlreadyHit(boolean playerAlreadyHit) {
        GameStates.playerAlreadyHit = playerAlreadyHit;
    }

    public static boolean isDealerRevealedHand() {
        return dealerRevealedHand;
    }

    public static void setDealerRevealedHand(boolean dealerRevealedHand) {
        GameStates.dealerRevealedHand = dealerRevealedHand;
    }

    public static boolean isHitAllowed() {
        return hitAllowed;
    }

    public static void setHitAllowed(boolean hitAllowed) {
        GameStates.hitAllowed = hitAllowed;
    }

    public static boolean isStandAllowed() {
        return standAllowed;
    }

    public static void setStandAllowed(boolean standAllowed) {
        GameStates.standAllowed = standAllowed;
    }

    public static boolean isDoubleAllowed() {
        return doubleAllowed;
    }

    public static void setDoubleAllowed(boolean doubleAllowed) {
        GameStates.doubleAllowed = doubleAllowed;
    }

    public static boolean isSplitAllowed() {
        return splitAllowed;
    }

    public static void setSplitAllowed(boolean splitAllowed) {
        GameStates.splitAllowed = splitAllowed;
    }

    public static boolean isHintAllowed() {
        return hintAllowed;
    }

    public static void setHintAllowed(boolean hintAllowed) {
        GameStates.hintAllowed = hintAllowed;
    }

    public static boolean isWaitingForPlayerBet() {
        return waitingForPlayerBet;
    }

    public static void setWaitingForPlayerBet(boolean waitingForPlayerBet) {
        GameStates.waitingForPlayerBet = waitingForPlayerBet;
    }

    private GameStates() {
    }

}
