package ru.romanov.durak.model.player;


import lombok.Data;
import org.springframework.web.socket.WebSocketSession;
import ru.romanov.durak.websocket.message.Message;
import ru.romanov.durak.model.Card;
import ru.romanov.durak.model.Game;
import ru.romanov.durak.model.Table;

import java.util.List;

@Data
public class AIPlayer extends Player {

    public AIPlayer(Game game) {
        this.game = game;
    }

    @Override
    public Card attack() {
        if (game.checkWin()) {
            return null;
        }

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
        Card cardForAttacking = tryingFirstAttackWithoutTrumps();
        if (cardForAttacking == null) {
            cardForAttacking = tryingFirstFullAttack();
        }

        return cardForAttacking;
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

    @Override
    public void sendMessage(Message message) {
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
