package it.unibo.cactus.model.cards.target;

import it.unibo.cactus.model.players.Player;

public record SwapTarget(Player playerA, int indexA, Player playerB, int indexB) implements PowerTarget {

}
