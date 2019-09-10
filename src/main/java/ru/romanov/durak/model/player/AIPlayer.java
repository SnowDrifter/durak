package ru.romanov.durak.model.player;


import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.romanov.durak.game.Game;
import ru.romanov.durak.model.Card;
import ru.romanov.durak.model.Table;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class AIPlayer extends Player {

    public AIPlayer(Game game) {
        this.game = game;
    }

    @Override
    public Card attack() {
        if (game.checkWin()) {
            return null;
        }

        List<Card> oldCardsOnTable = game.getTable().getOldCards();

        Card result = null;
        if (oldCardsOnTable.isEmpty()) {
            result = hand.iterator().next();

            for (Card card : hand) {
                if (result.isStronger(card)) {
                    result = card;
                    break;
                }
            }
        } else {
            Table table = game.getTable();
            Set<Integer> oldCardsPower = table.getOldCards()
                    .stream()
                    .map(Card::getPower)
                    .collect(Collectors.toSet());

            for (Card card : hand) {
                if (oldCardsPower.contains(card.getPower())) {
                    result = card;
                    break;
                }
            }
        }

        if (result != null) {
            hand.remove(result);
        } else {
            setFinishMove(true);
        }

        return result;
    }

    @Override
    public Card defend(Card enemyCard) {
        if (game.checkWin()) {
            return null;
        }

        Card result = null;
        for (Card card : hand) {
            if (card.isStronger(enemyCard)) {
                result = card;
                break;
            }
        }

        if (result != null) {
            hand.remove(result);
        } else {
            setTake(true);
        }

        return result;
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

}
