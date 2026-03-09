package it.unibo.cactus.model.rounds.actions;

import it.unibo.cactus.model.rounds.RoundAction;
import it.unibo.cactus.model.rounds.RoundInternalState;

public class EndTurnAction implements RoundAction{
    @Override
    public void execute(final RoundInternalState round) {
        round.advancePhase();
    }
}
