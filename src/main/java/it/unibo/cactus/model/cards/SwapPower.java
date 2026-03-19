package it.unibo.cactus.model.cards;

import it.unibo.cactus.model.game.Game;
import it.unibo.cactus.model.cards.target.PowerTarget;
import it.unibo.cactus.model.cards.target.SwapTarget;
import it.unibo.cactus.model.players.Player;

/**
 * Represents the "Swap" special power in the game.
 * This power allows a player to physically swap a specific card from one player's 
 * hand with a specific card from another player's hand.
 */
public final class SwapPower implements SpecialPower {

   @Override
    public void activate(final Game game, final Player activator, final PowerTarget target) {
        if (!(target instanceof SwapTarget t)) {
            throw new IllegalArgumentException("SwapPower requires a target of type SwapTarget!");
        }
        final Card cardA = t.playerA().getHand().getCard(t.indexA());
        final Card cardB = t.playerB().getHand().getCard(t.indexB());
        t.playerA().getHand().replace(t.indexA(), cardB);
        t.playerB().getHand().replace(t.indexB(), cardA);
    }
}
