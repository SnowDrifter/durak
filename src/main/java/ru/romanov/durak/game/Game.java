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
import java.util.stream.Collectors;

@Data
@Slf4j
public class Game {

    private Player attackPlayer;
    private Player defendPlayer;
    private List<Card> deck;
    private Card trump;
    private Table table;
    private boolean draw;
    private Consumer<Game> endGameConsumer = t -> {};
    private WebSocketService webSocketService;
    private GameState state;
    private boolean alive;

    private void attack() {
        Card currentCard = attackPlayer.attack(table.getOldCards());
        if (currentCard == null) {
            if (attackPlayer instanceof AIPlayer) {
                playerFinish();
            }
            return;
        }

        if (Card.INVALID_CARD.equals(currentCard)) {
            webSocketService.sendMessage(attackPlayer.getUsername(), new DefaultMessage(MessageType.WRONG_CARD));
            return;
        }

        table.setCurrentCard(currentCard);
        loggingCurrentState();
        updateTableView();
        changeState();

        if (defendPlayer instanceof AIPlayer) {
            defend();
        }

        if (checkWin()) {
            finishGame();
        }
    }

    private void defend() {
        if (checkWin()) {
            finishGame();
            return;
        }

        Card answer = defendPlayer.defend(table.getCurrentCard());
        if (answer == null) {
            if (defendPlayer instanceof AIPlayer) {
                playerTake();
            }
            return;
        }

        if (Card.INVALID_CARD.equals(answer)) {
            webSocketService.sendMessage(defendPlayer.getUsername(), new DefaultMessage(MessageType.WRONG_CARD));
            return;
        }

        table.getOldCards().add(table.getCurrentCard());
        table.getOldCards().add(answer);
        table.setCurrentCard(null);
        updateTableView();
        loggingCurrentState();

        changeState();
        if (attackPlayer instanceof AIPlayer) {
            attack();
        }

        if (checkWin()) {
            finishGame();
        }
    }

    private void playerFinish() {
        fillHand(defendPlayer);
        fillHand(attackPlayer);

        prepareNextRound();
        table.resetTable();
        updateTableView();

        webSocketService.sendMessage(attackPlayer.getUsername(), new DefaultMessage(MessageType.YOUR_MOVE));
        webSocketService.sendMessage(defendPlayer.getUsername(), new DefaultMessage(MessageType.ENEMY_MOVE));

        if (attackPlayer instanceof AIPlayer) {
            attack();
        }
    }

    private void playerTake() {
        defendPlayer.addToHand(table.getAllCardsOnTable());
        fillHand(attackPlayer);
        table.resetTable();
        updateTableView();

        changeState();

        webSocketService.sendMessage(attackPlayer.getUsername(), new DefaultMessage(MessageType.YOUR_MOVE));
        webSocketService.sendMessage(defendPlayer.getUsername(), new DefaultMessage(MessageType.ENEMY_MOVE));

        if (attackPlayer instanceof AIPlayer) {
            attack();
        }

    }

    private void changeState() {
        if (state.equals(GameState.ATTACK)) {
            state = GameState.DEFEND;
        } else {
            state = GameState.ATTACK;
        }
    }

    private void prepareNextRound() {
        Player temp = attackPlayer;
        attackPlayer = defendPlayer;
        defendPlayer = temp;
        state = GameState.ATTACK;
    }

    public void requestFromPlayer(String username, Message message) {
        Player player = findPlayer(username);

        switch (message.getType()) {
            case SELECT_CARD: {
                CardMessage cardMessage = (CardMessage) message;
                player.selectCard(cardMessage.getCard());

                switch (state) {
                    case ATTACK: {
                        attack();
                        break;
                    }
                    case DEFEND: {
                        defend();
                        break;
                    }
                }
                break;
            }
            case TAKE_CARD: {
                playerTake();
                break;
            }
            case FINISH_MOVE: {
                playerFinish();
                break;
            }
        }
    }

    private Player findPlayer(String username) {
        if (attackPlayer.getUsername().equals(username)) {
            return attackPlayer;
        } else {
            return defendPlayer;
        }
    }

