package org.blackjack.core;

import org.blackjack.controller.SSEController;

import javax.validation.constraints.NotNull;
import java.util.*;

import static org.blackjack.core.GameMessage.*;
import static org.blackjack.core.GameHands.*;
import static org.blackjack.core.PlayerPot.*;
import static org.blackjack.core.PlayerChips.*;
import static org.blackjack.core.GameStates.*;

public class GameActions {

    public static void resetTable() {
        GameDeck.initializeNewDeck();
        GameHands.resetAllHands();
        GameStates.resetStates();
        PlayerPot.setPot(0);
        GameMessage.setMessage("Please place a bet.");
    }

    public static void startNewRound() {
        displayMessage("Reshuffling Deck.");
        resetTable();
        GameStates.setWaitingForPlayerBet(true);
        SSEController.refreshPage();
    }

    public static void placeBet(int bet) {
        resetTable();
        GameStates.setWaitingForPlayerBet(false);
        addToPot(bet);
        displayMessage("Player bets " + bet + " chips.");
        GameDeck.initializeNewDeck();
        GameActions.dealNewHands();
    }

    public static void dealNewHands() {
        displayMessage("Dealing new hands.");
        for (int i = 0; i < 2; i++) {
            drawCard(getPlayerHand());
            drawCard(getDealerHand());
        }

//        Card dealerCard1 = new Card("C01", 11);
//        Card dealerCard2 = new Card("D01", 11);
//        List<Card> dealerHand = new ArrayList<Card>() {};
//        dealerHand.add(dealerCard1);
//        dealerHand.add(dealerCard2);
//        setDealerHand(dealerHand);
//
//        Card playerCard1 = new Card("H01", 11);
//        Card playerCard2 = new Card("S01", 11);
//        List<Card> playerHand = new ArrayList<Card>() {};
//        playerHand.add(playerCard1);
//        playerHand.add(playerCard2);
//        setPlayerHand(playerHand);

        SSEController.refreshPage();
        wait(2500);
        checkForBlackjack();
    }

    public static void dealerPlays() {
        setDealerRevealedHand(true);
        displayMessage("Dealer reveals hand.");
        while (true) {
            if (handValue(getDealerHand()) <= 16) {
                hits(getDealerHand());
            } else if (handValue(getDealerHand()) == 17 && holdsA(getDealerHand())) {
                changeValueOfA(getDealerHand());
                hits(getDealerHand());
            } else if (handValue(getDealerHand()) > 21 && holdsA(getDealerHand())) {
                changeValueOfA(getDealerHand());
            } else if (handValue(getDealerHand()) <= 21) {
                displayMessage("Dealer stands!");
                endRound();
                break;
            } else {
                if (getPlayerPlayingSplitHand() == 0) {
                    bust(getDealerHand());
                } else {
                    displayMessage("Dealer busts!");
                    endRound();
                }
                break;
            }
        }
    }

    public static void drawCard(@NotNull List<Card> hand) {
        hand.add(GameDeck.drawCard());
        if (hand == getPlayerHand() && getPlayerPlayingSplitHand() != 0) {
            if (getPlayerPlayingSplitHand() == 1) {
                getSplitPlayerHand1().add(new Card(getPlayerHand().get(getPlayerHand().size() - 1).getCardName(), getPlayerHand().get(getPlayerHand().size() - 1).getCardValue()));
            } else if (getPlayerPlayingSplitHand() == 2) {
                getSplitPlayerHand2().add(new Card(getPlayerHand().get(getPlayerHand().size() - 1).getCardName(), getPlayerHand().get(getPlayerHand().size() - 1).getCardValue()));
            }
        }
        if (handValue(hand) > 21 && holdsA(hand)) {
            changeValueOfA(hand);
        }
    }

    public static void checkForBlackjack() {
        if ((getDealerHand().get(0).getCardValue() == 11)) {
            displayMessage("Dealer checks his hand for a Blackjack.");
            if ((getDealerHand().get(0).getCardValue() == 11 && getDealerHand().get(1).getCardValue() != 10) || (getDealerHand().get(0).getCardValue() == 10 && getDealerHand().get(1).getCardValue() != 11)) {
                displayMessage("No Blackjack.");
            }
        }
        if (handValue(getDealerHand()) == 21 && handValue(getPlayerHand()) == 21) {
            setDealerRevealedHand(true);
            displayMessage("Push!");
            returnPot();
            startNewRound();
        } else if (handValue(getPlayerHand()) == 21) {
            setDealerRevealedHand(true);
            displayMessage("You have a Blackjack!");
            setChips(getChips() + getPot() + getPot() * 2);
            setPot(0);
            startNewRound();
        } else if (handValue(getDealerHand()) == 21) {
            setDealerRevealedHand(true);
            displayMessage("Dealer reveals a Blackjack.");
            setPot(0);
            startNewRound();
        } else {
            setPlayerActions();
        }
    }

