package ru.romanov.durak.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.romanov.durak.util.JsonHelper;
import ru.romanov.durak.websocket.message.Message;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class WebSocketServiceImpl implements WebSocketService {

    private final Map<String, WebSocketSession> sessions = new HashMap<>();

    @Override
    public void addSession(String username, WebSocketSession session) {
        sessions.put(username, session);
    }

    @Override
    public void removeSession(String username) {
        sessions.remove(username);
    }

    @Override
    public void sendMessage(String username, Message message) {
        WebSocketSession session = sessions.get(username);
        sendMessage(session, message);
    }

    @Override
    public void sendMessage(Set<String> usernames, Message message) {
        for (String username : usernames) {
            sendMessage(username, message);
        }
    }

    private void sendMessage(WebSocketSession session, Message message) {
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
