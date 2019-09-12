package ru.romanov.durak.game;

import ru.romanov.durak.websocket.message.Message;

public interface GameService {

    void processSingleplayerMessage(String username, Message message);

    void processMultiplayerMessage(String username, Message message);

    void createMultiplayerGame(String firstUsername, String secondUsername);

    void playerDisconnected(String username);

}
