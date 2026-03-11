package it.unibo.cactus.model.rounds;

import java.util.Optional;

import it.unibo.cactus.model.cards.Card;
import it.unibo.cactus.model.pile.DiscardPile;
import it.unibo.cactus.model.pile.DrawPile;
import it.unibo.cactus.model.players.Player;

/**
 * Internal interface for accessing and modifying the state of a round.
 * This interface is intended for use by {@link RoundAction} implementations only.
 * It exposes mutable state that should not be visible to external components such as the Controller.
 */
public interface RoundInternalState {
    /**
     * Returns the player whose turn it currently is.
     * @return the current {@link Player}.
     */
    public Player getCurrentPlayer();

    /**
     * Returns the card drawn from the draw pile during the current turn, if present.
     * @return an {@link Optional} containing the drawn {@link Card}, or empty if no card has been drawn yet.
     */
    public Optional<Card> getDrawnCard();

    /**
     * Sets the card drawn during the current turn.
     * @param card an {@link Optional} containing the drawn {@link Card}, or empty to clear it.
     */
    public void setDrawnCard(Optional<Card> card);

    /**
     * Returns the current phase of the turn.
     * @return the current {@link TurnPhase}.
     */
    public TurnPhase getPhase();

    /**
     * Advances the turn to the next phase.
     * The transition depends on the current phase and the state of the drawn card.
     */
    public void advancePhase();

    /**
     * Returns the draw pile of the current game.
     * @return the {@link DrawPile}.
     */
    public DrawPile getDrawPile();

    /**
     * Returns the discard pile of the current game.
     * @return the {@link DiscardPile}.
     */
    public DiscardPile getDiscardPile();

    /**
     * Sets whether this is the last round of the game.
     * Called when a player declares "Cactus!".
     * @param value {@code true} if this is the last round, {@code false} otherwise.
     */
    public void setLastRound(boolean value);
    
}
