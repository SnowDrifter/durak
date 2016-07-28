package ru.lam.durak.objects.players;


import org.springframework.web.socket.WebSocketSession;
import ru.lam.durak.objects.Card;
import ru.lam.durak.objects.Game;
import ru.lam.durak.objects.Table;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AIPlayer implements Player {
    private Set<Card> hand = new HashSet<>();
    private Game game;
    private boolean take;
    private boolean all;
    private boolean win;

    public AIPlayer(Game game) {
        this.game = game;
        take = false;
        all = false;
        win = false;
    }

    @Override
    public Card attack() {
        if (game.checkWin()) return null;

        Card attackingCard;

        if (isFirstAttack()) {
            attackingCard = firstAttack();
        } else {
            attackingCard = attackWithCardsOnTable();
        }

        if (attackingCard != null) {
            return attackingCard;
        } else {
            setFinishMove(true);
            return null;
        }
    }

    private boolean isFirstAttack() {
        Table currentTable = game.getTable();
        List<Card> oldCards = currentTable.getOldCards();
        return oldCards.isEmpty();
    }

    private Card firstAttack() {
        Card cardForAttacking;

        cardForAttacking = tryingFirstAttackWithoutTrumps();

        if (cardForAttacking == null) {
            cardForAttacking = tryingFirstFullAttack();
            return cardForAttacking;
        } else {
            return cardForAttacking;
        }
    }

    private Card tryingFirstAttackWithoutTrumps() {

        for (Card tryingCard : hand) {
            if (tryingCard.isTrump()) {
                continue;
            }

            hand.remove(tryingCard);
            return tryingCard;
        }

        return null;
    }

    private Card tryingFirstFullAttack() {
        Card tryingCard = hand.iterator().next();
        hand.remove(tryingCard);
        return tryingCard;
    }

    private Card attackWithCardsOnTable() {
        Table currentTable = game.getTable();
        List<Card> oldCards = currentTable.getOldCards();

        for (Card tryingCard : hand) {
            for (Card oldCard : oldCards) {
                if (oldCard.getPower() == tryingCard.getPower()) {
                    hand.remove(tryingCard);
                    return tryingCard;
                }
            }
        }

        return null;
    }


    @Override
    public Card defend(Card attackingCard) {
        if (game.checkWin()) {
            return null;
        }

        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Card cardForDefending;

        if (attackingCard.isTrump()) {
            cardForDefending = defendVersusTrump(attackingCard);
        } else {
            cardForDefending = defendVersusSimpleCard(attackingCard);
        }

        if (cardForDefending != null) {
            return cardForDefending;
        } else {
            setTake(true);
            return null;
        }
    }

    private Card defendVersusTrump(Card attackingCard) {
        for (Card tryingCard : hand) {

            if (tryingCard.isTrump() && tryingCard.getPower() > attackingCard.getPower()) {

                hand.remove(tryingCard);
                return tryingCard;
            }
        }
        return null;
    }

    private Card defendVersusSimpleCard(Card attackingCard) {
        for (Card trying : hand) {

            if (trying.getSuit() == attackingCard.getSuit() && trying.getPower() > attackingCard.getPower()) {
                hand.remove(trying);
                return trying;
            } else if (trying.isTrump() && trying.getPower() + 10 > attackingCard.getPower()) {
                hand.remove(trying);
                return trying;
            }
        }
        return null;
    }


    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public Set<Card> getHand() {
        return hand;
    }

    public void setHand(Set<Card> hand) {
        this.hand = hand;
    }

    @Override
    public boolean isTake() {
        return take;
    }

    @Override
    public void setTake(boolean take) {
        this.take = take;
    }

    public boolean isFinishMove() {
        return all;
    }

    public void setFinishMove(boolean all) {
        this.all = all;
    }

    @Override
    public void sendMessage(String message) {
        // AIPlayer does not send messages
    }

    @Override
    public void resetStatus() {
        setTake(false);
        setFinishMove(false);
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public void selectCard(String cardName) {
        // Not necessary for AIPlayer
    }

    @Override
    public void yourMove() {
        // AIPlayer does not send notifications
    }

    @Override
    public void enemyMove() {
        // AIPlayer does not send notifications
    }

    @Override
    public WebSocketSession getSession() {
        // AIPlayer doesn't have session
        return null;
    }

}
