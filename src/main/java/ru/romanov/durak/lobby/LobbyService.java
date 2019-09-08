package ru.romanov.durak.lobby;

import ru.romanov.durak.websocket.message.Message;

public interface LobbyService {

    void addUser(String username);

    void removeByUsername(String username);

    void sendMessageToAll(Message message, String exceptUsername);

    void sendLobbyState(String username);

}
