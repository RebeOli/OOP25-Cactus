package it.unibo.cactus.model.cards;

import it.unibo.cactus.model.cards.target.PowerTarget;
import it.unibo.cactus.model.game.Game;
import it.unibo.cactus.model.players.Player;

/**
 * Represents a special power that can be activated when a specific card is played.
 */
public interface SpecialPower {

    /**
     * Executes the specific logic of the special power associated with the card.
     * The exact behavior depends on the concrete implementation (e.g., Swap, Peek, Reveal).
     *
     * @param game      the current {@link Game} context, providing access to the overall game state.
     * @param activator the {@link Player} who played the card and triggered the special power.
     * @param target    a {@link PowerTarget} object containing the specific payload/parameters 
     * needed for the power (e.g., target players, card indices). 
     * Concrete powers will cast this to their expected record type.
     * @throws IllegalArgumentException if the provided {@code target} is not of the expected type 
     * for the specific power implementation.
     */
    void activate(Game game, Player activator, PowerTarget target);
}
