package ru.romanov.durak.websocket;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.romanov.durak.websocket.message.CardMessage;
import ru.romanov.durak.websocket.message.Message;
import ru.romanov.durak.game.Game;
import ru.romanov.durak.model.player.HumanPlayer;
import ru.romanov.durak.util.JsonHelper;

@Slf4j
public class SingleplayerWebSocket extends TextWebSocketHandler {

    @Autowired
    private WebSocketService webSocketService;
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
        webSocketService.addSession(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        webSocketService.removeSession(session.getId());
    }

    private void startSingleplayerGame(WebSocketSession session) {
        game = new Game();
        HumanPlayer player = new HumanPlayer();
        player.setUsername(session.getId());
        player.setGame(game);
        game.setFirstPlayer(player);
        game.setWebSocketService(webSocketService);
        game.initGame();
        new Thread(game).start();
    }

}
