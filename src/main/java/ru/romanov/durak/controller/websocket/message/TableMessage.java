package ru.romanov.durak.controller.websocket.message;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class TableMessage extends Message {

    public TableMessage() {
        super(MessageType.UPDATE_TABLE);
    }

    private String playerCards;
    private int enemyCardsCount;
    private String trump;
    private int deckSize;
    private String tableCards;

}
