package it.unibo.cactus.model.cards;

import it.unibo.cactus.model.cards.target.PowerTarget;
import it.unibo.cactus.model.players.Player;

/**
 * Represents a special power that can be activated when a specific card is played.
 */
@FunctionalInterface
public interface SpecialPower {

    /**
     * Executes the specific logic of the special power associated with the card.
     * The exact behavior depends on the concrete implementation (e.g., Swap, Peek, Reveal).
     *
     * @param activator the {@link Player} who played the card and triggered the special power.
     * @param target    a {@link PowerTarget} object containing the specific payload/parameters needed for the power.
     * @throws IllegalArgumentException if the provided {@code target} is not of the expected type.
     */
    void activate(Player activator, PowerTarget target);
}
