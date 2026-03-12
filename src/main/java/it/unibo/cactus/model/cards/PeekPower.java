package it.unibo.cactus.model.cards;

import it.unibo.cactus.model.Game;
import it.unibo.cactus.model.players.Player;

public class PeekPower implements SpecialPower {

    @Override
    public void activate(Game game, Player activator) {
        System.out.println("Power: Peek (6) activated for player " + activator);
    }
}
