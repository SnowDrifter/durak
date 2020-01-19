package ru.romanov.durak.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import ru.romanov.durak.game.Game;
import ru.romanov.durak.model.player.AIPlayer;
import ru.romanov.durak.model.player.HumanPlayer;

public class GameTest {

    private Game game;

    @Before
    public void testInit() {
        game = new Game();
        game.initGame(new HumanPlayer("admin"), new AIPlayer());
    }

    @Test
    public void testCardCount(){
        assertEquals(6, game.getAttackPlayer().getHand().size());
        assertEquals(6, game.getDefendPlayer().getHand().size());
//        assertEquals(23, game.getDeck().size());
        assertNotNull(game.getTrump());
    }

    @Test
    public void testCheckWin(){
        assertFalse(game.isGameFinished());
    }

    @Test
    public void testTrump(){
        assertTrue(game.getTrump().isTrump());
    }

    @Test
    public void testAIPlayerClass(){
        assertTrue(game.getDefendPlayer() instanceof AIPlayer);
    }

}
