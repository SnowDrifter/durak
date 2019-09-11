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

    public HumanPlayer(String username) {
        this.username = username;
    }

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
    public Card attack(List<Card> oldCards) {
        if (lastClickedCard == null) {
            return null;
        }

        Card result = null;
        if (oldCards.isEmpty()) {
            // Select card for empty table.
            result = lastClickedCard;
        } else {
            Set<Integer> oldCardsPower = oldCards.stream()
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
        if (lastClickedCard == null) {
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
