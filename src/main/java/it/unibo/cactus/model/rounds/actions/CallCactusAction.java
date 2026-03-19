package it.unibo.cactus.model.rounds.actions;

import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.RoundInternalState;

/**
 * Action that declares "Cactus!", marking the current round as the last one.
 */
public final class CallCactusAction implements RoundAction {

    @Override
    public void execute(final RoundInternalState round) {
        round.setLastRound(true);
        round.advancePhase();
    }

}
