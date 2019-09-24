package ru.romanov.durak.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Comparator;

@Data
@AllArgsConstructor
public class Card {

    public static final Card INVALID_CARD = new Card("invalid", null, -1, false);
    public static final Comparator<Card> CARD_COMPARATOR = (c1, c2) -> {
        if (c1.equals(c2)) {
            return 0;
        }
        return c1.isStronger(c2) ? 1 : -1;
    };

    private final String name;
    private final Suit suit;
    private final int power;
    private final boolean trump;

    public boolean isStronger(Card other) {
        boolean strongerByPower = (this.getPower() + (this.isTrump() ? 10 : 0) > (other.getPower() + (other.isTrump() ? 10 : 0)));
        boolean correctSuit = (this.isTrump() || (this.getSuit().equals(other.getSuit()) && !other.isTrump()));
        return strongerByPower && correctSuit;
    }

}
