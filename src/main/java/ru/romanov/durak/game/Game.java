package ru.romanov.durak.game;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.romanov.durak.model.Card;
import ru.romanov.durak.model.Suit;
import ru.romanov.durak.model.Table;
import ru.romanov.durak.model.player.AIPlayer;
import ru.romanov.durak.model.player.HumanPlayer;
import ru.romanov.durak.model.player.Player;
import ru.romanov.durak.websocket.WebSocketService;
import ru.romanov.durak.websocket.message.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Data
@Slf4j
public class Game implements Runnable {

    private Player firstPlayer;
    private Player secondPlayer;
    private List<Card> deck;
    private Card trump;
    private Table table;
    private boolean draw;
    private Consumer<Game> endGameConsumer = t -> {};
    private WebSocketService webSocketService;
    private GameState state;
    private boolean alive;

    @Override
    public void run() {
        log.info("Start game!");
        initGame();

        while (alive) {
            if (checkWin()) break;
            move(firstPlayer, secondPlayer);
            loggingCurrentState();

            if (checkWin()) break;
            move(secondPlayer, firstPlayer);
            loggingCurrentState();
        }

        if (!alive) {
            log.info("Game is forcibly stopped");
            return;
        }

        log.info("Game over!");
        sendGameOver();
        endGameConsumer.accept(this);
    }

