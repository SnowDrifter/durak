package ru.romanov.durak.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class CardTest {

    private Card c1;
    private Card anotherC1;
    private Card h3;

    @Before
    public void testInit() {
        c1 = new Card("c1", Suit.CLUBS, 1, false);
        anotherC1 = new Card("c1", Suit.CLUBS, 1, false);
        h3 = new Card("h3", Suit.HEARTS, 3, false);
    }

    @Test
    public void testEquals() {
        assertEquals(c1, anotherC1);
        assertEquals(anotherC1, c1);

        assertNotEquals(c1, h3);
    }

    @Test
    public void testHashCode() {
        Set<Card> set = new HashSet<>();

        set.add(c1);
        set.add(anotherC1);
        assertEquals(1, set.size());

        set.add(h3);
        assertEquals(2, set.size());
    }

}