    public static void setPlayerActions() {
        GameStates.setHitAllowed(true);
        GameStates.setStandAllowed(true);
        if (!isPlayerAlreadyHit() && ((getPlayerPlayingSplitHand() <= 1 && getChips() >= getPot()) || (getPlayerPlayingSplitHand() == 2 && getChips() >= getSplitPot()))) {
            GameStates.setDoubleAllowed(true);
        }
        if (getPlayerPlayingSplitHand() == 0 && getPlayerHand().size() == 2 && (handValue(getPlayerHand()) == getPlayerHand().get(0).getCardValue() * 2) && getChips() >= getPot()) {
            GameStates.setSplitAllowed(true);
        }
        if (GameStrategy.getRemainingHints() > 0) {
            GameStates.setHintAllowed(true);
        }
        if (getPlayerPlayingSplitHand() == 1) {
            GameMessage.setMessage("Select action on 1st hand:");
        } else if (getPlayerPlayingSplitHand() == 2) {
            GameMessage.setMessage("Select action on 2nd hand:");
        } else {
            GameMessage.setMessage("Select action:");
        }
        SSEController.refreshPage();
    }

    public static void bust(List<Card> hand) {
        if (getPlayerPlayingSplitHand() == 1 && hand == getPlayerHand()) {
            displayMessage("Player busts on 1st hand.");
            setPot(0);
        } else if (getPlayerPlayingSplitHand() == 2 && hand == getPlayerHand()) {
            displayMessage("Player busts on 2nd hand.");
            setSplitPot(0);
            setDealerRevealedHand(true);
            displayMessage("Dealer reveals hand.");
        } else if (getPlayerPlayingSplitHand() == 0 && hand == getPlayerHand()) {
            displayMessage("Player busts.");
            setPot(0);
            setDealerRevealedHand(true);
            displayMessage("Dealer reveals hand.");
            startNewRound();
        } else {
            setChips(getChips() + getPot() * 2);
            setPot(0);
            displayMessage("Dealer busts.");
            startNewRound();
        }
    }

    public static int handValue(List<Card> hand) {
        int handValue = 0;
        for (Card card : hand) {
            handValue += card.getCardValue();
        }
        return handValue;
    }

    public static boolean holdsA(List<Card> hand) {
        for (Card card : hand) {
            if (card.getCardValue() == 11) {
                return true;
            }
        }
        return false;
    }

