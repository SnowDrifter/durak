package ru.romanov.durak.model.player;


import lombok.Data;
import ru.romanov.durak.model.Card;
import ru.romanov.durak.model.CardComparator;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Data
public abstract class Player {

    protected final Set<Card> hand = new TreeSet<>(new CardComparator());
    private boolean win;

    abstract public Card attack(List<Card> oldCards);

    abstract public Card defend(Card enemyCard);

    abstract public void selectCard(String cardName);

    abstract public String getUsername();

    public void addToHand(List<Card> cards) {
        hand.addAll(cards);
    }

    public void addToHand(Card card) {
        hand.add(card);
    }

}
