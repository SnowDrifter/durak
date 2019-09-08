package ru.romanov.durak.model.player;


import lombok.Data;
import ru.romanov.durak.model.Card;
import ru.romanov.durak.model.Game;

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

    abstract public String getUsername();

}
