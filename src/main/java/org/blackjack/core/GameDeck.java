package org.blackjack.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameDeck {

    private static final List<Card> deck = new ArrayList<>();

    public static Card drawCard() {
        Card drawnCard = deck.get(0);
        deck.remove(0);
        return drawnCard;
    }

    public static void initializeNewDeck() {

        deck.clear();

        deck.add(new Card("C01", 11));
        deck.add(new Card("C02", 2));
        deck.add(new Card("C03", 3));
        deck.add(new Card("C04", 4));
        deck.add(new Card("C05", 5));
        deck.add(new Card("C06", 6));
        deck.add(new Card("C07", 7));
        deck.add(new Card("C08", 8));
        deck.add(new Card("C09", 9));
        deck.add(new Card("C10", 10));
        deck.add(new Card("C11", 10));
        deck.add(new Card("C12", 10));
        deck.add(new Card("C13", 10));
        deck.add(new Card("C01", 11));

        deck.add(new Card("D02", 2));
        deck.add(new Card("D03", 3));
        deck.add(new Card("D04", 4));
        deck.add(new Card("D05", 5));
        deck.add(new Card("D06", 6));
        deck.add(new Card("D07", 7));
        deck.add(new Card("D08", 8));
        deck.add(new Card("D09", 9));
        deck.add(new Card("D10", 10));
        deck.add(new Card("D11", 10));
        deck.add(new Card("D12", 10));
        deck.add(new Card("D13", 10));

        deck.add(new Card("H02", 2));
        deck.add(new Card("H03", 3));
        deck.add(new Card("H04", 4));
        deck.add(new Card("H05", 5));
        deck.add(new Card("H06", 6));
        deck.add(new Card("H07", 7));
        deck.add(new Card("H08", 8));
        deck.add(new Card("H09", 9));
        deck.add(new Card("H10", 10));
        deck.add(new Card("H11", 10));
        deck.add(new Card("H12", 10));
        deck.add(new Card("H13", 10));

        deck.add(new Card("S02", 2));
        deck.add(new Card("S03", 3));
        deck.add(new Card("S04", 4));
        deck.add(new Card("S05", 5));
        deck.add(new Card("S06", 6));
        deck.add(new Card("S07", 7));
        deck.add(new Card("S08", 8));
        deck.add(new Card("S09", 9));
        deck.add(new Card("S10", 10));
        deck.add(new Card("S11", 10));
        deck.add(new Card("S12", 10));
        deck.add(new Card("S13", 10));

        Collections.shuffle(deck);
    }

    private GameDeck() {
    }
}
