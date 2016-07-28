package ru.lam.durak.objects.players;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.lam.durak.objects.Card;
import ru.lam.durak.objects.Game;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RealPlayer implements Player {
    private String username;
    private WebSocketSession session;
    private Game game;
    private boolean take;
    private boolean finishMove;
    private boolean win;
    private Set<Card> hand = new HashSet<>();
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

                    // TODO Грёбанная египетская пирамида
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

    private boolean checkDefendCard(Card trying, Card tempEnemyCard){
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

    public RealPlayer() {
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

    public boolean isTake() {
        return take;
    }
    public void setTake(boolean take) {
        this.take = take;
    }

    public boolean isFinishMove() {
        return finishMove;
    }
    public void setFinishMove(boolean finishMove) {
        this.finishMove = finishMove;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public WebSocketSession getSession() {
        return session;
    }
    public void setSession(WebSocketSession session) {
        this.session = session;
    }


}
