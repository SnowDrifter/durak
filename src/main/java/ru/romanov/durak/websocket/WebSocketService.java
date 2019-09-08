package ru.romanov.durak.websocket;

import org.springframework.web.socket.WebSocketSession;
import ru.romanov.durak.websocket.message.Message;

import java.util.Set;

public interface WebSocketService {

    void addSession(String username, WebSocketSession session);

    void removeSession(String username);

    void sendMessage(String username, Message message);

    void sendMessage(Set<String> usernames, Message message);

}
