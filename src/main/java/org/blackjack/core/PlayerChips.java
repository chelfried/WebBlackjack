package org.blackjack.core;

public class PlayerChips {

    private static int chips = 10000;

    public static int getChips() {
        return chips;
    }

    public static void setChips(int chips) {
        PlayerChips.chips = chips;
    }

    private PlayerChips() {
    }

}
