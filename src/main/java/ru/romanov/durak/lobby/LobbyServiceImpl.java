package ru.romanov.durak.lobby;

import com.google.common.collect.HashBiMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import ru.romanov.durak.util.WebSocketHelper;
import ru.romanov.durak.websocket.message.Message;
import ru.romanov.durak.websocket.message.MessageType;
import ru.romanov.durak.websocket.message.lobby.LobbyChangeMessage;
import ru.romanov.durak.websocket.message.lobby.LobbyStateMessage;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
public class LobbyServiceImpl implements LobbyService {

    private final HashBiMap<String, WebSocketSession> sessions = HashBiMap.create();

    @Override
    public void addUser(String username, WebSocketSession session) {
        sessions.put(username, session);
        sendMessageToAll(new LobbyChangeMessage(MessageType.LOBBY_USER_CONNECTED, username), username);
    }

    @Override
    public void removeByUsername(String username) {
        sessions.remove(username);
        sendMessageToAll(new LobbyChangeMessage(MessageType.LOBBY_USER_DISCONNECTED, username), null);
    }

    @Override
    public WebSocketSession getSessionByUsername(String username) {
        return sessions.getOrDefault(username, null);
    }

    @Override
    public void sendMessageToAll(Message message, String exceptUsername) {
        for (Map.Entry<String, WebSocketSession> session : sessions.entrySet()) {
            if (session.getKey().equals(exceptUsername)) {
                continue;
            }
            WebSocketHelper.sendMessage(message, session.getValue());
        }
    }

    @Override
    public void sendLobbyState(String username, WebSocketSession session) {
        Set<String> usernames = getUsernames().stream()
                .filter(u -> !u.equals(username))
                .collect(Collectors.toSet());

        WebSocketHelper.sendMessage(new LobbyStateMessage(usernames), session);
    }

    private Set<String> getUsernames() {
        if (sessions.isEmpty()) {
            return Collections.emptySet();
        } else {
            return sessions.keySet();
        }
    }

}
