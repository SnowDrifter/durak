package ru.romanov.durak.controller.websocket.message;

public enum MessageType {
    CHAT_MESSAGE,
    LOBBY_MESSAGE,
    START,
    INIT_GAME,
    UPDATE_TABLE,
    SELECT_CARD,
    TAKE_CARD,
    WRONG_CARD,
    YOUR_MOVE,
    ENEMY_MOVE,
    FINISH_MOVE,
    WIN,
    LOSE,
    DRAW,
    ENEMY_DISCONNECTED
}
