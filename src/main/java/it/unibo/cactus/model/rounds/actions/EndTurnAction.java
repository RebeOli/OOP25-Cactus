package it.unibo.cactus.model.rounds.actions;

import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.MutableRound;

/**
 * Action that ends the current turn without calling "Cactus!".
 */
public final class EndTurnAction implements RoundAction {

    @Override
    public void execute(final MutableRound round) {
        round.advancePhase();
    }

}
