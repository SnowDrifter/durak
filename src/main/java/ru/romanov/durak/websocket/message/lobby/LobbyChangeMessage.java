package ru.romanov.durak.websocket.message.lobby;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.romanov.durak.websocket.message.Message;
import ru.romanov.durak.websocket.message.MessageType;

@Data
@EqualsAndHashCode(callSuper = true)
public class LobbyChangeMessage extends Message {

    public LobbyChangeMessage(MessageType type, String username) {
        super(type);
        this.username = username;
    }

    private String username;

}
