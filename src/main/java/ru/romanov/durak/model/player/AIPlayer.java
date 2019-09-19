package ru.romanov.durak.model.player;


import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.romanov.durak.model.Card;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class AIPlayer extends Player {

    @Override
    public Card attack(List<Card> oldCards) {
        Card result = null;
        if (oldCards.isEmpty()) {
            result = hand.iterator().next();

            for (Card card : hand) {
                if (result.isStronger(card)) {
                    result = card;
                    break;
                }
            }
        } else {
            Set<Integer> oldCardsPower = oldCards
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
        return "AIPlayer";
    }

    @Override
    public void selectCard(String cardName) {
        // Not necessary for AIPlayer
    }

}