    public void initGame(Player firstPlayer, Player secondPlayer) {
        attackPlayer = firstPlayer;
        defendPlayer = secondPlayer;
        table = new Table();
        state = GameState.ATTACK;
        initDeck();
        Collections.shuffle(deck);

        if (defendPlayer == null) {
            defendPlayer = new AIPlayer();
        }

        for (int i = 0; i < 6; i++) {
            attackPlayer.addToHand(deck.remove(0));
            defendPlayer.addToHand(deck.remove(0));
        }
        alive = true;
        updateTableView();
        webSocketService.sendMessage(attackPlayer.getUsername(), new DefaultMessage(MessageType.YOUR_MOVE));
        webSocketService.sendMessage(defendPlayer.getUsername(), new DefaultMessage(MessageType.ENEMY_MOVE));
    }

    private void updateTableView() {
        updateTableViewForPlayer(attackPlayer, defendPlayer);

        if (defendPlayer instanceof HumanPlayer) {
            updateTableViewForPlayer(defendPlayer, attackPlayer);
        }
    }

    private void updateTableViewForPlayer(Player player, Player enemy) {
        TableMessage message = new TableMessage();

        List<Card> playerTrumps = new ArrayList<>();
        List<Card> playerCards = new ArrayList<>();

        player.getHand()
                .stream()
                .collect(Collectors.groupingBy(Card::getSuit))
                .forEach((suit, cards) -> {
                    if (cards.get(0).isTrump()) {
                        playerTrumps.addAll(cards);
                    } else {
                        playerCards.addAll(cards);
                    }
                });

        List<String> playerCardNames = new ArrayList<>();
        playerCards.forEach(c -> playerCardNames.add(c.getName()));
        playerTrumps.forEach(c -> playerCardNames.add(c.getName()));

        message.setPlayerCards(playerCardNames);
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
        if (draw) {
            webSocketService.sendMessage(attackPlayer.getUsername(), new DefaultMessage(MessageType.DRAW));
            webSocketService.sendMessage(defendPlayer.getUsername(), new DefaultMessage(MessageType.DRAW));
        } else if (attackPlayer.isWin()) {
            webSocketService.sendMessage(attackPlayer.getUsername(), new DefaultMessage(MessageType.WIN));
            webSocketService.sendMessage(defendPlayer.getUsername(), new DefaultMessage(MessageType.LOSE));
        } else if (defendPlayer.isWin()) {
            webSocketService.sendMessage(attackPlayer.getUsername(), new DefaultMessage(MessageType.LOSE));
            webSocketService.sendMessage(defendPlayer.getUsername(), new DefaultMessage(MessageType.WIN));
        }
    }

    // TODO AOP. Maybe remove it?
    private void loggingCurrentState() {
        log.debug("========================");
        log.debug("First player hand: " + attackPlayer.getHand());
        log.debug("Second player hand: " + defendPlayer.getHand());
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

        if (attackPlayer.getHand().isEmpty() && defendPlayer.getHand().isEmpty()) {
            setDraw(true);
            return true;
        } else if (attackPlayer.getHand().isEmpty()) {
            attackPlayer.setWin(true);
            return true;
        } else if (defendPlayer.getHand().isEmpty()) {
            defendPlayer.setWin(true);
            return true;
        }
        return false;
    }

    private void finishGame() {
        sendGameOver();
        endGameConsumer.accept(this);
    }

    public void forceStop() {
        alive = false;
    }

    private void initDeck() {
        deck = new ArrayList<>();
        trump = selectTrump();

        for (Suit suit : Suit.values()) {
            boolean trumpFlag = suit.equals(trump.getSuit());
            for (int i = 0; i < 9; i++) {
                String name = suit.getLetter() + i;
                deck.add(new Card(name, suit, i, trumpFlag));
            }
        }

        deck.remove(trump);
    }

    private Card selectTrump() {
        int power = (int) (Math.random() * 9);
        int suitNumber = (int) (Math.random() * 4);

        Suit suit = Suit.values()[suitNumber];
        String name = suit.name().substring(0, 1).toLowerCase() + power;

        return new Card(name, suit, power, true);
    }

}
