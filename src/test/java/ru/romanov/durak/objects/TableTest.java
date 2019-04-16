package ru.romanov.durak.objects;


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TableTest {

    private Table table;

    @Before
    public void testInit(){
        table = new Table();
        table.setCurrentCard(new Card("1c", Suit.CLUBS, 1, false));

        List<Card> temp = new ArrayList<>();
        temp.add(new Card("3h", Suit.HEARTS, 3, true));
        temp.add(new Card("7s", Suit.SPADES, 7, false));
        table.setOldCards(temp);
    }

    @Test
    public void testGettersInTable() {
        assertNotNull(table.getCardNames());

        assertNotNull(table.getOldCards());
        assertEquals(2, table.getOldCards().size());

        assertNotNull(table.getCurrentCard());

        assertNotNull(table.getAllCardsOnTable());
        assertEquals(3, table.getAllCardsOnTable().size());
    }

    @Test
    public void testClean(){
        assertFalse(table.isClean());
        table.resetTable();
        assertTrue(table.isClean());
    }

}
