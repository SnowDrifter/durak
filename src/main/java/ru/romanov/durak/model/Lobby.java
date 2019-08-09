package ru.romanov.durak.model;

import com.google.common.collect.HashBiMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.romanov.durak.controller.websocket.message.LobbyStateMessage;
import ru.romanov.durak.controller.websocket.message.Message;
import ru.romanov.durak.util.JsonHelper;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@Slf4j
public class Lobby {

    private static HashBiMap<String, WebSocketSession> lobby = HashBiMap.create();

    public void addPlayer(String username, WebSocketSession session) {
        lobby.put(username, session);
    }

    public void removeBySession(WebSocketSession session) {
        lobby.inverse().remove(session);
    }

    public void removeByUsername(String username) {
        lobby.remove(username);
    }

    public WebSocketSession getSessionByUsername(String username) {
        return lobby.getOrDefault(username, null);
    }

    public Set<String> getUsernames() {
        if (lobby.isEmpty()) {
            return Collections.emptySet();
        } else {
            return lobby.keySet();
        }
    }

    public void sendMessageToAll(Message message) {
        String json = JsonHelper.convertObject(message);
        TextMessage textMessage = new TextMessage(json);

        for (WebSocketSession session : lobby.values()) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(textMessage);
                } catch (IOException e) {
                    log.error("Cannot send message, reason: " + e.getMessage());
                }
            }
        }
    }

    public void updateLobbyView() {
        sendMessageToAll(new LobbyStateMessage(getUsernames()));
    }

}
