package ru.romanov.durak.model.player;


import lombok.Data;
import ru.romanov.durak.model.Card;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public abstract class Player {

    protected final Set<Card> hand = new HashSet<>();
    private boolean win;

    abstract public Card attack(List<Card> oldCards);

    abstract public Card defend(Card enemyCard);

    public void addToHand(List<Card> cards) {
        hand.addAll(cards);
    }

    public void addToHand(Card card) {
        hand.add(card);
    }

    abstract public void selectCard(String cardName);

    abstract public String getUsername();

}
