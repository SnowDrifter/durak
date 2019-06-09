package ru.romanov.durak.controller.websocket;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.romanov.durak.controller.websocket.message.CardMessage;
import ru.romanov.durak.controller.websocket.message.Message;
import ru.romanov.durak.object.Game;
import ru.romanov.durak.object.player.RealPlayer;
import ru.romanov.durak.util.JsonHelper;


public class SingleplayerWebSocket extends TextWebSocketHandler {

    private static final Logger logger = LogManager.getLogger(SingleplayerWebSocket.class);
    private Game game;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) {
        Message message = JsonHelper.parseJson(textMessage.getPayload(), Message.class);

        switch (message.getType()) {
            case INIT_GAME: {
                startSingleplayerGame(session);
                break;
            }
            case SELECT_CARD: {
                CardMessage cardMessage = (CardMessage) message;
                game.getFirstPlayer().selectCard(cardMessage.getCard());
                break;
            }
            case TAKE_CARD: {
                game.getFirstPlayer().setTake(true);
                break;
            }
            case FINISH_MOVE: {
                game.getFirstPlayer().setFinishMove(true);
                break;
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.info("Open session " + session.getId());
    }

    private void startSingleplayerGame(WebSocketSession session) {
        game = new Game();
        RealPlayer player = new RealPlayer();
        player.setGame(game);
        player.setSession(session);
        game.setFirstPlayer(player);
        game.initGame();
        new Thread(game).start();
    }

}
