package ru.romanov.durak.objects;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import ru.romanov.durak.objects.players.AIPlayer;
import ru.romanov.durak.objects.players.RealPlayer;

public class GameTest {

    private Game game;

    @Before
    public void testInit() {
        game = new Game();

        RealPlayer firstPlayer = new RealPlayer();
        firstPlayer.setUsername("admin");
        game.setFirstPlayer(new RealPlayer());

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
