package ru.romanov.durak.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.romanov.durak.websocket.message.Message;

import java.io.IOException;

@Slf4j
public class WebSocketHelper {

    public static void sendMessage(Message message, WebSocketSession session) {
        if (session != null && session.isOpen()) {
            String json = JsonHelper.convertObject(message);
            TextMessage textMessage = new TextMessage(json);

            try {
                session.sendMessage(textMessage);
            } catch (IOException e) {
                log.error("Cannot send message, reason: " + e.getMessage());
            }
        }
    }

}
