package it.unibo.cactus.model.pile;

import java.util.List;
import java.util.Optional;

import it.unibo.cactus.model.cards.Card;

/**
 * Represents the discard pile in a card game.
 * This interface manages the accumulation of played cards, retrieval of the 
 * top-most card, and operations for clearing or simultaneous play.
 */
public interface DiscardPile {

    /**
     * Adds a card to the top of the discard pile.
     * * @param card The card to be discarded. Should not be {@code null}.
     */
    public void discard(Card card);

    /**
     * Retrieves, but does not remove, the card currently at the top of the pile.
     * * @return The {@link Card} at the top of the pile, or {@code null} if the pile is empty.
     */
    public Optional<Card> getTopCard();

    public List<Card> drainAll();

    /**
     * Checks whether the discard pile contains any cards.
     * * @return {@code true} if the pile is empty, {@code false} otherwise.
     */
    public boolean isEmpty();

    //public boolean trySimultaneousDiscard(Card card);
}
