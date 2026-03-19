package it.unibo.cactus.model.cards;

import it.unibo.cactus.model.game.Game;
import it.unibo.cactus.model.cards.target.PeekTarget;
import it.unibo.cactus.model.cards.target.PowerTarget;
import it.unibo.cactus.model.players.Player;

/**
 * Represents the "Peek" special power in the game.
 * This power allows a player to secretly look at one of their own face-down cards
 * without revealing it to the other players.
 */
public final class PeekPower implements SpecialPower {

    @Override
    public void activate(final Game game, final Player activator, final PowerTarget target) {
        if (!(target instanceof PeekTarget t)) {
            throw new IllegalArgumentException("PeekPower requires a target of type PeekTarget!");
        }
        activator.getHand().getCard(t.index());
    }
}
