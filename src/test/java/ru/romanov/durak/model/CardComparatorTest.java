package ru.romanov.durak.model;

import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertThat;

public class CardComparatorTest {

    private final List<Card> expected = new ArrayList<>();
    private final Card d2 = new Card("d2", Suit.DIAMONDS, 2, false);
    private final Card h1 = new Card("h1", Suit.HEARTS, 1, false);
    private final Card h2 = new Card("h2", Suit.HEARTS, 2, false);
    private final Card s3 = new Card("s3", Suit.SPADES, 3, false);
    private final Card c1 = new Card("c1", Suit.CLUBS, 1, true);

    @Before
    public void init() {
        expected.add(d2);
        expected.add(h1);
        expected.add(h2);
        expected.add(s3);
        expected.add(c1);
    }

    @Test
    public void comparatorTest() {
        Set<Card> cards = new TreeSet<>(new CardComparator());
        cards.add(c1);
        cards.add(h2);
        cards.add(h1);
        cards.add(d2);
        cards.add(s3);

        assertThat(cards, IsIterableContainingInOrder.contains(expected.toArray()));
    }

}
