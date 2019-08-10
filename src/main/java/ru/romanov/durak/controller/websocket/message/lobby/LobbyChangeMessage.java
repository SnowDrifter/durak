package ru.romanov.durak.controller.websocket.message.lobby;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.romanov.durak.controller.websocket.message.Message;
import ru.romanov.durak.controller.websocket.message.MessageType;

@Data
@EqualsAndHashCode(callSuper = true)
public class LobbyChangeMessage extends Message {

    public LobbyChangeMessage(MessageType type, String username) {
        super(type);
        this.username = username;
    }

    private String username;

}
