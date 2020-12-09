package org.blackjack.core;

import org.blackjack.controller.SSEController;

public class GameMessage {

    private static String message = "Please place a bet.";

    public static String getMessage() {
        return message;
    }

    public static void setMessage(String gameMessage) {
        GameMessage.message = gameMessage;
    }

    public static void displayMessage(String message){
        GameMessage.message = message;
        SSEController.refreshPage();
        wait(3500);
    }

    public static void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private GameMessage() {
    }

}
