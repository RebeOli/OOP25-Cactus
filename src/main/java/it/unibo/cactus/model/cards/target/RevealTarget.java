package it.unibo.cactus.model.cards.target;

import it.unibo.cactus.model.players.Player;

public record RevealTarget(Player player, int index) implements PowerTarget {

}

