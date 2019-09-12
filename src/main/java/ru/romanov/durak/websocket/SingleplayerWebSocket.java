package ru.romanov.durak.websocket;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.romanov.durak.game.GameService;
import ru.romanov.durak.websocket.message.Message;
import ru.romanov.durak.util.JsonHelper;

@Slf4j
public class SingleplayerWebSocket extends TextWebSocketHandler {

    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private GameService gameService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) {
        Message message = JsonHelper.parseJson(textMessage.getPayload(), Message.class);
        gameService.processSingleplayerMessage(session.getId(), message);
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

}
