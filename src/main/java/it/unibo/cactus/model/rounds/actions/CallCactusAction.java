package it.unibo.cactus.model.rounds.actions;

import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.MutableRound;

/**
 * Action that declares "Cactus!", marking the current round as the last one.
 */
public final class CallCactusAction implements RoundAction {

    @Override
    public void execute(final MutableRound round) {
        round.setLastRound(true);
        round.advancePhase();
    }

}
