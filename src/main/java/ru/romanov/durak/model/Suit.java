package ru.romanov.durak.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Suit {

    SPADES("s"),
    HEARTS("h"),
    DIAMONDS("d"),
    CLUBS("c");

    private final String letter;

}
