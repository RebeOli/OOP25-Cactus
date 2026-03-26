package it.unibo.cactus.model.cards;

import it.unibo.cactus.model.cards.target.PowerTarget;
import it.unibo.cactus.model.cards.target.RevealTarget;
import it.unibo.cactus.model.players.Player;

/**
 * Represents the "Reveal" special power in the game.
 * This power allows a player to choose and reveal one of an opponent's 
 * face-down cards, making it visible to everyone.
 */
public final class RevealPower implements SpecialPower {

   @Override
    public void activate(final Player activator, final PowerTarget target) {
        if (!(target instanceof RevealTarget t)) {
            throw new IllegalArgumentException("RevealPower requires a target of type RevealTarget!");
        }
        t.player().getHand().revealCard(t.index());
        t.player().getHand().getCard(t.index());
    }
}
