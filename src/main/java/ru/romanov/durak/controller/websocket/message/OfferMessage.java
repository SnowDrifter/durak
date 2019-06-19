package ru.romanov.durak.controller.websocket.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OfferMessage extends Message {

    private String firstUsername;
    private String secondUsername;

}
