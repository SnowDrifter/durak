package ru.romanov.durak.websocket.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
public class TableMessage extends Message {

    public TableMessage() {
        super(MessageType.UPDATE_TABLE);
    }

    private List<String> playerCards;
    private int enemyCardsCount;
    private String trump;
    private int deckSize;
    private List<String> tableCards;

}
