package ru.romanov.durak.controller.websocket.message;

public enum MessageType {
    CHAT_MESSAGE,
    LOBBY_MESSAGE,
    LOBBY_CHAT_MESSAGE,
    START_GAME,
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
    OFFER,
    ENEMY_DISCONNECTED
}
