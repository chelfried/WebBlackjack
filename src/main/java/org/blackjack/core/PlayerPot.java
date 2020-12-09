package org.blackjack.core;

import static org.blackjack.core.PlayerChips.*;

public class PlayerPot {

    private static int tempPot;
    private static int pot;
    private static int splitPot;

    public static void doublePot() {
        setChips(getChips() - pot);
        pot *= 2;
    }

    public static void doubleSplitPot() {
        setChips(getChips() - splitPot);
        splitPot *= 2;
    }

    public static int getPot() {
        return pot;
    }

    public static void setPot(int pot) {
        PlayerPot.pot = pot;
    }

    public static void returnPot() {
        setChips(getChips() + pot);
        pot = 0;
    }

    public static void addToPot(int pot) {
        PlayerPot.pot += pot;
        setChips(getChips() - pot);
    }

    public static int getSplitPot() {
        return splitPot;
    }

    public static void setSplitPot(int splitPot) {
        PlayerPot.splitPot = splitPot;
    }

    public static int getTempPot() {
        return tempPot;
    }

    public static void setTempPot(int tempPot) {
        PlayerPot.tempPot = tempPot;
    }

    public static void createSplitPot() {
        splitPot = pot;
        setChips(PlayerChips.getChips() - pot);
    }

    private PlayerPot() {
    }
}
