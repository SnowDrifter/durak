package ru.romanov.durak.controller.websocket.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class LobbyStateMessage extends Message {

    public LobbyStateMessage(Set<String> usernames) {
        super(MessageType.LOBBY_STATE_MESSAGE);
        this.usernames = usernames;
    }

    private Set<String> usernames;

}
