package ru.romanov.durak.object;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Table {

    private List<Card> oldCards = new ArrayList<>();
    private Card currentCard;

    public void resetTable() {
        oldCards = new ArrayList<>();
        currentCard = null;
    }

    public String getCardNames() {
        StringBuilder result = new StringBuilder();
        if(!oldCards.isEmpty()){
            for (Card card : oldCards){
                result.append(card.getName());
                result.append(" ");
            }
        }
        if(currentCard!=null){
            result.append(currentCard.getName());
            result.append(" ");
        }

        if(result.length()>0){
            result.delete(result.length()-1, result.length());
        }

        return result.toString();
    }

    public List<Card> getAllCardsOnTable() {
        List<Card> result = new ArrayList<>();
        result.addAll(oldCards);
        result.add(currentCard);
        return result;
    }

    public boolean isClean() {
        return oldCards.isEmpty() && currentCard == null;
    }

}
