package it.unibo.cactus.model.rounds.actions;

import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.MutableRound;

/**
 * Action that skips the special power of the discarded card.
 */
public final class SkipPowerAction implements RoundAction {

    @Override
    public void execute(final MutableRound round) {
        round.advancePhase();
    }

}
