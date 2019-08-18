package ru.romanov.durak.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Table {

    private List<Card> oldCards = new ArrayList<>();
    private Card currentCard;

    public void resetTable() {
        oldCards.clear();
        currentCard = null;
    }

    public List<String> getCardNames() {
        List<String> result = new ArrayList<>();
        for (Card card : oldCards) {
            result.add(card.getName());
        }

        if (currentCard != null) {
            result.add(currentCard.getName());
        }

        return result;
    }

    public List<Card> getAllCardsOnTable() {
        List<Card> result = new ArrayList<>(oldCards);
        result.add(currentCard);
        return result;
    }

    public boolean isClean() {
        return oldCards.isEmpty() && currentCard == null;
    }

}
