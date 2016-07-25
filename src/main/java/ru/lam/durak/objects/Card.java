package ru.lam.durak.objects;


public class Card {
    private String name;
    private Suit suit;
    private int power;
    private boolean trump;

    public Card(String name, Suit suit, int power, boolean isTrump) {
        this.suit = suit;
        this.name = name;
        this.power = power;
        this.trump = isTrump;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }
    public Suit getSuit() {
        return suit;
    }

    public boolean isTrump() {
        return trump;
    }
    public void setTrump(boolean trump) {
        this.trump = trump;
    }


    @Override
    public String toString() {
        return name + "(" + power + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        if (!name.equals(card.name)) return false;
        return suit == card.suit;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + suit.hashCode();
        return result;
    }
}
