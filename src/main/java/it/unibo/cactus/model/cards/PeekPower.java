package it.unibo.cactus.model.cards;

import it.unibo.cactus.model.Game;
import it.unibo.cactus.model.cards.target.PeekTarget;
import it.unibo.cactus.model.cards.target.PowerTarget;
import it.unibo.cactus.model.players.Player;

public class PeekPower implements SpecialPower {

    @Override
    public void activate(Game game, Player activator, PowerTarget target) {
        if (!(target instanceof PeekTarget t)) {
            throw new IllegalArgumentException("PeekPower requires a target of type PeekTarget!");
        }
        Card myCard = activator.getHand().getCard(t.index());
        System.out.println("PeekPower: Player " + activator + " is looking at their card: " + myCard);
    }
}
