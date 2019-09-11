package ru.romanov.durak.model.player;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.romanov.durak.model.Card;
import ru.romanov.durak.game.Game;
import ru.romanov.durak.model.Suit;
import ru.romanov.durak.model.Table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class AIPlayerTest {

    private Game game;
    private AIPlayer player;

    @Before
    public void setup() {
        game = Mockito.mock(Game.class);
        when(game.checkWin()).thenReturn(false);

        Table table = new Table();
        when(game.getTable()).thenReturn(table);

        game.setTable(new Table());

        player = new AIPlayer();

        Set<Card> playerHand = new HashSet<>();
        playerHand.add(new Card("h6", Suit.HEARTS, 6, false));
        playerHand.add(new Card("s2", Suit.SPADES, 2, true));

        player.setHand(playerHand);
    }

    @Test
    public void testFirstAttack(){
        Card card = player.attack(new ArrayList<>());

        assertNotNull(card);
        assertEquals("h6", card.getName());
        assertFalse(card.isTrump());
    }

    @Test
    public void testAttackWithCardsOnTable(){
        List<Card> oldCards = new ArrayList<>();
        oldCards.add(new Card("d3", Suit.DIAMONDS, 3, false));
        oldCards.add(new Card("d6", Suit.DIAMONDS, 6, false));
        game.getTable().setOldCards(oldCards);

        Card card = player.attack(oldCards);

        assertEquals("h6", card.getName());
        assertEquals(6, card.getPower());
        assertFalse(card.isTrump());
    }

    @Test
    public void testAnotherAttackWithCardsOnTable(){
        List<Card> oldCards = new ArrayList<>();
        oldCards.add(new Card("d2", Suit.DIAMONDS, 2, false));
        oldCards.add(new Card("d5", Suit.DIAMONDS, 5, false));
        game.getTable().setOldCards(oldCards);

        Card card = player.attack(oldCards);

        assertEquals("s2", card.getName());
        assertEquals(2, card.getPower());
        assertTrue(card.isTrump());
    }

    @Test
    public void testFailAttack(){
        List<Card> oldCards = new ArrayList<>();
        oldCards.add(new Card("d4", Suit.DIAMONDS, 4, false));
        game.getTable().setOldCards(oldCards);

        Card card = player.attack(oldCards);

        assertNull(card);
        assertTrue(player.isFinishMove());
    }


    @Test
    public void testDefendVersusSimpleCard(){
        Card card = player.defend(new Card("h4", Suit.HEARTS, 4, false ));

      assertNotNull(card);
    }


    @Test
    public void testDefendVersusTrump(){
        Card card = player.defend(new Card("s1", Suit.SPADES, 1, true ));

        assertEquals("s2", card.getName());
        assertEquals(2, card.getPower());
        assertEquals(true, card.isTrump());
    }

    @Test
    public void testFailDefend(){
        Card card = player.defend(new Card("s3", Suit.SPADES, 3, true ));

        assertNull(card);
        assertTrue(player.isTake());
    }

}
