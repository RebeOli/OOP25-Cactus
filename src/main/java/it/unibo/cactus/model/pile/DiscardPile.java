package it.unibo.cactus.model.pile;

import java.util.List;
import java.util.Optional;

import it.unibo.cactus.model.cards.Card;

/**
 * Represents the discard pile in the "Cactus!" card game.
 * The discard pile collects cards that players have discarded during their turns.
 */
public interface DiscardPile {

    /**
     * Places a card on top of the discard pile.
     * This happens when a player decides to discard a card from their hand
     * or when a drawn card is discarded without being kept.
     *
     * @param card the {@link Card} to place on top of the discard pile;
     *             must not be null.
     */
    void discard(Card card);

    /**
     * Retrieves, but does not remove, the card currently at the top of the discard pile.
     * The top card of the discard pile is always visible to all players,
     * as it is placed face-up on the table during the game.
     *
     * @return an {@link Optional} containing the {@link Card} at the top of the pile,
     *         or an empty {@link Optional} if the pile is empty.
     */
    Optional<Card> getTopCard();

    /**
     * Removes and returns all cards currently in the discard pile.
     * This is used to refill the draw pile when it runs out of cards:
     * all discarded cards are collected, shuffled, and moved back
     * to the draw pile.
     *
     * @return a {@link List} containing all the cards that were in the discard pile;
     *         the list is empty if the pile was already empty.
     */
    List<Card> drainAll();

    /**
     * Checks whether the discard pile is currently empty.
     *
     * @return {@code true} if the discard pile contains no cards,
     *         {@code false} otherwise.
     */
    boolean isEmpty();

    //public boolean trySimultaneousDiscard(Card card);
}
