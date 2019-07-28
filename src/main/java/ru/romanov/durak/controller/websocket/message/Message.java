package ru.romanov.durak.controller.websocket.message;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.EXISTING_PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = NAME, property = "type", defaultImpl = DefaultMessage.class, visible = true, include = EXISTING_PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CardMessage.class, name = "SELECT_CARD"),
        @JsonSubTypes.Type(value = OfferMessage.class, name = "OFFER"),
        @JsonSubTypes.Type(value = ChatMessage.class, name = "CHAT_MESSAGE"),
        @JsonSubTypes.Type(value = LobbyMessage.class, name = "LOBBY"),
        @JsonSubTypes.Type(value = LobbyChatMessage.class, name = "LOBBY_CHAT_MESSAGE")
})
public abstract class Message {

    private MessageType type;

}
