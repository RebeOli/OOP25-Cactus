package it.unibo.cactus.model.cards;

import it.unibo.cactus.model.game.Game;
import it.unibo.cactus.model.cards.target.PowerTarget;
import it.unibo.cactus.model.cards.target.RevealTarget;
import it.unibo.cactus.model.players.Player;

public class PeekPower implements SpecialPower {

    @Override
    public void activate(Game game, Player activator, PowerTarget target) {
        if (!(target instanceof RevealTarget t)) {
            throw new IllegalArgumentException("RevealPower requires a target of type RevealTarget!");
        }
        Card targetCard = t.player().getHand().getCard(t.index());
        System.out.println("RevealPower: Player " + activator + " revealed " + t.player() + "'s card: " + targetCard);
    }
}
