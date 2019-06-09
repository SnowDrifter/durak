package ru.romanov.durak.controller.websocket.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = DefaultMessage.class, visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CardMessage.class, name = "SELECT_CARD")
})
public abstract class Message {

    private MessageType type;

}
