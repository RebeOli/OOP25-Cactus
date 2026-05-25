package it.unibo.cactus.model.cards;

import it.unibo.cactus.model.cards.target.PeekTarget;
import it.unibo.cactus.model.cards.target.PowerTarget;
import it.unibo.cactus.model.players.Player;

/**
 * Represents the "Peek" special power in the game.
 * This power allows a player to secretly look at one of their own face-down cards.
 */
public final class PeekPower implements SpecialPower {

    @Override
    public void activate(final Player activator, final PowerTarget target) {
        if (!(target instanceof PeekTarget)) {
            throw new IllegalArgumentException("PeekPower requires a target of type PeekTarget");
        }
    }
}
