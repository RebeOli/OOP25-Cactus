package it.unibo.cactus.model.pile;

import java.util.List;
import java.util.Optional;

import it.unibo.cactus.model.cards.Card;


public interface DrawPile {

    /**
     * @return
     */
    public Optional<Card> draw();

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
    public int size(); //da togliere, non usare!!

}
