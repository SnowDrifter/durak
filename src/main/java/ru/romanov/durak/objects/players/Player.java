package ru.romanov.durak.objects.players;


import org.springframework.web.socket.WebSocketSession;
import ru.romanov.durak.objects.Card;

import java.util.Set;

public interface Player {

    Card attack();

    Card defend(Card card);

    boolean isWin();

    Set<Card> getHand();

    void setWin(boolean win);

    boolean isTake();

    boolean isFinishMove();

    void setTake(boolean status);

    void setFinishMove(boolean status);

    void sendMessage(String message);

    void resetStatus();

    String getUsername();

    void selectCard(String cardName);

    void yourMove();

    void enemyMove();

    WebSocketSession getSession();

}
