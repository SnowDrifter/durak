package ru.romanov.durak.controller.websocket.message;

import lombok.*;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DefaultMessage extends Message {

    public DefaultMessage(MessageType type) {
        super(type);
    }

}
