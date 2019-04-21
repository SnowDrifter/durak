package ru.romanov.durak.object.player;

import lombok.Data;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.romanov.durak.object.Card;

import java.io.IOException;
import java.util.List;

@Data
public class RealPlayer extends Player {

    private String username;
    private WebSocketSession session;
    private Card lastClickedCard;

    @Override
    public void resetStatus() {
        setTake(false);
        setFinishMove(false);
        lastClickedCard = null;
    }

    public void selectCard(String cardName) {
        for (Card card : hand) {
            if (card.getName().equals(cardName)) {
                lastClickedCard = card;
            }
        }
    }

    @Override
    public Card attack() {
        if (game.checkWin()) {
            return null;
        }

        List<Card> oldCardsOnTable = game.getTable().getOldCards();

        while (!finishMove) {
            if (oldCardsOnTable.size() == 0 && lastClickedCard != null) {
                // Select card for empty table.
                Card resultCard = lastClickedCard;
                hand.remove(resultCard);
                lastClickedCard = null;
                return resultCard;
            } else if (oldCardsOnTable.size() > 0 && lastClickedCard != null) {

                for (Card oldCard : oldCardsOnTable) {

                    if (oldCard.getPower() == lastClickedCard.getPower()) {
                        Card resultCard = lastClickedCard;
                        hand.remove(resultCard);
                        lastClickedCard = null;
                        return resultCard;
                    }
                }
                lastClickedCard = null;
                sendMessage("wrongcard");
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public Card defend(Card enemyCard) {
        if (game.checkWin()) {
            return null;
        }

        Card tempEnemyCard = enemyCard;
        if (tempEnemyCard.isTrump()) {
            tempEnemyCard.setPower(enemyCard.getPower() + 10);
        }

        while (!take) {
            if (lastClickedCard != null) {

                Card trying = lastClickedCard;

                if (lastClickedCard.isTrump()) {
                    trying.setPower(lastClickedCard.getPower() + 10);
                }

                if (checkDefendCard(trying, tempEnemyCard)) {

                    if (lastClickedCard.isTrump()) {
                        trying.setPower(lastClickedCard.getPower() - 10);  // Reset power after check.
                    }

                    lastClickedCard = null;
                    hand.remove(trying);
                    return trying;
                }

                lastClickedCard = null;
                sendMessage("wrongcard");
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private boolean checkDefendCard(Card trying, Card tempEnemyCard) {
        return (tempEnemyCard.getPower() < trying.getPower() && trying.isTrump()) ||
                (tempEnemyCard.getSuit() == trying.getSuit() && tempEnemyCard.getPower() < trying.getPower());
    }

    @Override
    public void sendMessage(String message) {
        if (session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void yourMove() {
        sendMessage("your_move");
    }

    @Override
    public void enemyMove() {
        sendMessage("enemy_move");
    }

}
