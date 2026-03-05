package it.unibo.cactus.model.Pile;

import java.util.List;

import it.unibo.cactus.model.Cards.Card;


public interface DrawPile {

    /**
     * @return
     */
    public Card draw();

    /**
     * @param cards
     */
    public void refill(List<Card> cards);

    /**
     * @return
     */
    public boolean isEmpty();

    /**
     * @return
     */
    public int size();

}
