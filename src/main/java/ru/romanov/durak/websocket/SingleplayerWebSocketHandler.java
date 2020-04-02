package ru.romanov.durak.websocket;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.romanov.durak.game.GameService;
import ru.romanov.durak.websocket.message.Message;
import ru.romanov.durak.util.JsonHelper;

@Component
@RequiredArgsConstructor
public class SingleplayerWebSocketHandler extends TextWebSocketHandler {

    private final WebSocketService webSocketService;
    private final GameService gameService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) {
        Message message = JsonHelper.parseJson(textMessage.getPayload(), Message.class);
        gameService.processSingleplayerMessage(session.getId(), message);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        webSocketService.addSession(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        webSocketService.removeSession(session.getId());
    }

}
