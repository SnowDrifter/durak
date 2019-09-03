package ru.romanov.durak.lobby;

import org.springframework.web.socket.WebSocketSession;
import ru.romanov.durak.websocket.message.Message;

public interface LobbyService {

    void addUser(String username, WebSocketSession session);

    void removeByUsername(String username);

    WebSocketSession getSessionByUsername(String username);

    void sendMessageToAll(Message message, String exceptUsername);

    void sendLobbyState(String username, WebSocketSession session);

}
