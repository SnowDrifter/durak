package ru.romanov.durak.model;

import com.google.common.collect.HashBiMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.romanov.durak.controller.websocket.message.Message;
import ru.romanov.durak.controller.websocket.message.MessageType;
import ru.romanov.durak.controller.websocket.message.lobby.LobbyChangeMessage;
import ru.romanov.durak.controller.websocket.message.lobby.LobbyStateMessage;
import ru.romanov.durak.util.JsonHelper;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class Lobby {

    private static HashBiMap<String, WebSocketSession> lobby = HashBiMap.create();

    public void addUser(String username, WebSocketSession session) {
        lobby.put(username, session);
        sendMessageToAll(new LobbyChangeMessage(MessageType.LOBBY_USER_CONNECTED, username), username);
    }

    public void removeByUsername(String username) {
        lobby.remove(username);
        sendMessageToAll(new LobbyChangeMessage(MessageType.LOBBY_USER_DISCONNECTED, username), null);
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

    public void sendMessageToAll(Message message, String exceptUsername) {
        String json = JsonHelper.convertObject(message);
        TextMessage textMessage = new TextMessage(json);

        for (Map.Entry<String, WebSocketSession> session : lobby.entrySet()) {
            if(session.getKey().equals(exceptUsername)) {
                continue;
            }
            sendMessage(textMessage, session.getValue());
        }
    }

    public void sendLobbyState(String username, WebSocketSession session) {
        Set<String> usernames = getUsernames().stream()
                .filter(u -> !u.equals(username))
                .collect(Collectors.toSet());

        String json = JsonHelper.convertObject(new LobbyStateMessage(usernames));
        TextMessage textMessage = new TextMessage(json);

        sendMessage(textMessage, session);
    }

    private void sendMessage(TextMessage textMessage, WebSocketSession session) {
        if (session.isOpen()) {
            try {
                session.sendMessage(textMessage);
            } catch (IOException e) {
                log.error("Cannot send message, reason: " + e.getMessage());
            }
        }
    }

}