    public static void changeValueOfA(List<Card> hand) {
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getCardValue() == 11) {
                hand.get(i).setCardValue(1);
                if (hand == getPlayerHand() && getPlayerPlayingSplitHand() == 1) {
                    getSplitPlayerHand1().get(i).setCardValue(1);
                } else if (hand == getPlayerHand() && getPlayerPlayingSplitHand() == 2) {
                    getSplitPlayerHand2().get(i).setCardValue(1);
                }
                return;
            }
        }
    }

    public static void endRound() {
        if (getPlayerPlayingSplitHand() == 0) {
            if (handValue(getDealerHand()) == handValue(getPlayerHand())) {
                displayMessage("Push!");
                setChips(getChips() + getPot());
            } else if (handValue(getDealerHand()) < handValue(getPlayerHand())) {
                displayMessage("Player wins!");
                setChips(getChips() + getPot() * 2);
            } else {
                displayMessage("Player loses!");
            }
        } else {
            if (handValue(getSplitPlayerHand1()) <= 21) {
                if (handValue(getDealerHand()) == handValue(getSplitPlayerHand1())) {
                    displayMessage("Push on 1st hand!");
                    setChips(getChips() + getPot());
                } else if ((handValue(getDealerHand()) < handValue(getSplitPlayerHand1())) || handValue(getDealerHand()) > 21) {
                    displayMessage("Player wins on 1st hand!");
                    setChips(getChips() + getPot() * 2);
                } else {
                    displayMessage("Player loses on 1st hand!");
                }
            }
            if (handValue(getSplitPlayerHand2()) <= 21) {
                if (handValue(getDealerHand()) == handValue(getSplitPlayerHand2())) {
                    displayMessage("Push on 2nd hand!");
                    setChips(getChips() + getSplitPot());
                } else if ((handValue(getDealerHand()) < handValue(getSplitPlayerHand2())) || handValue(getDealerHand()) > 21) {
                    displayMessage("Player wins on 2nd hand!");
                    setChips(getChips() + getSplitPot() * 2);
                } else {
                    displayMessage("Player loses on 2nd hand!");
                }
            }
        }
        startNewRound();
    }

    public static void clearTable() {
        setPlayerPlayingSplitHand(0);
        setPlayerAlreadyHit(false);
        setDealerRevealedHand(false);
        getPlayerHand().clear();
        getSplitPlayerHand1().clear();
        getSplitPlayerHand2().clear();
        getDealerHand().clear();
        setPot(0);
        setSplitPot(0);
    }

    public static void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public static void hits(List<Card> hand) {
        resetPlayerActions();
        drawCard(hand);
        if (handValue(hand) > 21 && holdsA(hand)) {
            changeValueOfA(hand);
        }
        if (hand == getPlayerHand()) {
            setPlayerAlreadyHit(true);
            displayMessage("Player Hits!");
            if (handValue(getPlayerHand()) <= 21) {
                setPlayerActions();
            } else {
                bust(getPlayerHand());
            }
        }
        if (hand == getDealerHand()) {
            displayMessage("Dealer Hits!");
        }
    }

    public static void splits() {
        resetPlayerActions();
        displayMessage("Player Splits!");
        displayMessage(getPot() + " chips are placed in 2nd pot.");
        setPlayerPlayingSplitHand(1);
        createSplitPot();
        getSplitPlayerHand1().add(new Card(getPlayerHand().get(0).getCardName(), getPlayerHand().get(0).getCardValue()));
        getSplitPlayerHand2().add(new Card(getPlayerHand().get(1).getCardName(), getPlayerHand().get(1).getCardValue()));
        getPlayerHand().remove(1);
        displayMessage("New card is dealt to 1st hand!");
        SSEController.refreshPage();
        setPlayerActions();
        setPlayerPlayingSplitHand(2);
        setPlayerAlreadyHit(false);
        getPlayerHand().clear();
        getPlayerHand().add(new Card(getSplitPlayerHand2().get(0).getCardName(), getSplitPlayerHand2().get(0).getCardValue()));
        displayMessage("New card is dealt to 1st hand!");
        drawCard(getPlayerHand());
        SSEController.refreshPage();
        setPlayerActions();
        GameStates.setDealerRevealedHand(true);
        if (handValue(getSplitPlayerHand1()) > 21 && handValue(getSplitPlayerHand2()) > 21) {
            clearTable();
        } else {
            dealerPlays();
        }
    }

    public static void doubles() {
        resetPlayerActions();
        if (getPlayerPlayingSplitHand() == 1) {
            drawCard(getPlayerHand());
            if (handValue(getPlayerHand()) > 21 && holdsA(getPlayerHand())) {
                changeValueOfA(getPlayerHand());
            }
            displayMessage("Player doubles on 1st hand!");
            doublePot();
            displayMessage(getPot() + " chips are added to 1st pot.");
        } else if (getPlayerPlayingSplitHand() == 2) {
            drawCard(getPlayerHand());
            if (handValue(getPlayerHand()) > 21 && holdsA(getPlayerHand())) {
                changeValueOfA(getPlayerHand());
            }
            displayMessage("Player doubles on 2nd hand!");
            doubleSplitPot();
            displayMessage(getSplitPot() + " chips are added to 2nd pot.");
            setDealerRevealedHand(true);
        } else {
            drawCard(getPlayerHand());
            if (handValue(getPlayerHand()) > 21 && holdsA(getPlayerHand()))
                changeValueOfA(getPlayerHand());
            displayMessage("Player doubles!");
            doublePot();
            displayMessage(getPot() + " chips are added to the pot.");
            setDealerRevealedHand(true);
            dealerPlays();
        }
    }

    public static void playerStands() {
        resetPlayerActions();
        if (handValue(getPlayerHand()) == 22 && holdsA(getPlayerHand()) && getPlayerHand().size() == 2) {
            changeValueOfA(getPlayerHand());
        }
        if (getPlayerPlayingSplitHand() == 1) {
            displayMessage("Player stands on 1st hand!");
        } else if (getPlayerPlayingSplitHand() == 2) {
            displayMessage("Player stands on 2nd hand!");
            setDealerRevealedHand(true);
        } else {
            displayMessage("Player stands!");
            setDealerRevealedHand(true);
            dealerPlays();
        }
    }

    private GameActions() {
    }

}
