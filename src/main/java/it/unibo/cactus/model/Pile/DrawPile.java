package it.unibo.cactus.model;

import java.util.List;


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
