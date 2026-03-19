package it.unibo.cactus.model.rounds.actions;

import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.RoundInternalState;

/**
 * Action that ends the current turn without calling "Cactus!".
 */
public final class EndTurnAction implements RoundAction {

    @Override
    public void execute(final RoundInternalState round) {
        round.advancePhase();
    }

}
