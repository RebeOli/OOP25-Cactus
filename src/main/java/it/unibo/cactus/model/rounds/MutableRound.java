package it.unibo.cactus.model.rounds;

import java.util.Optional;
import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.pile.DiscardPile;
import it.unibo.cactus.model.pile.DrawPile;

/**
 * Extends {@link Round} with methods to modify the round state.
 * Only {@link RoundAction} implementations should use this interface.
 */
public interface MutableRound extends Round {

    /**
     * Returns the card drawn from the draw pile during the current turn, if present.
     * 
     * @return an {@link Optional} containing the drawn {@link Card}, or empty if no card has been drawn yet.
     */
    Optional<Card> getDrawnCard();

    /**
     * Sets the card drawn during the current turn.
     * 
     * @param card an {@link Optional} containing the drawn {@link Card}, or empty to clear it.
     */
    void setDrawnCard(Optional<Card> card);

    /**
     * Advances the turn to the next phase.
     * The transition depends on the current phase and the state of the drawn card.
     */
    void advancePhase();

    /**
     * Returns the draw pile of the current game.
     * 
     * @return the {@link DrawPile}.
     */
    DrawPile getDrawPile();

    /**
     * Returns the discard pile of the current game.
     * 
     * @return the {@link DiscardPile}.
     */
    DiscardPile getDiscardPile();

    /**
     * Sets whether this is the last round of the game.
     * Called when a player declares "Cactus!".
     * 
     * @param value true if this is the last round, false otherwise.
     */
    void setLastRound(boolean value);

}
