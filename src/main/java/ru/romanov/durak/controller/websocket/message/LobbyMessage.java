package ru.romanov.durak.controller.websocket.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LobbyMessage extends Message {

    public LobbyMessage() {
        super(MessageType.LOBBY_MESSAGE);
    }

    private String usernames;

}
