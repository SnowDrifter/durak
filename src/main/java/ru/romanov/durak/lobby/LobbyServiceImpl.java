package ru.romanov.durak.lobby;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.romanov.durak.websocket.WebSocketService;
import ru.romanov.durak.websocket.message.Message;
import ru.romanov.durak.websocket.message.MessageType;
import ru.romanov.durak.websocket.message.lobby.LobbyChangeMessage;
import ru.romanov.durak.websocket.message.lobby.LobbyStateMessage;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class LobbyServiceImpl implements LobbyService {

    @Autowired
    private WebSocketService webSocketService;
    private final Set<String> lobbyUsernames = new HashSet<>();

    @Override
    public void addUser(String username) {
        lobbyUsernames.add(username);
        sendMessageToAll(new LobbyChangeMessage(MessageType.LOBBY_USER_CONNECTED, username), username);
    }

    @Override
    public void removeByUsername(String username) {
        lobbyUsernames.remove(username);
        sendMessageToAll(new LobbyChangeMessage(MessageType.LOBBY_USER_DISCONNECTED, username), null);
    }

    @Override
    public void sendMessageToAll(Message message, String exceptUsername) {
        Set<String> usernames = lobbyUsernames.stream()
                .filter(u -> !u.equals(exceptUsername))
                .collect(Collectors.toSet());

        webSocketService.sendMessage(usernames, message);
    }

    @Override
    public void sendLobbyState(String username) {
        Set<String> usernames = lobbyUsernames.stream()
                .filter(u -> !u.equals(username))
                .collect(Collectors.toSet());

        webSocketService.sendMessage(username, new LobbyStateMessage(usernames));
    }

}
