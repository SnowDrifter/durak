package ru.lam.durak.objects;

import com.google.common.collect.HashBiMap;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public class Lobby {

    private static final Logger logger = LogManager.getLogger(Lobby.class); // TODO: add logs
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

    public synchronized WebSocketSession getSessionByUsername(String username) {
        if (lobby.containsKey(username)) {
            return lobby.get(username);
        } else {
            return null;
        }
    }

    public synchronized String getStringWithUsernames() {
        if (lobby.isEmpty()) return null;

        StringBuilder result = new StringBuilder();

        for (String username : lobby.keySet()) {
            result.append(username);
            result.append(",");
        }
        result.delete(result.length() - 1, result.length());
        return result.toString();
    }

    public void sendMessageToAll(String message) {
        for (WebSocketSession session : lobby.values()) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void updateLobbyView() {
        sendMessageToAll("lobby:" + getStringWithUsernames());
    }

}
