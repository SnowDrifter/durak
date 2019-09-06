package ru.romanov.durak.model.player;

import lombok.Data;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.romanov.durak.model.Card;
import ru.romanov.durak.util.JsonHelper;
import ru.romanov.durak.websocket.message.DefaultMessage;
import ru.romanov.durak.websocket.message.Message;
import ru.romanov.durak.websocket.message.MessageType;

import java.io.IOException;
import java.util.List;

@Data
public class HumanPlayer extends Player {

    private String username;
    private WebSocketSession session;
    private Card lastClickedCard;

    @Override
    public void resetStatus() {
        setTake(false);
        setFinishMove(false);
        lastClickedCard = null;
    }

    @Override
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
                sendMessage(new DefaultMessage(MessageType.WRONG_CARD));
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
                sendMessage(new DefaultMessage(MessageType.WRONG_CARD));
            }
        }

        return null;
    }

    @Override
    //TODO: remove it
    public void sendMessage(Message message) {
        if (session.isOpen()) {
            try {
                String json = JsonHelper.convertObject(message);
                session.sendMessage(new TextMessage(json));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void yourMove() {
        sendMessage(new DefaultMessage(MessageType.YOUR_MOVE));
    }

    @Override
    public void enemyMove() {
        sendMessage(new DefaultMessage(MessageType.ENEMY_MOVE));
    }

    private boolean checkDefendCard(Card trying, Card tempEnemyCard) {
        return (tempEnemyCard.getPower() < trying.getPower() && trying.isTrump()) ||
                (tempEnemyCard.getSuit() == trying.getSuit() && tempEnemyCard.getPower() < trying.getPower());
    }
}
