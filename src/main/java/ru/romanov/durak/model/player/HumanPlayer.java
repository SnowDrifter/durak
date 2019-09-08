package ru.romanov.durak.model.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.romanov.durak.model.Card;

import java.util.List;

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
        if (game.checkWin()) {
            return null;
        }

        List<Card> oldCardsOnTable = game.getTable().getOldCards();

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
            return Card.INVALID_CARD;
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
            return Card.INVALID_CARD;
        }


        return null;
    }

    private boolean checkDefendCard(Card trying, Card tempEnemyCard) {
        return (tempEnemyCard.getPower() < trying.getPower() && trying.isTrump()) ||
                (tempEnemyCard.getSuit() == trying.getSuit() && tempEnemyCard.getPower() < trying.getPower());
    }
}
