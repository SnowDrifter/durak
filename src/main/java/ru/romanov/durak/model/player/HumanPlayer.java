package ru.romanov.durak.model.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.romanov.durak.model.Card;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class HumanPlayer extends Player {

    private String username;
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
        if (game.checkWin() || lastClickedCard == null) {
            return null;
        }

        List<Card> oldCardsOnTable = game.getTable().getOldCards();

        Card result = null;
        if (oldCardsOnTable.isEmpty()) {
            // Select card for empty table.
            result = lastClickedCard;
        } else {
            Set<Integer> oldCardsPower = oldCardsOnTable.stream()
                    .map(Card::getPower)
                    .collect(Collectors.toSet());

            if (oldCardsPower.contains(lastClickedCard.getPower())) {
                result = lastClickedCard;
            }
        }

        if (result != null) {
            hand.remove(lastClickedCard);
        } else {
            result = Card.INVALID_CARD;
        }

        lastClickedCard = null;
        return result;
    }

    @Override
    public Card defend(Card enemyCard) {
        if (game.checkWin() || lastClickedCard == null) {
            return null;
        }

        Card result;
        if (lastClickedCard.isStronger(enemyCard)) {
            result = lastClickedCard;
            hand.remove(lastClickedCard);
        } else {
            result = Card.INVALID_CARD;
        }

        lastClickedCard = null;
        return result;
    }

}
