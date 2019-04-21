package ru.romanov.durak.object;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Card {

    private String name;
    private Suit suit;
    private int power;
    private boolean trump;

}
