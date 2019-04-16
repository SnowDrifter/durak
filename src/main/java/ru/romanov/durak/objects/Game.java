package ru.romanov.durak.objects;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import ru.romanov.durak.objects.players.AIPlayer;
import ru.romanov.durak.objects.players.Player;
import ru.romanov.durak.objects.players.RealPlayer;
import ru.romanov.durak.users.models.User;
import ru.romanov.durak.users.service.UserService;
import ru.romanov.durak.objects.players.AIPlayer;
import ru.romanov.durak.objects.players.Player;
import ru.romanov.durak.objects.players.RealPlayer;
import ru.romanov.durak.users.models.User;
import ru.romanov.durak.users.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Component
public class Game implements Runnable {

    private static final Logger logger = LogManager.getLogger(Game.class);

    private UserService userService;
    private Player firstPlayer;
    private Player secondPlayer;
    private List<Card> deck;
    private Card trump;
    private Table table;
    private boolean draw;

    @Override
    public void run() {
        logger.info("Start game!");

        while (true) {
            if (checkWin()) break;
            move(firstPlayer, secondPlayer);
            loggingCurrentState();

            if (checkWin()) break;
            move(secondPlayer, firstPlayer);
            loggingCurrentState();
        }

        logger.info("Game over!");
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

    public void requestFromPlayer(String username, String request) {

        Player player = findPlayer(username);

        if (request.startsWith("selectcard")) {
            String cardName = request.replace("selectcard=", "");
            player.selectCard(cardName);
        } else if (request.startsWith("take")) {
            player.setTake(true);
        } else if (request.startsWith("finishMove")) {
            player.setFinishMove(true);
        }
    }

    private Player findPlayer(String username){
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

    public void updateTableView() {

        updateTableViewForPlayer(firstPlayer, secondPlayer);

        if (secondPlayer instanceof RealPlayer) {
            updateTableViewForPlayer(secondPlayer, firstPlayer);
        }

    }

    private void updateTableViewForPlayer(Player player, Player enemy) {
        String result = "update:";
        StringBuilder cardsForPlayer = new StringBuilder();

        if (!player.getHand().isEmpty()) {
            for (Card c : player.getHand()) {
                cardsForPlayer.append(c.getName());
                cardsForPlayer.append(" ");
            }
            cardsForPlayer.delete(cardsForPlayer.length() - 1, cardsForPlayer.length());
        }

        result += "playerCards=" + cardsForPlayer.toString() + ",";
        result += "enemyCardsCount=" + String.valueOf(enemy.getHand().size()) + ",";
        if (trump != null) {
            result += "trump=" + trump.getName() + ",";
        }
        if (deck != null) {
            result += "sizeOfDeck=" + String.valueOf(deck.size()) + ",";
        }
        if (!table.isClean()) {
            result += "table=" + table.getCardNames();
        }
        player.sendMessage(result);
    }

    private void sendGameOver() {
        if (isDraw()) {
            firstPlayer.sendMessage("draw");
            secondPlayer.sendMessage("draw");
        } else if (firstPlayer.isWin()) {
            firstPlayer.sendMessage("win");
            secondPlayer.sendMessage("lose");
        } else if (secondPlayer.isWin()) {
            firstPlayer.sendMessage("lose");
            secondPlayer.sendMessage("win");
        }
    }

    // TODO AOP. Maybe remove it?
    private void loggingCurrentState() {
        logger.debug("========================");
        logger.debug("First player hand: " + firstPlayer.getHand());
        logger.debug("Second player hand: " + secondPlayer.getHand());
        if (deck != null) {
            logger.debug("Current deck: " + deck);
        }
        if (trump != null) {
            logger.debug("Trump: " + trump.getName());
        }
        logger.debug("------------------------");
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
        if(player.getUsername() == null){
            return;
        }

        User user = userService.findByUsername(player.getUsername());

        int totalGames = user.getTotalGames();
        user.setTotalGames(++totalGames);

        if (isDraw()) {
            int oldLoses = user.getLoses();
            user.setLoses(++oldLoses);
        } else if (player.isWin()) {
            int oldWins = user.getWins();
            user.setWins(++oldWins);
        } else {
            int oldLoses = user.getLoses();
            user.setLoses(++oldLoses);
        }

        userService.save(user);
    }

    public void initDeck() {
        boolean clubsIsTrumps = false;
        boolean diamondsIsTrumps = false;
        boolean heartsIsTrumps = false;
        boolean spadesIsTrumps = false;

        deck = new ArrayList<>();
        trump = selectTrump();

        switch(trump.getSuit()){
            case CLUBS: clubsIsTrumps = true; break;
            case DIAMONDS: diamondsIsTrumps = true; break;
            case HEARTS: heartsIsTrumps = true; break;
            case SPADES: spadesIsTrumps = true; break;
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

    private Card selectTrump(){
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

    // TODO ?!
    public Game() {
    }

    public void sendChatMessage(String message) {
        firstPlayer.sendMessage(message);
        secondPlayer.sendMessage(message);
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(Player firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(Player secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void setDeck(List<Card> deck) {
        this.deck = deck;
    }

    public Card getTrump() {
        return trump;
    }

    public void setTrump(Card trump) {
        this.trump = trump;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
