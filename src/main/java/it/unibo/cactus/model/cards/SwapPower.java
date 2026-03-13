package it.unibo.cactus.model.cards;

import it.unibo.cactus.model.Game;
import it.unibo.cactus.model.cards.target.PowerTarget;
import it.unibo.cactus.model.cards.target.SwapTarget;
import it.unibo.cactus.model.players.Player;

public class SwapPower implements SpecialPower {

   @Override
    public void activate(Game game, Player activator, PowerTarget target) {
        if (!(target instanceof SwapTarget t)) {
            throw new IllegalArgumentException("SwapPower requires a target of type SwapTarget!");
        }
        Card cardA = t.playerA().getHand().getCard(t.indexA());
        Card cardB = t.playerB().getHand().getCard(t.indexB());
        t.playerA().getHand().replace(t.indexA(), cardB);
        t.playerB().getHand().replace(t.indexB(), cardA);
        System.out.println("SwapPower: Swap completed between " + t.playerA() + " and " + t.playerB());
    }
}
