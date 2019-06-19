package ru.romanov.durak.controller.websocket.message;

import lombok.Data;

import java.util.Date;

@Data
public class ChatMessage extends Message {

    private String username;
    private String message;
    private Date time;

}
