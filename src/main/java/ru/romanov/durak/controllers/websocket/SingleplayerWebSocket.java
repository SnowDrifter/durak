package ru.romanov.durak.controllers.websocket;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.romanov.durak.objects.Game;
import ru.romanov.durak.objects.players.RealPlayer;
import ru.romanov.durak.objects.players.RealPlayer;


public class SingleplayerWebSocket extends TextWebSocketHandler {

    private static final Logger logger = LogManager.getLogger(SingleplayerWebSocket.class);
    private Game game;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String request = message.getPayload();

        if (request.equals("initgame")) {
           startSingleplayerGame(session);
        } else if (request.startsWith("selectcard")) {
            request = request.replace("selectcard=", "");
            game.getFirstPlayer().selectCard(request);
        } else if (request.startsWith("take")) {
            game.getFirstPlayer().setTake(true);
        } else if (request.startsWith("finishMove")) {
            game.getFirstPlayer().setFinishMove(true);
        }

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("Open session " + session.getId());
    }

    private void startSingleplayerGame(WebSocketSession session){
        game = new Game();
        RealPlayer player = new RealPlayer();
        player.setGame(game);
        player.setSession(session);
        game.setFirstPlayer(player);
        game.initGame();
        Thread gameThread = new Thread(game);
        gameThread.start();
    }

}
