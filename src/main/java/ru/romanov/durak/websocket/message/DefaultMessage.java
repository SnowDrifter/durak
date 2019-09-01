package ru.romanov.durak.websocket.message;

import lombok.*;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DefaultMessage extends Message {

    public DefaultMessage(MessageType type) {
        super(type);
    }

}
