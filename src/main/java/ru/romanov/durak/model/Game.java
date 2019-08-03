package ru.romanov.durak.model;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.romanov.durak.controller.websocket.message.*;
import ru.romanov.durak.model.player.AIPlayer;
import ru.romanov.durak.model.player.Player;
import ru.romanov.durak.model.player.RealPlayer;
import ru.romanov.durak.user.model.User;
import ru.romanov.durak.user.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Slf4j
@Component
public class Game implements Runnable {

    private UserService userService;
    private Player firstPlayer;
    private Player secondPlayer;
    private List<Card> deck;
    private Card trump;
    private Table table;
    private boolean draw;

    @Override
    public void run() {
        log.info("Start game!");

        while (true) {
            if (checkWin()) break;
            move(firstPlayer, secondPlayer);
            loggingCurrentState();

            if (checkWin()) break;
            move(secondPlayer, firstPlayer);
            loggingCurrentState();
        }

        log.info("Game over!");
        sendGameOver();
        updateStatistics();
    }

    private void move(Player attackPlayer, Player defendPlayer) {
        if (checkWin()) return;

        attackPlayer.resetStatus();
        defendPlayer.resetStatus();

        if (!checkWin()) {
            attackPlayer.yourMove();
            defendPlayer.enemyMove();
        }

        updateTableView();
        loggingCurrentState();

        while (true) {
            attackPlayer.resetStatus();
            Card currentCard = attackPlayer.attack();
            updateTableView();

            if (currentCard == null || attackPlayer.isFinishMove()) {
                table.resetTable();
                fillHand(attackPlayer);
                fillHand(defendPlayer);
                attackPlayer.setFinishMove(false);
                return;
            }

            table.setCurrentCard(currentCard);
            loggingCurrentState();
            updateTableView();

            defendPlayer.resetStatus();
            Card answer = defendPlayer.defend(currentCard);
            updateTableView();

            if (answer == null || defendPlayer.isTake()) {
                defendPlayer.getHand().addAll(table.getOldCards());
                defendPlayer.getHand().add(currentCard);
                table.resetTable();
                fillHand(attackPlayer);
                defendPlayer.setTake(false);

                updateTableView();
                move(attackPlayer, defendPlayer);

                return;
            }

            table.setCurrentCard(null);
            table.getOldCards().add(currentCard);
            table.getOldCards().add(answer);
            updateTableView();
            loggingCurrentState();
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

    public void initGame() {
        table = new Table();
        initDeck();
        Collections.shuffle(deck);

        if (secondPlayer == null) {
            secondPlayer = new AIPlayer(this);
        }

        for (int i = 0; i < 6; i++) {
            firstPlayer.getHand().add(deck.remove(0));
            secondPlayer.getHand().add(deck.remove(0));
        }
    }

    private void updateTableView() {
        updateTableViewForPlayer(firstPlayer, secondPlayer);

        if (secondPlayer instanceof RealPlayer) {
            updateTableViewForPlayer(secondPlayer, firstPlayer);
        }
    }

    private void updateTableViewForPlayer(Player player, Player enemy) {
        TableMessage message = new TableMessage();

        StringBuilder cardsForPlayer = new StringBuilder();
        if (!player.getHand().isEmpty()) {
            for (Card card : player.getHand()) {
                cardsForPlayer.append(card.getName());
                cardsForPlayer.append(" ");
            }
            cardsForPlayer.delete(cardsForPlayer.length() - 1, cardsForPlayer.length());
        }

        message.setPlayerCards(cardsForPlayer.toString());
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

        player.sendMessage(message);
    }

    private void sendGameOver() {
        if (isDraw()) {
            firstPlayer.sendMessage(new DefaultMessage(MessageType.DRAW));
            secondPlayer.sendMessage(new DefaultMessage(MessageType.DRAW));
        } else if (firstPlayer.isWin()) {
            firstPlayer.sendMessage(new DefaultMessage(MessageType.WIN));
            secondPlayer.sendMessage(new DefaultMessage(MessageType.LOSE));
        } else if (secondPlayer.isWin()) {
            firstPlayer.sendMessage(new DefaultMessage(MessageType.LOSE));
            secondPlayer.sendMessage(new DefaultMessage(MessageType.WIN));
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
            player.getHand().add(deck.remove(0));
        }

        if (player.getHand().size() < 6 && deck.isEmpty() && trump != null) {
            player.getHand().add(trump);
            trump = null;
        }
    }

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

    private void updateStatistics() {

        updateStatisticsForPlayer(firstPlayer);

        if (secondPlayer instanceof RealPlayer) {
            updateStatisticsForPlayer(secondPlayer);
        }
    }

    private void updateStatisticsForPlayer(Player player) {
        if (player.getUsername() == null) {
            return;
        }

        User user = userService.findByUsername(player.getUsername());

        int totalGames = user.getTotalGames();
        user.setTotalGames(++totalGames);

        if (player.isWin()) {
            int oldWins = user.getWins();
            user.setWins(++oldWins);
        } else {
            int oldLoses = user.getLoses();
            user.setLoses(++oldLoses);
        }

        userService.save(user);
    }

    private void initDeck() {
        boolean clubsIsTrumps = false;
        boolean diamondsIsTrumps = false;
        boolean heartsIsTrumps = false;
        boolean spadesIsTrumps = false;

        deck = new ArrayList<>();
        trump = selectTrump();

        switch (trump.getSuit()) {
            case CLUBS:
                clubsIsTrumps = true;
                break;
            case DIAMONDS:
                diamondsIsTrumps = true;
                break;
            case HEARTS:
                heartsIsTrumps = true;
                break;
            case SPADES:
                spadesIsTrumps = true;
                break;
        }

        deck.add(new Card("c1", Suit.CLUBS, 1, clubsIsTrumps));
        deck.add(new Card("c2", Suit.CLUBS, 2, clubsIsTrumps));
        deck.add(new Card("c3", Suit.CLUBS, 3, clubsIsTrumps));
        deck.add(new Card("c4", Suit.CLUBS, 4, clubsIsTrumps));
        deck.add(new Card("c5", Suit.CLUBS, 5, clubsIsTrumps));
        deck.add(new Card("c6", Suit.CLUBS, 6, clubsIsTrumps));
        deck.add(new Card("c7", Suit.CLUBS, 7, clubsIsTrumps));
        deck.add(new Card("c8", Suit.CLUBS, 8, clubsIsTrumps));
        deck.add(new Card("c9", Suit.CLUBS, 9, clubsIsTrumps));

        deck.add(new Card("d1", Suit.DIAMONDS, 1, diamondsIsTrumps));
        deck.add(new Card("d2", Suit.DIAMONDS, 2, diamondsIsTrumps));
        deck.add(new Card("d3", Suit.DIAMONDS, 3, diamondsIsTrumps));
        deck.add(new Card("d4", Suit.DIAMONDS, 4, diamondsIsTrumps));
        deck.add(new Card("d5", Suit.DIAMONDS, 5, diamondsIsTrumps));
        deck.add(new Card("d6", Suit.DIAMONDS, 6, diamondsIsTrumps));
        deck.add(new Card("d7", Suit.DIAMONDS, 7, diamondsIsTrumps));
        deck.add(new Card("d8", Suit.DIAMONDS, 8, diamondsIsTrumps));
        deck.add(new Card("d9", Suit.DIAMONDS, 9, diamondsIsTrumps));

        deck.add(new Card("h1", Suit.HEARTS, 1, heartsIsTrumps));
        deck.add(new Card("h2", Suit.HEARTS, 2, heartsIsTrumps));
        deck.add(new Card("h3", Suit.HEARTS, 3, heartsIsTrumps));
        deck.add(new Card("h4", Suit.HEARTS, 4, heartsIsTrumps));
        deck.add(new Card("h5", Suit.HEARTS, 5, heartsIsTrumps));
        deck.add(new Card("h6", Suit.HEARTS, 6, heartsIsTrumps));
        deck.add(new Card("h7", Suit.HEARTS, 7, heartsIsTrumps));
        deck.add(new Card("h8", Suit.HEARTS, 8, heartsIsTrumps));
        deck.add(new Card("h9", Suit.HEARTS, 9, heartsIsTrumps));

        deck.add(new Card("s1", Suit.SPADES, 1, spadesIsTrumps));
        deck.add(new Card("s2", Suit.SPADES, 2, spadesIsTrumps));
        deck.add(new Card("s3", Suit.SPADES, 3, spadesIsTrumps));
        deck.add(new Card("s4", Suit.SPADES, 4, spadesIsTrumps));
        deck.add(new Card("s5", Suit.SPADES, 5, spadesIsTrumps));
        deck.add(new Card("s6", Suit.SPADES, 6, spadesIsTrumps));
        deck.add(new Card("s7", Suit.SPADES, 7, spadesIsTrumps));
        deck.add(new Card("s8", Suit.SPADES, 8, spadesIsTrumps));
        deck.add(new Card("s9", Suit.SPADES, 9, spadesIsTrumps));

        deck.remove(trump);
    }

    private Card selectTrump() {
        Card trump = null;

        int trumpValue = (int) (Math.random() * 9) + 1;
        int trumpSuitNumber = (int) (Math.random() * 4);
        String trumpName;

        switch (trumpSuitNumber) {
            case (0):
                trumpName = "c" + trumpValue;
                trump = new Card(trumpName, Suit.CLUBS, trumpValue, true);
                break;
            case (1):
                trumpName = "d" + trumpValue;
                trump = new Card(trumpName, Suit.DIAMONDS, trumpValue, true);
                break;
            case (2):
                trumpName = "h" + trumpValue;
                trump = new Card(trumpName, Suit.HEARTS, trumpValue, true);
                break;
            case (3):
                trumpName = "s" + trumpValue;
                trump = new Card(trumpName, Suit.SPADES, trumpValue, true);
                break;
        }

        return trump;
    }

    public void sendChatMessage(ChatMessage message) {
        firstPlayer.sendMessage(message);
        secondPlayer.sendMessage(message);
    }

}
