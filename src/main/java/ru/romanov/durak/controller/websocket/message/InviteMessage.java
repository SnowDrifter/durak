package ru.romanov.durak.controller.websocket.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InviteMessage extends Message {

    private String initiator;
    private String invitee;

    public InviteMessage(MessageType type, String initiator) {
        super(type);
        this.initiator = initiator;
    }

    public InviteMessage(MessageType type) {
        super(type);
    }
}
