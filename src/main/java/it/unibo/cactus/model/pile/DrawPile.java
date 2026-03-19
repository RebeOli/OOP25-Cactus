package it.unibo.cactus.model.pile;

import java.util.List;
import java.util.Optional;

import it.unibo.cactus.model.cards.Card;

/**
 * Represents the draw pile in the "Cactus!" card game.
 * The draw pile is the main source of cards during the game.
 * At the start of each game it contains the full shuffled deck,
 * and when it runs out of cards it is refilled with the cards
 * from the discard pile.
 */
public interface DrawPile {

    /**
     * Draws the top card from the draw pile, removing it from the pile.
     * If the draw pile is empty, an empty {@link Optional} is returned.
     *
     * @return an {@link Optional} containing the drawn {@link Card},
     *         or an empty {@link Optional} if the pile is empty.
     */
    Optional<Card> draw();

    /**
     * Refills the draw pile with the given list of cards.
     * The cards are shuffled before being added to the pile.
     * This is typically called when the draw pile runs out of cards,
     * using the cards collected from the discard pile.
     *
     * @param cards the {@link List} of {@link Card} to add to the pile;
     *              must not be null.
     */
    void refill(List<Card> cards);

    /**
     * Checks whether the draw pile is currently empty.
     *
     * @return {@code true} if the draw pile contains no cards,
     *         {@code false} otherwise.
     */
    boolean isEmpty();

    /**
     * @return the pile's size.
     */
    int size(); //da togliere, non usare!!

}
