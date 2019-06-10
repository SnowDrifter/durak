package ru.romanov.durak.object.player;


import lombok.Data;
import org.springframework.web.socket.WebSocketSession;
import ru.romanov.durak.controller.websocket.message.Message;
import ru.romanov.durak.object.Card;
import ru.romanov.durak.object.Game;

import java.util.HashSet;
import java.util.Set;

@Data
public abstract class Player {

    protected Set<Card> hand = new HashSet<>();
    protected Game game;
    protected boolean take;
    protected boolean finishMove;
    protected boolean win;

    abstract public Card attack();

    abstract public Card defend(Card card);

    abstract public void resetStatus();

    abstract public void selectCard(String cardName);

    abstract public void yourMove();

    abstract public void enemyMove();

    abstract public String getUsername();

    abstract public void sendMessage(String message);

    abstract public void sendMessage(Message message);

    abstract public WebSocketSession getSession();

}