    private void move(Player attackPlayer, Player defendPlayer) {
        if(!alive) {
            return;
        }
        state = GameState.ATTACK;
        attackPlayer.resetStatus();
        defendPlayer.resetStatus();

        if (!checkWin()) {
            webSocketService.sendMessage(attackPlayer.getUsername(), new DefaultMessage(MessageType.YOUR_MOVE));
            webSocketService.sendMessage(defendPlayer.getUsername(), new DefaultMessage(MessageType.ENEMY_MOVE));
        }

        updateTableView();
        loggingCurrentState();

        while (alive) {
            switch (state) {
                case ATTACK: {
                    if (checkWin()) {
                        return;
                    }

                    if (attackPlayer.isFinishMove()) {
                        table.resetTable();
                        fillHand(attackPlayer);
                        fillHand(defendPlayer);
                        return;
                    }

                    Card currentCard = attackPlayer.attack(table.getOldCards());
                    if (currentCard == null) {
                        break;
                    }

                    if (Card.INVALID_CARD.equals(currentCard)) {
                        webSocketService.sendMessage(attackPlayer.getUsername(), new DefaultMessage(MessageType.WRONG_CARD));
                        break;
                    }

                    table.setCurrentCard(currentCard);
                    loggingCurrentState();
                    updateTableView();
                    attackPlayer.resetStatus();
                    state = GameState.DEFEND;
                    break;
                }
                case DEFEND: {
                    if (checkWin()) {
                        return;
                    }

                    if (defendPlayer.isTake()) {
                        defendPlayer.addToHand(table.getAllCardsOnTable());
                        table.resetTable();
                        fillHand(attackPlayer);

                        updateTableView();
                        move(attackPlayer, defendPlayer);
                        return;
                    }

                    Card answer = defendPlayer.defend(table.getCurrentCard());
                    if (answer == null) {
                        break;
                    }

                    if (Card.INVALID_CARD.equals(answer)) {
                        webSocketService.sendMessage(defendPlayer.getUsername(), new DefaultMessage(MessageType.WRONG_CARD));
                        break;
                    }

                    table.getOldCards().add(table.getCurrentCard());
                    table.getOldCards().add(answer);
                    table.setCurrentCard(null);
                    updateTableView();
                    loggingCurrentState();

                    defendPlayer.resetStatus();
                    state = GameState.ATTACK;
                    break;
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }
    }

    public void requestFromPlayer(String username, Message message) {
        Player player = findPlayer(username);

        switch (message.getType()) {
            case SELECT_CARD: {
                CardMessage cardMessage = (CardMessage) message;
                player.selectCard(cardMessage.getCard());
                break;
            }
            case TAKE_CARD: {
                player.setTake(true);
                break;
            }
            case FINISH_MOVE: {
                player.setFinishMove(true);
                break;
            }
        }
    }

    private Player findPlayer(String username) {
        if (firstPlayer.getUsername().equals(username)) {
            return firstPlayer;
        } else {
            return secondPlayer;
        }
    }

    private void initGame() {
        table = new Table();
        state = GameState.ATTACK;
        initDeck();
        Collections.shuffle(deck);

        if (secondPlayer == null) {
            secondPlayer = new AIPlayer();
        }

        for (int i = 0; i < 6; i++) {
            firstPlayer.addToHand(deck.remove(0));
            secondPlayer.addToHand(deck.remove(0));
        }
        alive = true;
    }

    private void updateTableView() {
        updateTableViewForPlayer(firstPlayer, secondPlayer);

        if (secondPlayer instanceof HumanPlayer) {
            updateTableViewForPlayer(secondPlayer, firstPlayer);
        }
    }

    private void updateTableViewForPlayer(Player player, Player enemy) {
        TableMessage message = new TableMessage();

        List<String> playerCards = new ArrayList<>();
        for (Card card : player.getHand()) {
            playerCards.add(card.getName());
        }

        message.setPlayerCards(playerCards);
        message.setEnemyCardsCount(enemy.getHand().size());

        if (trump != null) {
            message.setTrump(trump.getName());
        }
        if (deck != null) {
            message.setDeckSize(deck.size());
        }
        if (!table.isClean()) {
            message.setTableCards(table.getCardNames());
        }

        webSocketService.sendMessage(player.getUsername(), message);
    }

    private void sendGameOver() {
        if (isDraw()) {
            webSocketService.sendMessage(firstPlayer.getUsername(), new DefaultMessage(MessageType.DRAW));
            webSocketService.sendMessage(secondPlayer.getUsername(), new DefaultMessage(MessageType.DRAW));
        } else if (firstPlayer.isWin()) {
            webSocketService.sendMessage(firstPlayer.getUsername(), new DefaultMessage(MessageType.WIN));
            webSocketService.sendMessage(secondPlayer.getUsername(), new DefaultMessage(MessageType.LOSE));
        } else if (secondPlayer.isWin()) {
            webSocketService.sendMessage(firstPlayer.getUsername(), new DefaultMessage(MessageType.LOSE));
            webSocketService.sendMessage(secondPlayer.getUsername(), new DefaultMessage(MessageType.WIN));
        }
    }

    // TODO AOP. Maybe remove it?
    private void loggingCurrentState() {
        log.debug("========================");
        log.debug("First player hand: " + firstPlayer.getHand());
        log.debug("Second player hand: " + secondPlayer.getHand());
        if (deck != null) {
            log.debug("Current deck: " + deck);
        }
        if (trump != null) {
            log.debug("Trump: " + trump.getName());
        }
        log.debug("------------------------");
    }

    private void fillHand(Player player) {
        Collections.shuffle(deck);

        while (player.getHand().size() < 6 && !deck.isEmpty()) {
            player.addToHand(deck.remove(0));
        }

        if (player.getHand().size() < 6 && deck.isEmpty() && trump != null) {
            player.addToHand(trump);
            trump = null;
        }
    }

    // isGameFinished
    public boolean checkWin() {
        if (!deck.isEmpty() && trump != null) {
            return false;
        }

        if (firstPlayer.getHand().isEmpty() && secondPlayer.getHand().isEmpty()) {
            setDraw(true);
            return true;
        } else if (firstPlayer.getHand().isEmpty()) {
            firstPlayer.setWin(true);
            return true;
        } else if (secondPlayer.getHand().isEmpty()) {
            secondPlayer.setWin(true);
            return true;
        }
        return false;
    }

    public void forceStop() {
        alive = false;
    }

    private void initDeck() {
        deck = new ArrayList<>();
        trump = selectTrump();

        for (Suit suit : Suit.values()) {
            boolean trumpFlag = suit.equals(trump.getSuit());
            for (int i = 1; i < 10; i++) {
                String name = suit.getLetter() + i;
                deck.add(new Card(name, suit, i, trumpFlag));
            }
        }

        deck.remove(trump);
    }

    private Card selectTrump() {
        int power = (int) (Math.random() * 9) + 1;
        int suitNumber = (int) (Math.random() * 4);

        Suit suit = Suit.values()[suitNumber];
        String name = suit.name().substring(0, 1).toLowerCase() + power;

        return new Card(name, suit, power, true);
    }

}
