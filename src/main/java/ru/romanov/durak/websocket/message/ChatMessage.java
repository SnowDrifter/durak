package ru.romanov.durak.websocket.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChatMessage extends Message {

    private String username;
    private String message;

}
