package ru.romanov.durak.websocket.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChatMessage extends Message {

    private String username;
    private String message;

}
