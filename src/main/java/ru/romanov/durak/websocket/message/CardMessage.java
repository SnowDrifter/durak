package ru.romanov.durak.websocket.message;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CardMessage extends Message {

    private String card;

}
