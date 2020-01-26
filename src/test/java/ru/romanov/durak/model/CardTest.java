package ru.romanov.durak.model;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class CardTest {

    private final Card c1 = new Card("c1", Suit.CLUBS, 1, false);
    private final Card anotherC1 = new Card("c1", Suit.CLUBS, 1, false);
    private final Card h3 = new Card("h3", Suit.HEARTS, 3, false);

    @Test
    public void testEquals() {
        assertEquals(c1, anotherC1);
        assertEquals(anotherC1, c1);

        assertNotEquals(c1, h3);
    }

    @Test
    public void testHashCode() {
        Set<Card> cards = new HashSet<>();

        cards.add(c1);
        cards.add(anotherC1);
        assertEquals(1, cards.size());

        cards.add(h3);
        assertEquals(2, cards.size());
    }

}
