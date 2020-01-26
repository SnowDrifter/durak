package ru.romanov.durak.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.romanov.durak.game.Game;
import ru.romanov.durak.model.player.AIPlayer;
import ru.romanov.durak.model.player.HumanPlayer;
import ru.romanov.durak.websocket.WebSocketServiceImpl;

public class GameTest {

    private Game game;

    @Before
    public void testInit() {
        game = new Game();
        game.setWebSocketService(Mockito.mock(WebSocketServiceImpl.class));
        game.initGame(new HumanPlayer("admin"), new AIPlayer());
    }

    @Test
    public void testCardCount(){
        assertEquals(6, game.getAttackPlayer().getHand().size());
        assertEquals(6, game.getDefendPlayer().getHand().size());
        assertEquals(23, game.getDeck().size());
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
