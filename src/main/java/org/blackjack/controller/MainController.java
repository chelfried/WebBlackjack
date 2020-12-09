package org.blackjack.controller;

import org.blackjack.core.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    String redirect = "redirect:/blackjack";

    @GetMapping("/newGame")
    public ModelAndView welcome() {
        return new ModelAndView("welcome");
    }

    @PostMapping("/startNewGame")
    public ModelAndView startNewGame() {
        GameStrategy.setRemainingHints(5);
        PlayerChips.setChips(10000);
        GameActions.resetTable();
        GameStates.setWaitingForPlayerBet(true);
        return new ModelAndView(redirect);
    }

    @PostMapping("/blackjack/placeBet")
    public ModelAndView placeBet() {
        if (GameStates.isWaitingForPlayerBet() && (PlayerPot.getTempPot() <= PlayerChips.getChips()) && PlayerPot.getTempPot() != 0) {
            GameActions.placeBet(PlayerPot.getTempPot());
        } else {PlayerPot.setTempPot(0);}
        return new ModelAndView(redirect);
    }

    @GetMapping("/blackjack")
    public ModelAndView playGame() {
        return new ModelAndView("blackjack");
    }

    @PostMapping("/blackjack/hit")
    public ModelAndView playerHits() {
        GameActions.hits(GameHands.getPlayerHand());
        return new ModelAndView(redirect);
    }

    @PostMapping("/blackjack/stand")
    public ModelAndView playerStands() {
        GameActions.playerStands();
        return new ModelAndView(redirect);
    }

    @PostMapping("/blackjack/double")
    public ModelAndView playerDoubles() {
        GameActions.doubles();
        return new ModelAndView(redirect);
    }

    @PostMapping("/blackjack/hint")
    public ModelAndView displayHint() {
        GameStrategy.hint();
        return new ModelAndView(redirect);
    }

    @PostMapping("/tempBet/{bet}")
    public ModelAndView placeTempBet(@PathVariable Integer bet) {
        if (getPlayerTempPot() + bet <= PlayerChips.getChips()){
        PlayerPot.setTempPot(getPlayerTempPot() + bet);}
        SSEController.refreshPage();
        return new ModelAndView(redirect);
    }

    @PostMapping("/tempBet/clear")
    public ModelAndView clearTempBet() {
        PlayerPot.setTempPot(0);
        SSEController.refreshPage();
        return new ModelAndView(redirect);
    }

    @ModelAttribute("gameMessage")
    public String getGameMessage() {
        return GameMessage.getMessage();
    }

    @ModelAttribute ("dealerHand")
    public String[] getDealerHand() {
        List<Card> dealerHand = GameHands.getDealerHand();
        String[] dealerCards = new String[9];
        for (int i = 0; i < dealerHand.size(); i++) {
            dealerCards[i] = "cards/" + dealerHand.get(i).getCardName() + ".svg";
        }
        if (!GameStates.isDealerRevealedHand() && !dealerHand.isEmpty()) {
            dealerCards[1] = "cards/BCK.svg";
        }
        return dealerCards;
    }

    @ModelAttribute("playerHand")
    public String[] getPlayerHand() {
        List<Card> playerHand = GameHands.getPlayerHand();
        String[] playerCards = new String[9];
        for (int i = 0; i < playerHand.size(); i++) {
            playerCards[i] = "cards/" + playerHand.get(i).getCardName() + ".svg";
        }
        return playerCards;
    }

    @ModelAttribute("playerSplitHand1")
    public String[] getPlayerSplitHand1() {
        List<Card> playerHand = GameHands.getSplitPlayerHand1();
        String[] playerCards = new String[9];
        for (int i = 0; i < playerHand.size(); i++) {
            playerCards[i] = "cards/" + playerHand.get(i).getCardName() + ".svg";
        }
        return playerCards;
    }

    @ModelAttribute("playerSplitHand2")
    public String[] getPlayerSplitHand2() {
        List<Card> playerHand = GameHands.getSplitPlayerHand2();
        String[] playerCards = new String[9];
        for (int i = 0; i < playerHand.size(); i++) {
            playerCards[i] = "cards/" + playerHand.get(i).getCardName() + ".svg";
        }
        return playerCards;
    }

    @ModelAttribute("hitAllowed")
    public boolean isHitAllowed() {
        return GameStates.isHitAllowed();
    }

    @ModelAttribute("standAllowed")
    public boolean isStandAllowed() {
        return GameStates.isStandAllowed();
    }

    @ModelAttribute("doubleAllowed")
    public boolean isDoubleAllowed() {
        return GameStates.isDoubleAllowed();
    }

    @ModelAttribute("splitAllowed")
    public boolean isSplitAllowed() {
        return GameStates.isSplitAllowed();
    }

    @ModelAttribute("hintAllowed")
    public boolean isHintAllowed() {
        return GameStates.isHintAllowed();
    }

    @ModelAttribute("waitingForPlayerBet")
    public boolean isWaitingForPlayerBet() {
        return GameStates.isWaitingForPlayerBet();
    }

    @ModelAttribute("playingSplitHands")
    public int playingSplitHands() {
        return GameStates.getPlayerPlayingSplitHand();
    }

    @ModelAttribute("playerPot")
    public int getPlayerPot() {
        return PlayerPot.getPot();
    }

    @ModelAttribute("playerSplitPot")
    public int getPlayerSplitPot() {
        return PlayerPot.getSplitPot();
    }

    @ModelAttribute("playerChips")
    public int getPlayerChips() {
        return PlayerChips.getChips();
    }

    @ModelAttribute("handValueDealer")
    public String getHandValueDealer() {
        List<Card> dealerHand = GameHands.getDealerHand();
        List<Card> dealerHandDeepCopy = new ArrayList<>();
        for (int i = 0; i < dealerHand.size(); i++) {
            dealerHandDeepCopy.add(i, new Card(dealerHand.get(i).getCardName(), dealerHand.get(i).getCardValue()));
        }
        if (!GameStates.isDealerRevealedHand() && !dealerHand.isEmpty()) {
            dealerHandDeepCopy.remove(1);
        }
        return getResult(dealerHandDeepCopy);
    }

    @ModelAttribute("handValuePlayer")
    public String getHandValuePlayer() {
        return getResult(GameHands.getPlayerHand());
    }

    private String getResult(List<Card> hand) {
        int handValue = 0;
        String result;
        for (Card card : hand) {
            handValue += card.getCardValue();
            if (card.getCardValue() == 11) {
                handValue -= 10;
            }
        }
        if (GameActions.holdsA(hand) && handValue + 10 <= 21) {
            result = handValue + "/" + (handValue + 10);
        } else result = String.valueOf(handValue);
        return result;
    }

    @ModelAttribute("remainingHints")
    public int getRemainingHints() {
        return GameStrategy.getRemainingHints();
    }

    @ModelAttribute("tempPotStatus")
    public boolean tempPotActive() {
        return GameStates.isWaitingForPlayerBet();
    }

    @ModelAttribute("tempPot")
    public int getPlayerTempPot() {
        return PlayerPot.getTempPot();
    }

    private MainController() {
    }

}