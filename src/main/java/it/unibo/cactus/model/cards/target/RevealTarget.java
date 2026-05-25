package it.unibo.cactus.model.cards.target;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.cactus.model.players.Player;

@SuppressFBWarnings(
    value = "EI", 
    justification = "The Target must hold the actual reference to the Player to apply the power, not a copy."
)
public record RevealTarget(Player player, int index) implements PowerTarget {

}

