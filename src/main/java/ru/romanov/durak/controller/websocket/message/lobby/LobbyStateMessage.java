package ru.romanov.durak.controller.websocket.message.lobby;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.romanov.durak.controller.websocket.message.Message;
import ru.romanov.durak.controller.websocket.message.MessageType;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class LobbyStateMessage extends Message {

    public LobbyStateMessage(Set<String> usernames) {
        super(MessageType.LOBBY_STATE);
        this.usernames = usernames;
    }

    private Set<String> usernames;

}
