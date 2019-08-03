package ru.romanov.durak.controller.websocket;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.romanov.durak.controller.websocket.message.CardMessage;
import ru.romanov.durak.controller.websocket.message.Message;
import ru.romanov.durak.model.Game;
import ru.romanov.durak.model.player.RealPlayer;
import ru.romanov.durak.util.JsonHelper;

@Slf4j
public class SingleplayerWebSocket extends TextWebSocketHandler {

    private Game game;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) {
        Message message = JsonHelper.parseJson(textMessage.getPayload(), Message.class);

        switch (message.getType()) {
            case START_GAME: {
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
        log.info("Open session " + session.getId());
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
