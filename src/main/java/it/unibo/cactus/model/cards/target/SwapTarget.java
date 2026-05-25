package it.unibo.cactus.model.cards.target;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.cactus.model.players.Player;

@SuppressFBWarnings(
    value = "EI", 
    justification = "The Target must hold the actual references to the Players to apply the power, not copies."
)
public record SwapTarget(Player playerA, int indexA, Player playerB, int indexB) implements PowerTarget {

}
