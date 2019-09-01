package ru.romanov.durak.model;

import com.google.common.collect.*;
import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpirationListener;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.romanov.durak.websocket.message.Message;
import ru.romanov.durak.websocket.message.MessageType;
import ru.romanov.durak.websocket.message.InviteMessage;
import ru.romanov.durak.websocket.message.lobby.LobbyChangeMessage;
import ru.romanov.durak.websocket.message.lobby.LobbyStateMessage;
import ru.romanov.durak.util.JsonHelper;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static ru.romanov.durak.websocket.message.MessageType.INVITE;
import static ru.romanov.durak.websocket.message.MessageType.REJECT_INVITE;

@Slf4j
public class Lobby {

    private HashBiMap<String, WebSocketSession> lobbySessions = HashBiMap.create();
    private Map<String, GameInvite> invites = ExpiringMap.builder()
            .expiration(30, TimeUnit.SECONDS)
            .asyncExpirationListener((ExpirationListener<String, GameInvite>) (invitee, invite) -> rejectInvite(invitee))
            .build();

    public void createInvite(String initiator, String invitee) {
        invites.put(invitee, new GameInvite(initiator, invitee));
        sendMessage(new InviteMessage(INVITE, initiator), lobbySessions.get(invitee));
    }

    public GameInvite getInvite(String invitee) {
        return invites.get(invitee);
    }

    public void rejectInvite(String invitee) {
        GameInvite invite = invites.remove(invitee);
        WebSocketSession initiatorSession = lobbySessions.get(invite.getInitiator());
        sendMessage(new InviteMessage(REJECT_INVITE), initiatorSession);
    }

    public void addUser(String username, WebSocketSession session) {
        lobbySessions.put(username, session);
        sendMessageToAll(new LobbyChangeMessage(MessageType.LOBBY_USER_CONNECTED, username), username);
    }

    public void removeByUsername(String username) {
        lobbySessions.remove(username);
        sendMessageToAll(new LobbyChangeMessage(MessageType.LOBBY_USER_DISCONNECTED, username), null);
    }

    public WebSocketSession getSessionByUsername(String username) {
        return lobbySessions.getOrDefault(username, null);
    }

    public void sendMessageToAll(Message message, String exceptUsername) {
        for (Map.Entry<String, WebSocketSession> session : lobbySessions.entrySet()) {
            if(session.getKey().equals(exceptUsername)) {
                continue;
            }
            sendMessage(message, session.getValue());
        }
    }

    public void sendLobbyState(String username, WebSocketSession session) {
        Set<String> usernames = getUsernames().stream()
                .filter(u -> !u.equals(username))
                .collect(Collectors.toSet());

        sendMessage(new LobbyStateMessage(usernames), session);
    }

    private Set<String> getUsernames() {
        if (lobbySessions.isEmpty()) {
            return Collections.emptySet();
        } else {
            return lobbySessions.keySet();
        }
    }

    private void sendMessage(Message message, WebSocketSession session) {
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
