package ru.romanov.durak.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import ru.romanov.durak.model.player.AIPlayer;
import ru.romanov.durak.model.player.HumanPlayer;

public class GameTest {

    private Game game;

    @Before
    public void testInit() {
        game = new Game();

        HumanPlayer firstPlayer = new HumanPlayer();
        firstPlayer.setUsername("admin");
        game.setFirstPlayer(new HumanPlayer());

        game.initGame();
    }

    @Test
    public void testCardCount(){
        assertEquals(6, game.getFirstPlayer().getHand().size());
        assertEquals(6, game.getSecondPlayer().getHand().size());
        assertEquals(23, game.getDeck().size());
        assertNotNull(game.getTrump());
    }

    @Test
    public void testCheckWin(){
        assertFalse(game.checkWin());
    }


    @Test
    public void testTrump(){
        assertTrue(game.getTrump().isTrump());
    }

    @Test
    public void testAIPlayerClass(){
        assertTrue(game.getSecondPlayer() instanceof AIPlayer);
    }

}
