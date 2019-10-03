package ru.romanov.durak.model;

import com.google.common.collect.ComparisonChain;

import java.util.Comparator;

public class CardComparator implements Comparator<Card> {

    @Override
    public int compare(Card c1, Card c2) {
        if (c1.equals(c2)) {
            return 0;
        }

        return ComparisonChain.start()
                .compareFalseFirst(c1.isTrump(), c2.isTrump())
                .compare(c1.getName(), c2.getName())
                .result();
    }
}
