package it.unibo.cactus.model.rounds.actions;

import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.RoundInternalState;

/**
 * Action that skips the special power of the discarded card.
 */
public final class SkipPowerAction implements RoundAction {

    @Override
    public void execute(final RoundInternalState round) {
        round.advancePhase();
    }

}
