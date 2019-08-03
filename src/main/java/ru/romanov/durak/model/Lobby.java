package ru.romanov.durak.model;

import com.google.common.collect.HashBiMap;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.romanov.durak.controller.websocket.message.LobbyMessage;
import ru.romanov.durak.controller.websocket.message.Message;
import ru.romanov.durak.util.JsonHelper;

import java.io.IOException;

public class Lobby {

    private static final Logger logger = LogManager.getLogger(Lobby.class);
    private static HashBiMap<String, WebSocketSession> lobby = HashBiMap.create();

    public synchronized void addPlayer(String username, WebSocketSession session) {
        lobby.put(username, session);
    }

    public synchronized void removeBySession(WebSocketSession session) {
        lobby.inverse().remove(session);
    }

    public void removeByUsername(String username) {
        lobby.remove(username);
    }

    public WebSocketSession getSessionByUsername(String username) {
        return lobby.getOrDefault(username, null);
    }

    public synchronized String getStringWithUsernames() {
        if (lobby.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder();

        for (String username : lobby.keySet()) {
            result.append(username);
            result.append(",");
        }
        result.delete(result.length() - 1, result.length());
        return result.toString();
    }

    public void sendMessageToAll(Message message) {
        String json = JsonHelper.convertObject(message);
        TextMessage textMessage = new TextMessage(json);

        for (WebSocketSession session : lobby.values()) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(textMessage);
                } catch (IOException e) {
                    logger.error("Cannot send message, reason: " + e.getMessage());
                }
            }
        }

    }

    public void updateLobbyView() {
        LobbyMessage message = new LobbyMessage();
        message.setUsernames(getStringWithUsernames());
        sendMessageToAll(message);
    }

}
